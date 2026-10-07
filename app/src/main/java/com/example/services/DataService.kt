package com.example.services

import android.content.Context
import com.example.R
import com.example.types.Cliente
import com.example.types.InteracaoAtendimento
import com.example.types.MetricasDashboard
import com.example.types.Pedido
import com.example.types.PlanoAlimentar
import com.example.types.Produto
import com.example.types.StatusDisponibilidade
import com.example.types.StatusPedido
import com.example.types.UsuarioSistema
import com.example.utils.Formatadores
import com.example.utils.MapeadoresFirestore
import com.example.utils.MapeadoresPedidosPlanos
import com.example.utils.OperationType
import com.example.utils.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class DataService(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        ),
        Firebase.auth
    )

    private fun requireUserId(): String =
        auth.currentUser?.uid
            ?: throw IllegalStateException("Sessão expirada: faça login com o Google para acessar seus dados.")

    private fun colecaoDoUsuario(userId: String, nomeColecao: String) =
        db.collection("users").document(userId).collection(nomeColecao)

    private suspend fun <T> executarSeguramente(
        path: String,
        operacao: OperationType,
        bloco: suspend () -> T
    ): Result<T> = runCatching {
        try {
            bloco()
        } catch (e: Exception) {
            handleFirestoreError(e, operacao, path)
            throw e
        }
    }

    private suspend fun salvarOuAtualizarDoc(
        ref: DocumentReference,
        ehEdicao: Boolean,
        mapaCriacao: Map<String, Any>,
        mapaAtualizacao: Map<String, Any>
    ): Result<Unit> = executarSeguramente(
        ref.path,
        if (ehEdicao) OperationType.UPDATE else OperationType.CREATE
    ) {
        if (ehEdicao && ref.get().await().exists()) {
            ref.update(mapaAtualizacao).await()
        } else {
            ref.set(mapaCriacao).await()
        }
    }

    fun observarPerfil(userId: String): Flow<UsuarioSistema?> =
        db.collection("users").document(userId).snapshots()
            .map { snap -> if (snap.exists()) MapeadoresFirestore.documentoParaPerfil(snap) else null }
            .catch { e ->
                if (e is Exception) handleFirestoreError(e, OperationType.GET, "users/$userId")
                throw e
            }

    fun observarProdutos(userId: String): Flow<List<Produto>> =
        colecaoDoUsuario(userId, "produtos").whereEqualTo("userId", userId).snapshots()
            .map { snap -> snap.documents.map { MapeadoresFirestore.documentoParaProduto(it) } }
            .catch { e ->
                if (e is Exception) handleFirestoreError(e, OperationType.LIST, "users/$userId/produtos")
                throw e
            }

    fun observarClientes(userId: String): Flow<List<Cliente>> =
        colecaoDoUsuario(userId, "clientes").whereEqualTo("userId", userId).snapshots()
            .map { snap -> snap.documents.map { MapeadoresFirestore.documentoParaCliente(it) } }
            .catch { e ->
                if (e is Exception) handleFirestoreError(e, OperationType.LIST, "users/$userId/clientes")
                throw e
            }

    fun observarPedidos(userId: String): Flow<List<Pedido>> =
        colecaoDoUsuario(userId, "pedidos").whereEqualTo("userId", userId).snapshots()
            .map { snap -> snap.documents.map { MapeadoresPedidosPlanos.documentoParaPedido(it) } }
            .catch { e ->
                if (e is Exception) handleFirestoreError(e, OperationType.LIST, "users/$userId/pedidos")
                throw e
            }

    fun observarPlanos(userId: String): Flow<List<PlanoAlimentar>> =
        colecaoDoUsuario(userId, "planos").whereEqualTo("userId", userId).snapshots()
            .map { snap -> snap.documents.map { MapeadoresPedidosPlanos.documentoParaPlano(it) } }
            .catch { e ->
                if (e is Exception) handleFirestoreError(e, OperationType.LIST, "users/$userId/planos")
                throw e
            }

    suspend fun inicializarTenantUsuario(
        perfilInicial: UsuarioSistema,
        popularDemoSeVazio: Boolean = true
    ): Result<Unit> = runCatching {
        val uid = requireUserId()
        val docPerfilRef = db.collection("users").document(uid)
        val snapPerfil = executarSeguramente(docPerfilRef.path, OperationType.GET) { docPerfilRef.get().await() }.getOrThrow()
        if (!snapPerfil.exists()) {
            executarSeguramente(docPerfilRef.path, OperationType.CREATE) {
                docPerfilRef.set(MapeadoresFirestore.perfilParaMapaCriacao(perfilInicial, uid)).await()
            }.getOrThrow()
        }
        if (popularDemoSeVazio && colecaoDoUsuario(uid, "produtos").whereEqualTo("userId", uid).limit(1).get().await().isEmpty) {
            restaurarDadosDemonstracao().getOrThrow()
        }
    }

    suspend fun atualizarPerfilTenant(perfil: UsuarioSistema): Result<Unit> {
        val uid = requireUserId()
        return salvarOuAtualizarDoc(
            ref = db.collection("users").document(uid),
            ehEdicao = true,
            mapaCriacao = MapeadoresFirestore.perfilParaMapaCriacao(perfil, uid),
            mapaAtualizacao = MapeadoresFirestore.perfilParaMapaAtualizacao(perfil)
        )
    }

    suspend fun restaurarDadosDemonstracao(): Result<Unit> {
        val uid = requireUserId()
        val userRef = db.collection("users").document(uid)
        return executarSeguramente(userRef.path, OperationType.WRITE) {
            val batch = db.batch()
            DadosSimuladosProdutos.obterProdutosIniciais().forEach {
                batch.set(userRef.collection("produtos").document(it.id), MapeadoresFirestore.produtoParaMapaCriacao(it, uid))
            }
            DadosSimuladosCRM.obterClientesIniciais().forEach {
                batch.set(userRef.collection("clientes").document(it.id), MapeadoresFirestore.clienteParaMapaCriacao(it, uid))
            }
            DadosSimuladosCRM.obterPedidosIniciais().forEach {
                batch.set(userRef.collection("pedidos").document(it.id), MapeadoresPedidosPlanos.pedidoParaMapaCriacao(it, uid))
            }
            DadosSimuladosPlanos.obterPlanosIniciais().forEach {
                batch.set(userRef.collection("planos").document(it.id), MapeadoresPedidosPlanos.planoParaMapaCriacao(it, uid))
            }
            batch.commit().await()
        }
    }

    suspend fun salvarProduto(produto: Produto, ehEdicao: Boolean): Result<String> {
        val uid = requireUserId()
        val atualizado = produto.copy(statusDisponibilidade = Formatadores.calcularStatusEstoque(produto.estoqueSimulado))
        val ref = colecaoDoUsuario(uid, "produtos").document(atualizado.id)
        return salvarOuAtualizarDoc(
            ref, ehEdicao, MapeadoresFirestore.produtoParaMapaCriacao(atualizado, uid), MapeadoresFirestore.produtoCamposMutaveis(atualizado)
        ).map { atualizado.id }
    }

    suspend fun obterProdutoDeTenantPorId(tenantUserId: String, produtoId: String): Result<Produto> {
        val ref = colecaoDoUsuario(tenantUserId, "produtos").document(produtoId)
        return executarSeguramente(ref.path, OperationType.GET) { MapeadoresFirestore.documentoParaProduto(ref.get().await()) }
    }

    suspend fun removerProduto(produtoId: String): Result<Unit> =
        removerDocColecao("produtos", produtoId)

    suspend fun salvarCliente(cliente: Cliente, ehEdicao: Boolean): Result<Unit> {
        val uid = requireUserId()
        val ref = colecaoDoUsuario(uid, "clientes").document(cliente.id)
        return salvarOuAtualizarDoc(ref, ehEdicao, MapeadoresFirestore.clienteParaMapaCriacao(cliente, uid), MapeadoresFirestore.clienteCamposMutaveis(cliente))
    }

    suspend fun removerCliente(clienteId: String): Result<Unit> =
        removerDocColecao("clientes", clienteId)

    suspend fun registrarInteracaoCliente(clienteAtual: Cliente, interacao: InteracaoAtendimento): Result<Unit> =
        salvarCliente(clienteAtual.copy(historicoInteracoes = listOf(interacao) + clienteAtual.historicoInteracoes), true)

    suspend fun registrarNovoPedido(pedido: Pedido, produtosAtuais: List<Produto>): Result<Unit> {
        val uid = requireUserId()
        val pedRef = colecaoDoUsuario(uid, "pedidos").document(pedido.id)
        return executarSeguramente(pedRef.path, OperationType.CREATE) {
            pedRef.set(MapeadoresPedidosPlanos.pedidoParaMapaCriacao(pedido, uid)).await()
            pedido.itens.forEach { itemVendido ->
                produtosAtuais.find { it.id == itemVendido.produtoId }?.let { prod ->
                    val novoEstoque = (prod.estoqueSimulado - itemVendido.quantidade).coerceAtLeast(0)
                    salvarProduto(prod.copy(estoqueSimulado = novoEstoque), ehEdicao = true)
                }
            }
        }
    }

    suspend fun atualizarStatusPedido(pedidoId: String, novoStatus: String): Result<Unit> {
        val ref = colecaoDoUsuario(requireUserId(), "pedidos").document(pedidoId)
        return executarSeguramente(ref.path, OperationType.UPDATE) {
            ref.update(mapOf("status" to novoStatus, "updatedAt" to FieldValue.serverTimestamp())).await()
        }
    }

    suspend fun removerPedido(pedidoId: String): Result<Unit> =
        removerDocColecao("pedidos", pedidoId)

    suspend fun salvarPlano(plano: PlanoAlimentar, ehEdicao: Boolean): Result<Unit> {
        val uid = requireUserId()
        val ref = colecaoDoUsuario(uid, "planos").document(plano.id)
        return salvarOuAtualizarDoc(ref, ehEdicao, MapeadoresPedidosPlanos.planoParaMapaCriacao(plano, uid), MapeadoresPedidosPlanos.planoCamposMutaveis(plano))
    }

    suspend fun removerPlano(planoId: String): Result<Unit> =
        removerDocColecao("planos", planoId)

    private suspend fun removerDocColecao(nomeColecao: String, docId: String): Result<Unit> {
        val ref = colecaoDoUsuario(requireUserId(), nomeColecao).document(docId)
        return executarSeguramente(ref.path, OperationType.DELETE) { ref.delete().await() }
    }

    fun calcularMetricas(
        produtos: List<Produto>,
        clientes: List<Cliente>,
        pedidos: List<Pedido>,
        planos: List<PlanoAlimentar>
    ): MetricasDashboard {
        val validos = pedidos.filterNot { it.status == StatusPedido.CANCELADO.rotulo }
        val faturamento = validos.sumOf { it.total }
        val ticket = if (validos.isNotEmpty()) faturamento / validos.size else 0.0
        val ativos = pedidos.count { it.status != StatusPedido.ENTREGUE.rotulo && it.status != StatusPedido.CANCELADO.rotulo }
        val criticos = produtos.count { it.statusDisponibilidade != StatusDisponibilidade.DISPONIVEL.rotulo }
        return MetricasDashboard(faturamento, ticket, pedidos.size, ativos, clientes.size, planos.count { it.ativo }, criticos)
    }
}
