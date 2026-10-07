package com.example.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import com.example.types.Cliente
import com.example.types.InteracaoAtendimento

@Composable
fun TelaClientesCRM(
    estado: EstadoVeganFlow,
    aoAtualizarBusca: (String) -> Unit,
    aoSalvarCliente: (Cliente, Boolean) -> Unit,
    aoExcluirCliente: (String) -> Unit,
    aoRegistrarInteracao: (String, InteracaoAtendimento) -> Unit,
    aoVoltarPainel: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { aoVoltarPainel() }

    var clienteProntuarioId by remember { mutableStateOf<String?>(null) }
    var exibirFormulario by remember { mutableStateOf(false) }
    var clienteParaEditar by remember { mutableStateOf<Cliente?>(null) }
    var clientePendenteConfirmacaoEdicao by remember { mutableStateOf<Cliente?>(null) }
    var clienteParaExcluir by remember { mutableStateOf<Cliente?>(null) }

    val clientesFiltrados = estado.clientes.filter { cli ->
        val termo = estado.buscaClientes.trim().lowercase()
        termo.isEmpty() ||
            cli.nomeCompleto.lowercase().contains(termo) ||
            cli.cidadeLocalizacao.lowercase().contains(termo) ||
            cli.objetivos.any { it.lowercase().contains(termo) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("tela_clientes_crm"),
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
                        text = "CRM & Atendimento",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${clientesFiltrados.size} pacientes com histórico clínico e restrições",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = {
                        clienteParaEditar = null
                        exibirFormulario = true
                    },
                    modifier = Modifier.testTag("botao_novo_cliente")
                ) {
                    Icon(Icons.Filled.PersonAdd, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Cadastrar")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = estado.buscaClientes,
                onValueChange = aoAtualizarBusca,
                label = { Text("Buscar por nome, cidade ou objetivo clínico...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_busca_clientes"),
                singleLine = true
            )
        }

        if (clientesFiltrados.isEmpty()) {
            item {
                EstadoVazio(
                    emoji = "👥",
                    titulo = "Nenhum cliente encontrado",
                    descricao = "Cadastre um novo cliente ou limpe o filtro de pesquisa para visualizar os prontuários.",
                    rotuloBotao = "Novo Cliente",
                    aoClicarBotao = {
                        clienteParaEditar = null
                        exibirFormulario = true
                    }
                )
            }
        } else {
            items(clientesFiltrados, key = { it.id }) { cli ->
                val pedidosDoCliente = estado.pedidos.count { it.clienteId == cli.id }
                val temPlano = estado.planos.any { it.clienteId == cli.id && it.ativo }
                CardClienteItem(
                    cliente = cli,
                    totalPedidosCliente = pedidosDoCliente,
                    possuiPlanoAtivo = temPlano,
                    aoAbrirProntuario = { clienteProntuarioId = it.id },
                    aoEditar = {
                        clienteParaEditar = it
                        exibirFormulario = true
                    },
                    aoSolicitarExclusao = { clienteParaExcluir = it }
                )
            }
        }
    }

    val clienteProntuarioAtual = estado.clientes.find { it.id == clienteProntuarioId }
    if (clienteProntuarioAtual != null) {
        ModalDetalheCliente(
            cliente = clienteProntuarioAtual,
            nomeProfissional = "${estado.usuarioSistema.nome} (${estado.usuarioSistema.crnOuRegistro})",
            aoRegistrarInteracao = aoRegistrarInteracao,
            aoFechar = { clienteProntuarioId = null }
        )
    }

    if (exibirFormulario) {
        ModalFormularioCliente(
            clienteExistente = clienteParaEditar,
            aoSalvar = { clienteSalvo ->
                if (clienteParaEditar != null) {
                    exibirFormulario = false
                    clientePendenteConfirmacaoEdicao = clienteSalvo
                } else {
                    exibirFormulario = false
                    aoSalvarCliente(clienteSalvo, false)
                }
            },
            aoCancelar = { exibirFormulario = false }
        )
    }

    clientePendenteConfirmacaoEdicao?.let { cliEditado ->
        ModalConfirmacao(
            titulo = "Confirmar Edição do Cliente",
            mensagem = "Deseja salvar as alterações cadastrais e clínicas de \"${cliEditado.nomeCompleto}\"?",
            textoConfirmar = "Salvar Alterações",
            ehDestrutivo = false,
            aoConfirmar = {
                aoSalvarCliente(cliEditado, true)
                clientePendenteConfirmacaoEdicao = null
            },
            aoCancelar = { clientePendenteConfirmacaoEdicao = null }
        )
    }

    clienteParaExcluir?.let { cli ->
        ModalConfirmacao(
            titulo = "Remover Cliente do CRM",
            mensagem = "Deseja realmente excluir o cadastro e o histórico de atendimento de \"${cli.nomeCompleto}\"?",
            textoConfirmar = "Excluir Cliente",
            ehDestrutivo = true,
            aoConfirmar = {
                aoExcluirCliente(cli.id)
                clienteParaExcluir = null
            },
            aoCancelar = { clienteParaExcluir = null }
        )
    }
}
