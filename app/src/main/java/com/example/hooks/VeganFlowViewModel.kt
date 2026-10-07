package com.example.hooks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.services.DataService
import com.example.types.Cliente
import com.example.types.InteracaoAtendimento
import com.example.types.MetricasDashboard
import com.example.types.Pedido
import com.example.types.PlanoAlimentar
import com.example.types.Produto
import com.example.types.UsuarioSistema
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

enum class AbaNavegacao(val titulo: String) {
    PAINEL("Painel"),
    PRODUTOS("Produtos"),
    CLIENTES("Clientes"),
    PEDIDOS("Pedidos"),
    PLANOS("Planos")
}

data class EstadoVeganFlow(
    val abaAtual: AbaNavegacao = AbaNavegacao.PAINEL,
    val produtos: List<Produto> = emptyList(),
    val clientes: List<Cliente> = emptyList(),
    val pedidos: List<Pedido> = emptyList(),
    val planos: List<PlanoAlimentar> = emptyList(),
    val metricas: MetricasDashboard = MetricasDashboard(0.0, 0.0, 0, 0, 0, 0, 0),
    val usuarioSistema: UsuarioSistema = UsuarioSistema("", "", "", "", ""),
    val buscaProdutos: String = "",
    val categoriaFiltro: String = "Todas",
    val buscaClientes: String = "",
    val statusPedidoFiltro: String = "Todos",
    val clientePlanoFiltroId: String = "Todos",
    val carregando: Boolean = false,
    val mensagemFeedback: String? = null
)

class VeganFlowViewModel(
    private val dataService: DataService,
    private val currentUserId: String,
    nomeInicial: String = "Profissional Vegan Flow",
    emailInicial: String = ""
) : ViewModel() {

    private val _estado = MutableStateFlow(
        EstadoVeganFlow(
            usuarioSistema = UsuarioSistema(
                id = currentUserId,
                nome = nomeInicial.ifBlank { "Profissional Vegan Flow" },
                cargo = "Nutricionista Clínica & Esportiva Plant-Based",
                crnOuRegistro = "CRN-3 Ativo",
                email = emailInicial
            ),
            carregando = true
        )
    )
    val estado: StateFlow<EstadoVeganFlow> = _estado.asStateFlow()

    init {
        iniciarObservadoresTempoReal()
    }

    private fun iniciarObservadoresTempoReal() {
        viewModelScope.launch {
            dataService.observarPerfil(currentUserId)
                .catch { e -> registrarErroStream("perfil", e) }
                .collect { perfil ->
                    if (perfil != null) {
                        _estado.value = _estado.value.copy(usuarioSistema = perfil)
                    }
                }
        }
        viewModelScope.launch {
            dataService.observarProdutos(currentUserId)
                .catch { e -> registrarErroStream("produtos", e) }
                .collect { lista -> atualizarColecoes(produtos = lista) }
        }
        viewModelScope.launch {
            dataService.observarClientes(currentUserId)
                .catch { e -> registrarErroStream("clientes", e) }
                .collect { lista -> atualizarColecoes(clientes = lista) }
        }
        viewModelScope.launch {
            dataService.observarPedidos(currentUserId)
                .catch { e -> registrarErroStream("pedidos", e) }
                .collect { lista -> atualizarColecoes(pedidos = lista) }
        }
        viewModelScope.launch {
            dataService.observarPlanos(currentUserId)
                .catch { e -> registrarErroStream("planos", e) }
                .collect { lista -> atualizarColecoes(planos = lista) }
        }
    }

    private fun atualizarColecoes(
        produtos: List<Produto> = _estado.value.produtos,
        clientes: List<Cliente> = _estado.value.clientes,
        pedidos: List<Pedido> = _estado.value.pedidos,
        planos: List<PlanoAlimentar> = _estado.value.planos
    ) {
        val metricas = dataService.calcularMetricas(produtos, clientes, pedidos, planos)
        _estado.value = _estado.value.copy(
            produtos = produtos,
            clientes = clientes,
            pedidos = pedidos,
            planos = planos,
            metricas = metricas,
            carregando = false
        )
    }

    private fun registrarErroStream(colecao: String, erro: Throwable) {
        Log.w("VeganFlowVM", "Erro observando $colecao no Firestore", erro)
        _estado.value = _estado.value.copy(
            carregando = false,
            mensagemFeedback = "⚠️ Falha ao sincronizar $colecao: ${erro.localizedMessage ?: "Permissão negada"}"
        )
    }

    fun inicializarTenantSeNecessario(cargo: String, crn: String, popularDemo: Boolean) {
        val perfil = _estado.value.usuarioSistema.copy(
            id = currentUserId,
            cargo = cargo.ifBlank { _estado.value.usuarioSistema.cargo },
            crnOuRegistro = crn.ifBlank { _estado.value.usuarioSistema.crnOuRegistro }
        )
        executarOperacaoFirestore("✅ Ambiente Multi-Tenant sincronizado no Firestore!") {
            dataService.inicializarTenantUsuario(perfil, popularDemo)
        }
    }

    fun atualizarPerfilTenant(novoPerfil: UsuarioSistema) {
        executarOperacaoFirestore("✅ Perfil profissional do Tenant atualizado!") {
            dataService.atualizarPerfilTenant(novoPerfil)
        }
    }

    fun selecionarAba(aba: AbaNavegacao) {
        _estado.value = _estado.value.copy(abaAtual = aba)
    }

    fun atualizarBuscaProdutos(termo: String) {
        _estado.value = _estado.value.copy(buscaProdutos = termo)
    }

    fun atualizarCategoriaFiltro(categoria: String) {
        _estado.value = _estado.value.copy(categoriaFiltro = categoria)
    }

    fun atualizarBuscaClientes(termo: String) {
        _estado.value = _estado.value.copy(buscaClientes = termo)
    }

    fun atualizarFiltroStatusPedido(status: String) {
        _estado.value = _estado.value.copy(statusPedidoFiltro = status)
    }

    fun atualizarFiltroClientePlano(clienteId: String) {
        _estado.value = _estado.value.copy(clientePlanoFiltroId = clienteId)
    }

    fun salvarProduto(produto: Produto, ehEdicao: Boolean) {
        val msg = if (ehEdicao) "✅ Suplemento atualizado no Firestore!" else "✅ Novo suplemento cadastrado!"
        executarOperacaoFirestore(msg) { dataService.salvarProduto(produto, ehEdicao) }
    }

    fun excluirProduto(produtoId: String) {
        executarOperacaoFirestore("🗑️ Suplemento removido do catálogo.") { dataService.removerProduto(produtoId) }
    }

    fun salvarCliente(cliente: Cliente, ehEdicao: Boolean) {
        val msg = if (ehEdicao) "✅ Ficha do cliente atualizada!" else "✅ Novo cliente cadastrado no CRM!"
        executarOperacaoFirestore(msg) { dataService.salvarCliente(cliente, ehEdicao) }
    }

    fun excluirCliente(clienteId: String) {
        executarOperacaoFirestore("🗑️ Cliente removido da base.") { dataService.removerCliente(clienteId) }
    }

    fun registrarAtendimento(clienteId: String, interacao: InteracaoAtendimento) {
        val cliente = _estado.value.clientes.find { it.id == clienteId } ?: return
        executarOperacaoFirestore("📋 Registro de atendimento salvo no prontuário!") {
            dataService.registrarInteracaoCliente(cliente, interacao)
        }
    }

    fun criarPedido(pedido: Pedido) {
        executarOperacaoFirestore("🛒 Venda ${pedido.codigoPedido} registrada e estoque atualizado!") {
            dataService.registrarNovoPedido(pedido, _estado.value.produtos)
        }
    }

    fun mudarStatusPedido(pedidoId: String, novoStatus: String) {
        executarOperacaoFirestore("📦 Status do pedido alterado para $novoStatus!") {
            dataService.atualizarStatusPedido(pedidoId, novoStatus)
        }
    }

    fun excluirPedido(pedidoId: String) {
        executarOperacaoFirestore("🗑️ Pedido excluído do histórico.") { dataService.removerPedido(pedidoId) }
    }

    fun salvarPlanoAlimentar(plano: PlanoAlimentar, ehEdicao: Boolean) {
        val msg = if (ehEdicao) "🥗 Plano alimentar atualizado!" else "🥗 Novo plano alimentar vinculado ao cliente!"
        executarOperacaoFirestore(msg) { dataService.salvarPlano(plano, ehEdicao) }
    }

    fun excluirPlanoAlimentar(planoId: String) {
        executarOperacaoFirestore("🗑️ Plano alimentar removido.") { dataService.removerPlano(planoId) }
    }

    fun restaurarDadosDemonstracao() {
        executarOperacaoFirestore("🔄 Base de demonstração restaurada no seu Tenant!") {
            dataService.restaurarDadosDemonstracao()
        }
    }

    fun limparFeedback() {
        _estado.value = _estado.value.copy(mensagemFeedback = null)
    }

    private fun <T> executarOperacaoFirestore(mensagemSucesso: String, bloco: suspend () -> Result<T>) {
        viewModelScope.launch {
            _estado.value = _estado.value.copy(carregando = true)
            val resultado = bloco()
            val textoFeedback = resultado.fold(
                onSuccess = { mensagemSucesso },
                onFailure = { "⚠️ Erro na operação: ${it.localizedMessage ?: "Falha de permissão RLS"}" }
            )
            _estado.value = _estado.value.copy(mensagemFeedback = textoFeedback, carregando = false)
            delay(3600)
            if (_estado.value.mensagemFeedback == textoFeedback) {
                _estado.value = _estado.value.copy(mensagemFeedback = null)
            }
        }
    }
}
