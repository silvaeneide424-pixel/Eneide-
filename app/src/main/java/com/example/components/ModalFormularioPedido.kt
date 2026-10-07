package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.types.Cliente
import com.example.types.ItemPedido
import com.example.types.MetodoPagamento
import com.example.types.Pedido
import com.example.types.Produto
import com.example.utils.ConstrutoresEntidades
import com.example.utils.Formatadores

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModalFormularioPedido(
    clientes: List<Cliente>,
    produtos: List<Produto>,
    quantidadePedidosExistentes: Int,
    aoConfirmarPedido: (Pedido) -> Unit,
    aoCancelar: () -> Unit
) {
    var clienteSelecionado by remember { mutableStateOf(clientes.firstOrNull()) }
    val quantidadesPorProduto = remember { mutableStateMapOf<String, Int>() }
    var descontoTexto by remember { mutableStateOf("0") }
    var metodoPagamento by remember { mutableStateOf(MetodoPagamento.PIX.rotulo) }
    var observacoes by remember { mutableStateOf("") }
    var erroValidacao by remember { mutableStateOf<String?>(null) }

    val itensMontados = produtos.mapNotNull { prod ->
        val qtd = quantidadesPorProduto[prod.id] ?: 0
        if (qtd > 0) ItemPedido(prod.id, prod.nome, qtd, prod.preco, prod.preco * qtd) else null
    }
    val subtotal = Formatadores.calcularSubtotalItens(itensMontados)
    val desconto = descontoTexto.replace(",", ".").toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val totalFinal = Formatadores.calcularTotalPedido(subtotal, desconto)

    Dialog(
        onDismissRequest = aoCancelar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
                .testTag("modal_formulario_pedido"),
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Registrar Nova Venda (PDV)",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Selecione o Cliente *", style = MaterialTheme.typography.titleSmall)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    clientes.forEach { cli ->
                        FilterChip(
                            selected = clienteSelecionado?.id == cli.id,
                            onClick = { clienteSelecionado = cli; erroValidacao = null },
                            label = { Text(cli.nomeCompleto, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("2. Selecione os Suplementos *", style = MaterialTheme.typography.titleSmall)
                produtos.forEach { prod ->
                    val qtdAtual = quantidadesPorProduto[prod.id] ?: 0
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(prod.nome, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                            Text(
                                text = "${Formatadores.formatarMoeda(prod.preco)} • Est: ${prod.estoqueSimulado}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (qtdAtual > 0) quantidadesPorProduto[prod.id] = qtdAtual - 1 }) {
                                Icon(Icons.Filled.RemoveCircleOutline, contentDescription = "Diminuir")
                            }
                            Text(qtdAtual.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            IconButton(
                                onClick = {
                                    if (qtdAtual < prod.estoqueSimulado) {
                                        quantidadesPorProduto[prod.id] = qtdAtual + 1
                                        erroValidacao = null
                                    }
                                },
                                modifier = Modifier.testTag("botao_add_item_pedido_${prod.id}")
                            ) {
                                Icon(Icons.Filled.AddCircleOutline, contentDescription = "Adicionar")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("3. Método de Pagamento", style = MaterialTheme.typography.titleSmall)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    MetodoPagamento.entries.forEach { met ->
                        FilterChip(
                            selected = metodoPagamento == met.rotulo,
                            onClick = { metodoPagamento = met.rotulo },
                            label = { Text(met.rotulo, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = descontoTexto,
                        onValueChange = { descontoTexto = it },
                        label = { Text("Desconto (R$)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = observacoes,
                        onValueChange = { observacoes = it },
                        label = { Text("Observações") },
                        modifier = Modifier.weight(1.5f),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Subtotal: ${Formatadores.formatarMoeda(subtotal)} | Total: ${Formatadores.formatarMoeda(totalFinal)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                if (erroValidacao != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(erroValidacao.orEmpty(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelLarge)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = aoCancelar, modifier = Modifier.weight(1f)) { Text("Cancelar") }
                    Button(
                        onClick = {
                            val cli = clienteSelecionado
                            when {
                                cli == null -> erroValidacao = "⚠️ Selecione um cliente para o pedido."
                                itensMontados.isEmpty() -> erroValidacao = "⚠️ Adicione pelo menos 1 suplemento ao pedido."
                                else -> aoConfirmarPedido(
                                    ConstrutoresEntidades.construirPedido(
                                        quantidadePedidosExistentes + 1, cli, itensMontados,
                                        subtotal, desconto, totalFinal, metodoPagamento, observacoes
                                    )
                                )
                            }
                        },
                        modifier = Modifier.weight(1f).testTag("botao_confirmar_novo_pedido")
                    ) { Text("Concluir Venda") }
                }
            }
        }
    }
}
