package com.example.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hooks.EstadoVeganFlow
import com.example.types.CategoriaProduto
import com.example.types.Produto

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TelaCatalogoProdutos(
    estado: EstadoVeganFlow,
    aoAtualizarBusca: (String) -> Unit,
    aoAtualizarCategoria: (String) -> Unit,
    aoSalvarProduto: (Produto, Boolean) -> Unit,
    aoExcluirProduto: (String) -> Unit,
    aoVoltarPainel: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { aoVoltarPainel() }

    var produtoEmDetalhe by remember { mutableStateOf<Produto?>(null) }
    var exibirFormulario by remember { mutableStateOf(false) }
    var produtoParaEditar by remember { mutableStateOf<Produto?>(null) }
    var produtoPendenteConfirmacaoEdicao by remember { mutableStateOf<Produto?>(null) }
    var produtoParaExcluir by remember { mutableStateOf<Produto?>(null) }

    val produtosFiltrados = estado.produtos.filter { prod ->
        val bateCategoria = estado.categoriaFiltro == "Todas" || prod.categoria == estado.categoriaFiltro
        val termo = estado.buscaProdutos.trim().lowercase()
        val bateBusca = termo.isEmpty() ||
            prod.nome.lowercase().contains(termo) ||
            prod.descricaoCurta.lowercase().contains(termo) ||
            prod.ingredientes.any { it.lowercase().contains(termo) }
        bateCategoria && bateBusca
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("tela_catalogo_produtos"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Catálogo de Suplementos",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${produtosFiltrados.size} fórmulas veganas com ficha nutricional",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = {
                        produtoParaEditar = null
                        exibirFormulario = true
                    },
                    modifier = Modifier.testTag("botao_novo_produto")
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Novo")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = estado.buscaProdutos,
                onValueChange = aoAtualizarBusca,
                label = { Text("Buscar por nome, ativo ou ingrediente...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_busca_produtos"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val categorias = listOf("Todas") + CategoriaProduto.entries.map { it.rotulo }
                categorias.forEach { cat ->
                    FilterChip(
                        selected = estado.categoriaFiltro == cat,
                        onClick = { aoAtualizarCategoria(cat) },
                        label = { Text(cat, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
        }

        if (produtosFiltrados.isEmpty()) {
            item {
                EstadoVazio(
                    emoji = "🌱",
                    titulo = "Nenhum suplemento encontrado",
                    descricao = "Não há produtos correspondentes ao filtro atual. Limpe a busca ou cadastre uma nova fórmula vegana.",
                    rotuloBotao = "Limpar Filtros",
                    aoClicarBotao = {
                        aoAtualizarBusca("")
                        aoAtualizarCategoria("Todas")
                    }
                )
            }
        } else {
            items(produtosFiltrados, key = { it.id }) { prod ->
                CardProdutoItem(
                    produto = prod,
                    aoVerDetalhes = { produtoEmDetalhe = it },
                    aoEditar = {
                        produtoParaEditar = it
                        exibirFormulario = true
                    },
                    aoSolicitarExclusao = { produtoParaExcluir = it }
                )
            }
        }
    }

    produtoEmDetalhe?.let { prod ->
        ModalDetalheProduto(
            produto = prod,
            aoFechar = { produtoEmDetalhe = null }
        )
    }

    if (exibirFormulario) {
        ModalFormularioProduto(
            produtoExistente = produtoParaEditar,
            aoSalvar = { novoOuEditado ->
                if (produtoParaEditar != null) {
                    exibirFormulario = false
                    produtoPendenteConfirmacaoEdicao = novoOuEditado
                } else {
                    exibirFormulario = false
                    aoSalvarProduto(novoOuEditado, false)
                }
            },
            aoCancelar = { exibirFormulario = false }
        )
    }

    produtoPendenteConfirmacaoEdicao?.let { prodEditado ->
        ModalConfirmacao(
            titulo = "Confirmar Alterações no Suplemento",
            mensagem = "Deseja salvar as alterações realizadas na ficha do produto \"${prodEditado.nome}\"?",
            textoConfirmar = "Salvar Alterações",
            ehDestrutivo = false,
            aoConfirmar = {
                aoSalvarProduto(prodEditado, true)
                produtoPendenteConfirmacaoEdicao = null
            },
            aoCancelar = { produtoPendenteConfirmacaoEdicao = null }
        )
    }

    produtoParaExcluir?.let { prod ->
        ModalConfirmacao(
            titulo = "Excluir Suplemento",
            mensagem = "Tem certeza que deseja remover \"${prod.nome}\" do catálogo Vegan Flow? Esta ação pode ser desfeita apenas restaurando a base demo.",
            textoConfirmar = "Excluir",
            ehDestrutivo = true,
            aoConfirmar = {
                aoExcluirProduto(prod.id)
                produtoParaExcluir = null
            },
            aoCancelar = { produtoParaExcluir = null }
        )
    }
}
