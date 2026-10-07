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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.types.CanalAtendimento
import com.example.types.Cliente
import com.example.types.InteracaoAtendimento
import com.example.utils.Formatadores

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModalDetalheCliente(
    cliente: Cliente,
    nomeProfissional: String,
    aoRegistrarInteracao: (String, InteracaoAtendimento) -> Unit,
    aoFechar: () -> Unit
) {
    var canalSelecionado by remember { mutableStateOf(CanalAtendimento.CONSULTA_ONLINE.rotulo) }
    var resumoAtendimento by remember { mutableStateOf("") }
    var proximosPassos by remember { mutableStateOf("") }
    var erroInteracao by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = aoFechar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
                .testTag("modal_prontuario_cliente"),
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "PRONTUÁRIO & HISTÓRICO DE ATENDIMENTO",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = cliente.nomeCompleto,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${cliente.cidadeLocalizacao} • ${cliente.telefone} • ${cliente.email}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                BlocoInfoProntuario("Objetivos Clínicos", cliente.objetivos.joinToString(" • "))
                BlocoInfoProntuario("Preferências Alimentares", cliente.preferenciasAlimentares.joinToString(" • "))
                BlocoInfoProntuario("Restrições & Alergias", (cliente.restricoes + cliente.alergias).joinToString(" • "))
                BlocoInfoProntuario("Observações Nutricionais", cliente.observacoes)
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Registrar Nova Evolução / Atendimento",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            CanalAtendimento.entries.forEach { canal ->
                                FilterChip(
                                    selected = canalSelecionado == canal.rotulo,
                                    onClick = { canalSelecionado = canal.rotulo },
                                    label = { Text(canal.rotulo, style = MaterialTheme.typography.labelSmall) }
                                )
                            }
                        }
                        OutlinedTextField(
                            value = resumoAtendimento,
                            onValueChange = { resumoAtendimento = it; erroInteracao = null },
                            label = { Text("Resumo clínico do atendimento *") },
                            modifier = Modifier.fillMaxWidth().testTag("input_resumo_interacao")
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = proximosPassos,
                            onValueChange = { proximosPassos = it },
                            label = { Text("Próximos passos / Ajustes") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (erroInteracao != null) {
                            Text(erroInteracao.orEmpty(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (resumoAtendimento.isBlank()) {
                                    erroInteracao = "⚠️ Descreva o resumo do atendimento antes de salvar."
                                } else {
                                    aoRegistrarInteracao(
                                        cliente.id,
                                        InteracaoAtendimento(
                                            id = Formatadores.gerarIdUnico(),
                                            data = Formatadores.dataAtual2026(),
                                            canal = canalSelecionado,
                                            resumo = resumoAtendimento.trim(),
                                            responsavel = nomeProfissional,
                                            proximosPassos = proximosPassos.ifBlank { "Manter acompanhamento mensal." }.trim()
                                        )
                                    )
                                    resumoAtendimento = ""
                                    proximosPassos = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("botao_registrar_interacao")
                        ) { Text("Adicionar ao Histórico") }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Linha do Tempo (${cliente.historicoInteracoes.size})",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                cliente.historicoInteracoes.forEach { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${item.data} • ${item.canal}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            Text(item.responsavel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(item.resumo, style = MaterialTheme.typography.bodySmall)
                        Text("Conduta: ${item.proximosPassos}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = aoFechar,
                    modifier = Modifier.fillMaxWidth().testTag("botao_fechar_prontuario")
                ) { Text("Fechar Prontuário") }
            }
        }
    }
}

@Composable
private fun BlocoInfoProntuario(titulo: String, valor: String) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(titulo, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text(valor.ifBlank { "Não informado" }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
        HorizontalDivider(modifier = Modifier.padding(top = 3.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    }
}
