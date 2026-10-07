package com.example.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.types.Pedido
import com.example.types.StatusPedido
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldVibrant
import com.example.ui.theme.ErrorRed
import com.example.utils.Formatadores

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardPedidoItem(
    pedido: Pedido,
    aoAlterarStatus: (Pedido, String) -> Unit,
    aoSolicitarExclusao: (Pedido) -> Unit,
    modifier: Modifier = Modifier
) {
    val corStatus = when (pedido.status) {
        StatusPedido.ENTREGUE.rotulo, StatusPedido.PAGO.rotulo -> EmeraldVibrant
        StatusPedido.CANCELADO.rotulo -> ErrorRed
        else -> AmberGold
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_pedido_${pedido.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${pedido.codigoPedido} • ${pedido.dataCriacao}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = pedido.nomeCliente,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = corStatus.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = pedido.status,
                            style = MaterialTheme.typography.labelSmall,
                            color = corStatus,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    IconButton(
                        onClick = { aoSolicitarExclusao(pedido) },
                        modifier = Modifier.testTag("botao_excluir_pedido_${pedido.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = "Excluir pedido",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            pedido.itens.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.quantidade}x ${item.nomeProduto}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = Formatadores.formatarMoeda(item.subtotalItem),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pagamento: ${pedido.metodoPagamento}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (pedido.desconto > 0) {
                        Text(
                            text = "Desconto aplicado: -${Formatadores.formatarMoeda(pedido.desconto)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldVibrant
                        )
                    }
                }
                Text(
                    text = "Total: ${Formatadores.formatarMoeda(pedido.total)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            if (pedido.observacoes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Obs: ${pedido.observacoes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Atualizar etapa logística:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                StatusPedido.entries.forEach { st ->
                    FilterChip(
                        selected = pedido.status == st.rotulo,
                        onClick = {
                            if (pedido.status != st.rotulo) {
                                aoAlterarStatus(pedido, st.rotulo)
                            }
                        },
                        label = { Text(st.rotulo, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
        }
    }
}
