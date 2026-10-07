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
import androidx.compose.material.icons.filled.PostAdd
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
import com.example.types.PlanoAlimentar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TelaPlanosAlimentares(
    estado: EstadoVeganFlow,
    aoAtualizarFiltroCliente: (String) -> Unit,
    aoSalvarPlano: (PlanoAlimentar, Boolean) -> Unit,
    aoExcluirPlano: (String) -> Unit,
    aoVoltarPainel: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { aoVoltarPainel() }

    var planoEmDetalhe by remember { mutableStateOf<PlanoAlimentar?>(null) }
    var exibirFormulario by remember { mutableStateOf(false) }
    var planoParaEditar by remember { mutableStateOf<PlanoAlimentar?>(null) }
    var planoPendenteConfirmacaoEdicao by remember { mutableStateOf<PlanoAlimentar?>(null) }
    var planoParaExcluir by remember { mutableStateOf<PlanoAlimentar?>(null) }

    val planosFiltrados = estado.planos.filter { pl ->
        estado.clientePlanoFiltroId == "Todos" || pl.clienteId == estado.clientePlanoFiltroId
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("tela_planos_alimentares"),
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
                        text = "Planos Alimentares",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${planosFiltrados.size} prescrições dietéticas com suplementos veganos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = {
                        planoParaEditar = null
                        exibirFormulario = true
                    },
                    modifier = Modifier.testTag("botao_novo_plano_alimentar")
                ) {
                    Icon(Icons.Filled.PostAdd, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Criar Plano")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text("Filtrar por paciente:", style = MaterialTheme.typography.labelMedium)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(
                    selected = estado.clientePlanoFiltroId == "Todos",
                    onClick = { aoAtualizarFiltroCliente("Todos") },
                    label = { Text("Todos os Pacientes", style = MaterialTheme.typography.labelSmall) }
                )
                estado.clientes.forEach { cli ->
                    FilterChip(
                        selected = estado.clientePlanoFiltroId == cli.id,
                        onClick = { aoAtualizarFiltroCliente(cli.id) },
                        label = { Text(cli.nomeCompleto.substringBefore(" "), style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
        }

        if (planosFiltrados.isEmpty()) {
            item {
                EstadoVazio(
                    emoji = "🥗",
                    titulo = "Nenhum plano alimentar para este filtro",
                    descricao = "Monte um plano alimentar personalizado associado ao cliente com cálculo de macronutrientes e suplementos veganos.",
                    rotuloBotao = "Montar Novo Plano",
                    aoClicarBotao = {
                        planoParaEditar = null
                        exibirFormulario = true
                    }
                )
            }
        } else {
            items(planosFiltrados, key = { it.id }) { plano ->
                CardPlanoAlimentar(
                    plano = plano,
                    aoVerPlanoCompleto = { planoEmDetalhe = it },
                    aoEditar = {
                        planoParaEditar = it
                        exibirFormulario = true
                    },
                    aoSolicitarExclusao = { planoParaExcluir = it }
                )
            }
        }
    }

    planoEmDetalhe?.let { pl ->
        ModalDetalhePlano(
            plano = pl,
            aoFechar = { planoEmDetalhe = null }
        )
    }

    if (exibirFormulario) {
        ModalFormularioPlano(
            planoExistente = planoParaEditar,
            clientes = estado.clientes,
            produtos = estado.produtos,
            aoSalvar = { planoSalvo ->
                if (planoParaEditar != null) {
                    exibirFormulario = false
                    planoPendenteConfirmacaoEdicao = planoSalvo
                } else {
                    exibirFormulario = false
                    aoSalvarPlano(planoSalvo, false)
                }
            },
            aoCancelar = { exibirFormulario = false }
        )
    }

    planoPendenteConfirmacaoEdicao?.let { plEditado ->
        ModalConfirmacao(
            titulo = "Confirmar Edição do Plano",
            mensagem = "Deseja salvar as alterações na prescrição alimentar \"${plEditado.titulo}\" de ${plEditado.nomeCliente}?",
            textoConfirmar = "Salvar Prescrição",
            ehDestrutivo = false,
            aoConfirmar = {
                aoSalvarPlano(plEditado, true)
                planoPendenteConfirmacaoEdicao = null
            },
            aoCancelar = { planoPendenteConfirmacaoEdicao = null }
        )
    }

    planoParaExcluir?.let { pl ->
        ModalConfirmacao(
            titulo = "Excluir Plano Alimentar",
            mensagem = "Tem certeza que deseja excluir o plano \"${pl.titulo}\" vinculado a ${pl.nomeCliente}?",
            textoConfirmar = "Excluir Plano",
            ehDestrutivo = true,
            aoConfirmar = {
                aoExcluirPlano(pl.id)
                planoParaExcluir = null
            },
            aoCancelar = { planoParaExcluir = null }
        )
    }
}
