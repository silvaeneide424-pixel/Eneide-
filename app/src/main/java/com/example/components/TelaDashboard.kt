package com.example.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hooks.AbaNavegacao
import com.example.hooks.EstadoVeganFlow
import com.example.types.StatusDisponibilidade
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldVibrant
import com.example.ui.theme.TerracottaAlert
import com.example.utils.Formatadores

@Composable
fun TelaDashboard(
    estado: EstadoVeganFlow,
    aoNavegarAba: (AbaNavegacao) -> Unit,
    aoRestaurarDemo: () -> Unit,
    aoAbrirPerfilTenant: () -> Unit,
    modifier: Modifier = Modifier
) {
    val produtosCriticos = estado.produtos.filter {
        it.statusDisponibilidade != StatusDisponibilidade.DISPONIVEL.rotulo
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("tela_dashboard_lista"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            CabecalhoHero(
                usuario = estado.usuarioSistema,
                aoClicarNovaVenda = { aoNavegarAba(AbaNavegacao.PEDIDOS) },
                aoClicarNovoPlano = { aoNavegarAba(AbaNavegacao.PLANOS) },
                aoRestaurarDemo = aoRestaurarDemo,
                aoAbrirPerfilTenant = aoAbrirPerfilTenant
            )
        }

        item {
            Text(
                text = "Indicadores Operacionais do Tenant (Outubro/2026)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CardMetrica(
                    titulo = "Receita Acumulada",
                    valorPrincipal = Formatadores.formatarMoeda(estado.metricas.faturamentoTotal),
                    subtitulo = "Ticket médio ${Formatadores.formatarMoeda(estado.metricas.ticketMedio)}",
                    icone = Icons.Filled.AttachMoney,
                    corDestaque = EmeraldVibrant,
                    tagTeste = "kpi_receita_card",
                    aoClicar = { aoNavegarAba(AbaNavegacao.PEDIDOS) },
                    modifier = Modifier.weight(1f)
                )
                CardMetrica(
                    titulo = "Pedidos & Vendas",
                    valorPrincipal = "${estado.metricas.totalPedidos} pedidos",
                    subtitulo = "${estado.metricas.pedidosAtivos} em fluxo ativo",
                    icone = Icons.Filled.LocalShipping,
                    corDestaque = AmberGold,
                    tagTeste = "kpi_pedidos_card",
                    aoClicar = { aoNavegarAba(AbaNavegacao.PEDIDOS) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CardMetrica(
                    titulo = "Clientes Ativos",
                    valorPrincipal = "${estado.metricas.totalClientes} pacientes",
                    subtitulo = "CRM & Prontuário em dia",
                    icone = Icons.Filled.Groups,
                    corDestaque = EmeraldPrimary,
                    tagTeste = "kpi_clientes_card",
                    aoClicar = { aoNavegarAba(AbaNavegacao.CLIENTES) },
                    modifier = Modifier.weight(1f)
                )
                CardMetrica(
                    titulo = "Planos Alimentares",
                    valorPrincipal = "${estado.metricas.planosAtivos} ativos",
                    subtitulo = "${estado.metricas.produtosEstoqueBaixo} alertas de estoque",
                    icone = Icons.Filled.Restaurant,
                    corDestaque = if (estado.metricas.produtosEstoqueBaixo > 0) TerracottaAlert else EmeraldVibrant,
                    tagTeste = "kpi_planos_card",
                    aoClicar = { aoNavegarAba(AbaNavegacao.PLANOS) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (produtosCriticos.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = TerracottaAlert.copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, TerracottaAlert.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.WarningAmber, contentDescription = "Alerta de estoque", tint = TerracottaAlert)
                            Text(
                                text = " Reposição Recomendada (${produtosCriticos.size} suplementos)",
                                style = MaterialTheme.typography.titleSmall,
                                color = TerracottaAlert,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        produtosCriticos.forEach { prod ->
                            Text(
                                text = "• ${prod.nome}: apenas ${prod.estoqueSimulado} un. restantes (${prod.statusDisponibilidade})",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Últimos Pedidos Registrados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { aoNavegarAba(AbaNavegacao.PEDIDOS) }) {
                    Text("Ver todos")
                }
            }
        }

        items(estado.pedidos.take(3), key = { it.id }) { pedido ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${pedido.codigoPedido} • ${pedido.nomeCliente}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${pedido.itens.size} itens • ${pedido.metodoPagamento} • ${pedido.status}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = Formatadores.formatarMoeda(pedido.total),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
