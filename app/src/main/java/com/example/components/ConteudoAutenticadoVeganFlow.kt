package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hooks.AbaNavegacao
import com.example.hooks.VeganFlowViewModel

@Composable
fun ConteudoAutenticadoVeganFlow(
    viewModel: VeganFlowViewModel,
    configInicial: ConfigNovoUsuarioTenant?,
    aoConsumirConfigInicial: () -> Unit,
    aoEncerrarSessao: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    var confirmarRestauracaoDemo by remember { mutableStateOf(false) }
    var exibirModalPerfilTenant by remember { mutableStateOf(false) }
    var modoVisualizacaoSiteManual by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(configInicial) {
        val cfg = configInicial ?: ConfigNovoUsuarioTenant()
        viewModel.inicializarTenantSeNecessario(
            cargo = cfg.cargoProfissional,
            crn = cfg.crnOuRegistro,
            popularDemo = cfg.popularDadosIniciais
        )
        if (configInicial != null) {
            aoConsumirConfigInicial()
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val usarModoSite = modoVisualizacaoSiteManual ?: (maxWidth >= 680.dp)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (usarModoSite) "💻 Visualização: Modelo Site (Web)" else "📱 Visualização: App Celular",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = !usarModoSite,
                                onClick = { modoVisualizacaoSiteManual = false },
                                label = { Text("Celular", style = MaterialTheme.typography.labelSmall) },
                                leadingIcon = {
                                    Icon(Icons.Filled.PhoneAndroid, contentDescription = null, modifier = Modifier.size(14.dp))
                                },
                                modifier = Modifier.testTag("chip_modo_celular")
                            )
                            FilterChip(
                                selected = usarModoSite,
                                onClick = { modoVisualizacaoSiteManual = true },
                                label = { Text("Modelo Site", style = MaterialTheme.typography.labelSmall) },
                                leadingIcon = {
                                    Icon(Icons.Filled.Computer, contentDescription = null, modifier = Modifier.size(14.dp))
                                },
                                modifier = Modifier.testTag("chip_modo_site")
                            )
                        }
                    }
                }
            },
            bottomBar = {
                if (!usarModoSite) {
                    BarraNavegacaoInferior(
                        abaAtual = estado.abaAtual,
                        aoSelecionarAba = viewModel::selecionarAba
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (usarModoSite) {
                    BarraLateralWeb(
                        abaAtual = estado.abaAtual,
                        usuario = estado.usuarioSistema,
                        aoSelecionarAba = viewModel::selecionarAba,
                        aoAbrirPerfilTenant = { exibirModalPerfilTenant = true },
                        aoAlternarModoCelular = { modoVisualizacaoSiteManual = false }
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = if (usarModoSite) 1200.dp else 720.dp)
                    ) {
                        BannerFeedback(
                            mensagem = estado.mensagemFeedback,
                            carregando = estado.carregando,
                            aoFechar = viewModel::limparFeedback
                        )

                        when (estado.abaAtual) {
                            AbaNavegacao.PAINEL -> TelaDashboard(
                                estado = estado,
                                aoNavegarAba = viewModel::selecionarAba,
                                aoRestaurarDemo = { confirmarRestauracaoDemo = true },
                                aoAbrirPerfilTenant = { exibirModalPerfilTenant = true }
                            )

                            AbaNavegacao.PRODUTOS -> TelaCatalogoProdutos(
                                estado = estado,
                                aoAtualizarBusca = viewModel::atualizarBuscaProdutos,
                                aoAtualizarCategoria = viewModel::atualizarCategoriaFiltro,
                                aoSalvarProduto = viewModel::salvarProduto,
                                aoExcluirProduto = viewModel::excluirProduto,
                                aoVoltarPainel = { viewModel.selecionarAba(AbaNavegacao.PAINEL) }
                            )

                            AbaNavegacao.CLIENTES -> TelaClientesCRM(
                                estado = estado,
                                aoAtualizarBusca = viewModel::atualizarBuscaClientes,
                                aoSalvarCliente = viewModel::salvarCliente,
                                aoExcluirCliente = viewModel::excluirCliente,
                                aoRegistrarInteracao = viewModel::registrarAtendimento,
                                aoVoltarPainel = { viewModel.selecionarAba(AbaNavegacao.PAINEL) }
                            )

                            AbaNavegacao.PEDIDOS -> TelaPedidosVendas(
                                estado = estado,
                                aoAtualizarFiltroStatus = viewModel::atualizarFiltroStatusPedido,
                                aoCriarPedido = viewModel::criarPedido,
                                aoMudarStatusPedido = viewModel::mudarStatusPedido,
                                aoExcluirPedido = viewModel::excluirPedido,
                                aoVoltarPainel = { viewModel.selecionarAba(AbaNavegacao.PAINEL) }
                            )

                            AbaNavegacao.PLANOS -> TelaPlanosAlimentares(
                                estado = estado,
                                aoAtualizarFiltroCliente = viewModel::atualizarFiltroClientePlano,
                                aoSalvarPlano = viewModel::salvarPlanoAlimentar,
                                aoExcluirPlano = viewModel::excluirPlanoAlimentar,
                                aoVoltarPainel = { viewModel.selecionarAba(AbaNavegacao.PAINEL) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (exibirModalPerfilTenant) {
        ModalPerfilTenant(
            usuario = estado.usuarioSistema,
            aoSalvarPerfil = { novoPerfil ->
                viewModel.atualizarPerfilTenant(novoPerfil)
                exibirModalPerfilTenant = false
            },
            aoEncerrarSessao = {
                exibirModalPerfilTenant = false
                aoEncerrarSessao()
            },
            aoFechar = { exibirModalPerfilTenant = false }
        )
    }

    if (confirmarRestauracaoDemo) {
        ModalConfirmacao(
            titulo = "Restaurar Base de Demonstração no Tenant",
            mensagem = "Deseja restaurar os 6 suplementos veganos, 5 clientes, 5 pedidos e 4 planos alimentares de demonstração na sua conta Google?",
            textoConfirmar = "Restaurar Base",
            ehDestrutivo = false,
            aoConfirmar = {
                viewModel.restaurarDadosDemonstracao()
                confirmarRestauracaoDemo = false
            },
            aoCancelar = { confirmarRestauracaoDemo = false }
        )
    }
}
