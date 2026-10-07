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
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.types.Pedido
import com.example.types.StatusPedido
import com.example.utils.Formatadores

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TelaPedidosVendas(
    estado: EstadoVeganFlow,
    aoAtualizarFiltroStatus: (String) -> Unit,
    aoCriarPedido: (Pedido) -> Unit,
    aoMudarStatusPedido: (String, String) -> Unit,
    aoExcluirPedido: (String) -> Unit,
    aoVoltarPainel: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { aoVoltarPainel() }

    var exibirModalNovaVenda by remember { mutableStateOf(false) }
    var pedidoParaMudarStatus by remember { mutableStateOf<Pair<Pedido, String>?>(null) }
    var pedidoParaExcluir by remember { mutableStateOf<Pedido?>(null) }

    val pedidosFiltrados = estado.pedidos.filter { ped ->
        estado.statusPedidoFiltro == "Todos" || ped.status == estado.statusPedidoFiltro
    }
    val somaFiltrada = pedidosFiltrados.sumOf { it.total }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("tela_pedidos_vendas"),
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
                        text = "Vendas & Pedidos",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${pedidosFiltrados.size} pedidos • Volume: ${Formatadores.formatarMoeda(somaFiltrada)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = { exibirModalNovaVenda = true },
                    modifier = Modifier.testTag("botao_abrir_pdv_venda")
                ) {
                    Icon(Icons.Filled.AddShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Nova Venda")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val opcoes = listOf("Todos") + StatusPedido.entries.map { it.rotulo }
                opcoes.forEach { st ->
                    FilterChip(
                        selected = estado.statusPedidoFiltro == st,
                        onClick = { aoAtualizarFiltroStatus(st) },
                        label = { Text(st, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
        }

        if (pedidosFiltrados.isEmpty()) {
            item {
                EstadoVazio(
                    emoji = "🛒",
                    titulo = "Nenhum pedido neste status",
                    descricao = "Registre uma nova venda no PDV ou altere o filtro de status logístico acima.",
                    rotuloBotao = "Registrar Nova Venda",
                    aoClicarBotao = { exibirModalNovaVenda = true }
                )
            }
        } else {
            items(pedidosFiltrados, key = { it.id }) { ped ->
                CardPedidoItem(
                    pedido = ped,
                    aoAlterarStatus = { pedidoAlvo, novoStatus ->
                        pedidoParaMudarStatus = pedidoAlvo to novoStatus
                    },
                    aoSolicitarExclusao = { pedidoParaExcluir = it }
                )
            }
        }
    }

    if (exibirModalNovaVenda) {
        ModalFormularioPedido(
            clientes = estado.clientes,
            produtos = estado.produtos,
            quantidadePedidosExistentes = estado.pedidos.size,
            aoConfirmarPedido = { novo ->
                exibirModalNovaVenda = false
                aoCriarPedido(novo)
            },
            aoCancelar = { exibirModalNovaVenda = false }
        )
    }

    pedidoParaMudarStatus?.let { (pedido, novoStatus) ->
        ModalConfirmacao(
            titulo = "Confirmar Mudança de Status",
            mensagem = "Deseja atualizar o status do pedido ${pedido.codigoPedido} (${pedido.nomeCliente}) para \"$novoStatus\"?",
            textoConfirmar = "Atualizar Status",
            ehDestrutivo = false,
            aoConfirmar = {
                aoMudarStatusPedido(pedido.id, novoStatus)
                pedidoParaMudarStatus = null
            },
            aoCancelar = { pedidoParaMudarStatus = null }
        )
    }

    pedidoParaExcluir?.let { ped ->
        ModalConfirmacao(
            titulo = "Excluir Registro de Pedido",
            mensagem = "Confirma a exclusão do pedido ${ped.codigoPedido} de ${ped.nomeCliente} no valor de ${Formatadores.formatarMoeda(ped.total)}?",
            textoConfirmar = "Excluir Pedido",
            ehDestrutivo = true,
            aoConfirmar = {
                aoExcluirPedido(ped.id)
                pedidoParaExcluir = null
            },
            aoCancelar = { pedidoParaExcluir = null }
        )
    }
}
