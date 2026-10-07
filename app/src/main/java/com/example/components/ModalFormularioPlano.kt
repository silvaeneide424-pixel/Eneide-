package com.example.components

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
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.types.Cliente
import com.example.types.PlanoAlimentar
import com.example.types.Produto
import com.example.utils.ConstrutoresEntidades

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModalFormularioPlano(
    planoExistente: PlanoAlimentar?,
    clientes: List<Cliente>,
    produtos: List<Produto>,
    aoSalvar: (PlanoAlimentar) -> Unit,
    aoCancelar: () -> Unit
) {
    var clienteSelecionado by remember {
        mutableStateOf(clientes.find { it.id == planoExistente?.clienteId } ?: clientes.firstOrNull())
    }
    var titulo by remember { mutableStateOf(planoExistente?.titulo.orEmpty()) }
    var objetivo by remember { mutableStateOf(planoExistente?.objetivoPrincipal.orEmpty()) }
    var caloriasTexto by remember { mutableStateOf(planoExistente?.caloriasDiarias?.toString() ?: "2100") }
    var proteinasTexto by remember { mutableStateOf(planoExistente?.proteinasGramas?.toString() ?: "130") }
    var carboTexto by remember { mutableStateOf(planoExistente?.carboidratosGramas?.toString() ?: "240") }
    var gordurasTexto by remember { mutableStateOf(planoExistente?.gordurasGramas?.toString() ?: "60") }
    var orientacoes by remember {
        mutableStateOf(planoExistente?.orientacoesGerais ?: "Hidratação mínima de 35ml/kg/dia e remolho de leguminosas.")
    }
    var refeicao1Alimento by remember {
        mutableStateOf(
            planoExistente?.refeicoes?.firstOrNull()?.itens?.firstOrNull()?.alimento
                ?: "Shake Vegan Pro Blend + Aveia e Banana"
        )
    }
    var refeicao2Alimento by remember {
        mutableStateOf(
            planoExistente?.refeicoes?.getOrNull(1)?.itens?.firstOrNull()?.alimento
                ?: "Tofu grelhado 150g + Quinoa Real + Brócolis"
        )
    }
    val suplementosIds = remember {
        mutableStateListOf<String>().apply {
            val iniciais = planoExistente?.suplementosSugeridos?.map { it.produtoId }
                ?: listOfNotNull(produtos.firstOrNull()?.id)
            addAll(iniciais)
        }
    }
    var erroValidacao by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = aoCancelar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
                .testTag("modal_formulario_plano"),
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (planoExistente == null) "Criar Plano Alimentar" else "Editar Plano Alimentar",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Cliente Associado *:", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    clientes.forEach { cli ->
                        FilterChip(
                            selected = clienteSelecionado?.id == cli.id,
                            onClick = { clienteSelecionado = cli },
                            label = { Text(cli.nomeCompleto, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it; erroValidacao = null },
                    label = { Text("Título do Plano *") },
                    modifier = Modifier.fillMaxWidth().testTag("input_plano_titulo"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = objetivo,
                    onValueChange = { objetivo = it; erroValidacao = null },
                    label = { Text("Objetivo Clínico Principal *") },
                    modifier = Modifier.fillMaxWidth().testTag("input_plano_objetivo"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = caloriasTexto,
                        onValueChange = { caloriasTexto = it },
                        label = { Text("Kcal *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = proteinasTexto,
                        onValueChange = { proteinasTexto = it },
                        label = { Text("Prot (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = carboTexto,
                        onValueChange = { carboTexto = it },
                        label = { Text("Carb (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = gordurasTexto,
                        onValueChange = { gordurasTexto = it },
                        label = { Text("Lip (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = refeicao1Alimento,
                    onValueChange = { refeicao1Alimento = it },
                    label = { Text("Refeição 1 (08:00)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = refeicao2Alimento,
                    onValueChange = { refeicao2Alimento = it },
                    label = { Text("Refeição 2 (13:00)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text("Suplementos Veganos Sugeridos:", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    produtos.forEach { prod ->
                        val selecionado = suplementosIds.contains(prod.id)
                        FilterChip(
                            selected = selecionado,
                            onClick = { if (selecionado) suplementosIds.remove(prod.id) else suplementosIds.add(prod.id) },
                            label = { Text(prod.nome.take(26), style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = orientacoes,
                    onValueChange = { orientacoes = it },
                    label = { Text("Orientações Gerais de Conduta") },
                    modifier = Modifier.fillMaxWidth()
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
                                cli == null -> erroValidacao = "⚠️ Selecione um cliente."
                                titulo.isBlank() || objetivo.isBlank() ->
                                    erroValidacao = "⚠️ Preencha o título e o objetivo clínico do plano."
                                else -> aoSalvar(
                                    ConstrutoresEntidades.construirPlanoAlimentar(
                                        planoExistente, cli, titulo, objetivo,
                                        caloriasTexto.toIntOrNull() ?: 2000,
                                        proteinasTexto.toIntOrNull() ?: 120,
                                        carboTexto.toIntOrNull() ?: 220,
                                        gordurasTexto.toIntOrNull() ?: 60,
                                        refeicao1Alimento, refeicao2Alimento,
                                        produtos.filter { suplementosIds.contains(it.id) },
                                        orientacoes
                                    )
                                )
                            }
                        },
                        modifier = Modifier.weight(1f).testTag("botao_salvar_plano")
                    ) { Text("Salvar Plano") }
                }
            }
        }
    }
}
