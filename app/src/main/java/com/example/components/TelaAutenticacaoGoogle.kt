package com.example.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.R
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.MintSoft
import com.example.ui.theme.PureWhite
import com.example.utils.GerenciadorAutenticacaoGoogle

data class ConfigNovoUsuarioTenant(
    val cargoProfissional: String = "Nutricionista Clínica & Esportiva Plant-Based",
    val crnOuRegistro: String = "CRN-3 Ativo",
    val popularDadosIniciais: Boolean = true
)

@Composable
fun TelaAutenticacaoGoogle(
    aoAutenticarComSucesso: (ConfigNovoUsuarioTenant) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    var modoCriarNovoUsuario by remember { mutableStateOf(false) }
    var cargoNovoUsuario by remember { mutableStateOf("Nutricionista Clínica & Esportiva Plant-Based") }
    var crnNovoUsuario by remember { mutableStateOf("CRN-3 55120") }
    var popularBaseDemo by remember { mutableStateOf(true) }
    var carregando by remember { mutableStateOf(false) }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        GerenciadorAutenticacaoGoogle.tentarLoginSilencioso(
            context = context,
            credentialManager = credentialManager,
            scope = scope,
            aoAutenticar = {
                aoAutenticarComSucesso(ConfigNovoUsuarioTenant(cargoNovoUsuario, crnNovoUsuario, popularBaseDemo))
            },
            aoNaoAutenticado = {}
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("tela_autenticacao_obrigatoria"),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(18.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_vegan_flow_1791334407770),
                        contentDescription = "Vegan Flow SaaS",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(EmeraldDeep.copy(alpha = 0.85f))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Eco, contentDescription = null, tint = MintSoft)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "VEGAN FLOW SaaS",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Plataforma Multi-Usuário com Isolamento RLS no Cloud Firestore",
                                style = MaterialTheme.typography.bodySmall,
                                color = MintSoft,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = !modoCriarNovoUsuario,
                        onClick = { modoCriarNovoUsuario = false; mensagemErro = null },
                        label = { Text("Entrar na Conta") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        modifier = Modifier.weight(1f).testTag("aba_login_google")
                    )
                    FilterChip(
                        selected = modoCriarNovoUsuario,
                        onClick = { modoCriarNovoUsuario = true; mensagemErro = null },
                        label = { Text("Novo Usuário") },
                        leadingIcon = { Icon(Icons.Filled.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        modifier = Modifier.weight(1f).testTag("aba_criar_usuario_google")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                if (modoCriarNovoUsuario) {
                    FormularioNovoUsuarioGoogle(
                        cargoNovoUsuario = cargoNovoUsuario,
                        aoMudarCargo = { cargoNovoUsuario = it; mensagemErro = null },
                        crnNovoUsuario = crnNovoUsuario,
                        aoMudarCrn = { crnNovoUsuario = it; mensagemErro = null },
                        popularBaseDemo = popularBaseDemo,
                        aoMudarPopularDemo = { popularBaseDemo = it }
                    )
                } else {
                    Text(
                        text = "Login Obrigatório — Acesso ao Sistema",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Autentique-se com sua conta Google para acessar seus produtos, clientes, pedidos e planos alimentares isolados por usuário.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.VerifiedUser, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Políticas RLS Ativas: cada profissional visualiza e gerencia exclusivamente os dados do próprio UID no Firestore.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                if (mensagemErro != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = mensagemErro.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("erro_autenticacao_google")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (modoCriarNovoUsuario && (cargoNovoUsuario.isBlank() || crnNovoUsuario.isBlank())) {
                            mensagemErro = "⚠️ Preencha a especialidade e o registro (CRN) para criar seu novo usuário."
                            return@Button
                        }
                        carregando = true
                        mensagemErro = null
                        GerenciadorAutenticacaoGoogle.entrarComGoogleInterativo(
                            context = context,
                            credentialManager = credentialManager,
                            scope = scope,
                            aoSucesso = {
                                carregando = false
                                aoAutenticarComSucesso(
                                    ConfigNovoUsuarioTenant(cargoNovoUsuario.trim(), crnNovoUsuario.trim(), popularBaseDemo)
                                )
                            },
                            aoErro = { msg -> carregando = false; mensagemErro = msg },
                            aoCancelar = { carregando = false }
                        )
                    },
                    enabled = !carregando,
                    modifier = Modifier.fillMaxWidth().height(52.dp).testTag("botao_login_google_principal"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    if (carregando) {
                        CircularProgressIndicator(modifier = Modifier.size(22.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Filled.AccountCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (modoCriarNovoUsuario) "Criar Novo Usuário com Google" else "Entrar com Google (Sign in with Google)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
