package com.example.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import com.example.types.Cliente
import com.example.utils.Formatadores

@Composable
fun ModalFormularioCliente(
    clienteExistente: Cliente?,
    aoSalvar: (Cliente) -> Unit,
    aoCancelar: () -> Unit
) {
    var nomeCompleto by remember { mutableStateOf(clienteExistente?.nomeCompleto.orEmpty()) }
    var telefone by remember { mutableStateOf(clienteExistente?.telefone.orEmpty()) }
    var email by remember { mutableStateOf(clienteExistente?.email.orEmpty()) }
    var cidade by remember { mutableStateOf(clienteExistente?.cidadeLocalizacao.orEmpty()) }
    var objetivosTexto by remember {
        mutableStateOf(clienteExistente?.objetivos?.joinToString(", ").orEmpty())
    }
    var preferenciasTexto by remember {
        mutableStateOf(clienteExistente?.preferenciasAlimentares?.joinToString(", ").orEmpty())
    }
    var restricoesTexto by remember {
        mutableStateOf(clienteExistente?.restricoes?.joinToString(", ").orEmpty())
    }
    var alergiasTexto by remember {
        mutableStateOf(clienteExistente?.alergias?.joinToString(", ").orEmpty())
    }
    var observacoes by remember { mutableStateOf(clienteExistente?.observacoes.orEmpty()) }
    var erroValidacao by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = aoCancelar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
                .testTag("modal_formulario_cliente"),
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (clienteExistente == null) "Cadastrar Novo Cliente" else "Editar Ficha do Cliente",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = nomeCompleto,
                    onValueChange = { nomeCompleto = it; erroValidacao = null },
                    label = { Text("Nome Completo *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_cliente_nome"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = telefone,
                        onValueChange = { telefone = it; erroValidacao = null },
                        label = { Text("Telefone / WhatsApp *") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_cliente_telefone"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = cidade,
                        onValueChange = { cidade = it; erroValidacao = null },
                        label = { Text("Cidade / UF *") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_cliente_cidade"),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; erroValidacao = null },
                    label = { Text("E-mail *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_cliente_email"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = objetivosTexto,
                    onValueChange = { objetivosTexto = it },
                    label = { Text("Objetivos (ex: Hipertrofia, Energia, Reposição B12)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = preferenciasTexto,
                    onValueChange = { preferenciasTexto = it },
                    label = { Text("Preferências Alimentares (ex: Vegano estrito, Sem soja)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = restricoesTexto,
                        onValueChange = { restricoesTexto = it },
                        label = { Text("Restrições") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = alergiasTexto,
                        onValueChange = { alergiasTexto = it },
                        label = { Text("Alergias") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = observacoes,
                    onValueChange = { observacoes = it },
                    label = { Text("Observações Clínicas e Rotina") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                if (erroValidacao != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = erroValidacao.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.testTag("erro_formulario_cliente")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = aoCancelar,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            when {
                                nomeCompleto.trim().length < 3 ->
                                    erroValidacao = "⚠️ Informe o nome completo do cliente."
                                !Formatadores.validarTelefone(telefone) ->
                                    erroValidacao = "⚠️ Informe um telefone válido com DDD (ex: 11 99999-0000)."
                                !Formatadores.validarEmail(email) ->
                                    erroValidacao = "⚠️ Informe um endereço de e-mail válido."
                                cidade.isBlank() ->
                                    erroValidacao = "⚠️ Informe a cidade/UF do cliente."
                                else -> {
                                    val cliente = Cliente(
                                        id = clienteExistente?.id ?: Formatadores.gerarIdUnico(),
                                        nomeCompleto = nomeCompleto.trim(),
                                        telefone = telefone.trim(),
                                        email = email.trim(),
                                        cidadeLocalizacao = cidade.trim(),
                                        preferenciasAlimentares = Formatadores.dividirListaTexto(preferenciasTexto)
                                            .ifEmpty { listOf("Alimentação Plant-Based") },
                                        objetivos = Formatadores.dividirListaTexto(objetivosTexto)
                                            .ifEmpty { listOf("Saúde integrativa e longevidade") },
                                        restricoes = Formatadores.dividirListaTexto(restricoesTexto),
                                        alergias = Formatadores.dividirListaTexto(alergiasTexto),
                                        observacoes = observacoes.ifBlank { "Acompanhamento nutricional ativo." }.trim(),
                                        dataCadastro = clienteExistente?.dataCadastro ?: Formatadores.dataAtual2026(),
                                        historicoInteracoes = clienteExistente?.historicoInteracoes ?: emptyList()
                                    )
                                    aoSalvar(cliente)
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("botao_salvar_cliente")
                    ) {
                        Text("Salvar Cliente")
                    }
                }
            }
        }
    }
}
