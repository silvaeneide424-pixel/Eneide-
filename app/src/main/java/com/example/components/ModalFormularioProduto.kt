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
import com.example.types.CategoriaProduto
import com.example.types.Produto
import com.example.utils.ConstrutoresEntidades

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModalFormularioProduto(
    produtoExistente: Produto?,
    aoSalvar: (Produto) -> Unit,
    aoCancelar: () -> Unit
) {
    var nome by remember { mutableStateOf(produtoExistente?.nome.orEmpty()) }
    var categoria by remember { mutableStateOf(produtoExistente?.categoria ?: CategoriaProduto.PROTEINAS.rotulo) }
    var descricaoCurta by remember { mutableStateOf(produtoExistente?.descricaoCurta.orEmpty()) }
    var descricaoCompleta by remember { mutableStateOf(produtoExistente?.descricaoCompleta.orEmpty()) }
    var precoTexto by remember { mutableStateOf(produtoExistente?.preco?.toString().orEmpty()) }
    var estoqueTexto by remember { mutableStateOf(produtoExistente?.estoqueSimulado?.toString() ?: "25") }
    var unidade by remember { mutableStateOf(produtoExistente?.unidade ?: "Pote 600g") }
    var ingredientesTexto by remember { mutableStateOf(produtoExistente?.ingredientes?.joinToString(", ").orEmpty()) }
    var beneficiosTexto by remember { mutableStateOf(produtoExistente?.beneficios?.joinToString(", ").orEmpty()) }
    var modoUso by remember { mutableStateOf(produtoExistente?.modoUso ?: "Diluir 1 scoop em 200ml de água.") }
    var erroValidacao by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = aoCancelar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
                .testTag("modal_formulario_produto"),
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (produtoExistente == null) "Novo Suplemento Vegano" else "Editar Suplemento",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CategoriaProduto.entries.forEach { cat ->
                        FilterChip(
                            selected = categoria == cat.rotulo,
                            onClick = { categoria = cat.rotulo },
                            label = { Text(cat.rotulo, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it; erroValidacao = null },
                    label = { Text("Nome do Suplemento *") },
                    modifier = Modifier.fillMaxWidth().testTag("input_produto_nome"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = precoTexto,
                        onValueChange = { precoTexto = it; erroValidacao = null },
                        label = { Text("Preço (R$) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f).testTag("input_produto_preco"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = estoqueTexto,
                        onValueChange = { estoqueTexto = it; erroValidacao = null },
                        label = { Text("Estoque *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("input_produto_estoque"),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = unidade,
                    onValueChange = { unidade = it },
                    label = { Text("Unidade (ex: Pote 900g)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = descricaoCurta,
                    onValueChange = { descricaoCurta = it; erroValidacao = null },
                    label = { Text("Descrição Curta Comercial *") },
                    modifier = Modifier.fillMaxWidth().testTag("input_produto_descricao_curta")
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = descricaoCompleta,
                    onValueChange = { descricaoCompleta = it },
                    label = { Text("Descrição Técnica Completa") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = ingredientesTexto,
                    onValueChange = { ingredientesTexto = it },
                    label = { Text("Ingredientes (separados por vírgula)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = beneficiosTexto,
                    onValueChange = { beneficiosTexto = it },
                    label = { Text("Benefícios (separados por vírgula)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = modoUso,
                    onValueChange = { modoUso = it },
                    label = { Text("Modo de Uso Recomendado") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (erroValidacao != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = erroValidacao.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.testTag("erro_formulario_produto")
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = aoCancelar, modifier = Modifier.weight(1f)) { Text("Cancelar") }
                    Button(
                        onClick = {
                            val preco = precoTexto.replace(",", ".").toDoubleOrNull()
                            val estoque = estoqueTexto.toIntOrNull()
                            when {
                                nome.isBlank() || descricaoCurta.isBlank() ->
                                    erroValidacao = "⚠️ Informe o nome e a descrição curta do suplemento."
                                preco == null || preco <= 0.0 ->
                                    erroValidacao = "⚠️ Informe um preço válido maior que R$ 0,00."
                                estoque == null || estoque < 0 ->
                                    erroValidacao = "⚠️ Informe uma quantidade de estoque válida."
                                else -> aoSalvar(
                                    ConstrutoresEntidades.construirProduto(
                                        produtoExistente, nome, categoria, descricaoCurta,
                                        descricaoCompleta, preco, estoque, unidade,
                                        ingredientesTexto, beneficiosTexto, modoUso
                                    )
                                )
                            }
                        },
                        modifier = Modifier.weight(1f).testTag("botao_salvar_produto")
                    ) { Text("Salvar") }
                }
            }
        }
    }
}
