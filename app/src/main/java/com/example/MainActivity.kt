package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.components.ConfigNovoUsuarioTenant
import com.example.components.ConteudoAutenticadoVeganFlow
import com.example.components.TelaAutenticacaoGoogle
import com.example.hooks.VeganFlowViewModel
import com.example.services.DataService
import com.example.ui.theme.MyApplicationTheme
import com.example.utils.GerenciadorAutenticacaoGoogle
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AplicacaoVeganFlow()
            }
        }
    }
}

@Composable
fun AplicacaoVeganFlow(
    auth: FirebaseAuth = Firebase.auth
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    val usuarioAtual by GerenciadorAutenticacaoGoogle
        .observarEstadoAuth(auth)
        .collectAsStateWithLifecycle(initialValue = auth.currentUser)

    var configNovoUsuarioPendente by remember { mutableStateOf<ConfigNovoUsuarioTenant?>(null) }

    val usuario = usuarioAtual
    if (usuario == null) {
        TelaAutenticacaoGoogle(
            aoAutenticarComSucesso = { config ->
                configNovoUsuarioPendente = config
            }
        )
    } else {
        val currentUserId = usuario.uid
        val nomeGoogle = usuario.displayName ?: "Profissional Vegan Flow"
        val emailGoogle = usuario.email ?: "contato@veganflow.com.br"

        val veganFlowViewModel: VeganFlowViewModel = viewModel(
            key = currentUserId,
            factory = viewModelFactory {
                initializer {
                    val app = checkNotNull(this[APPLICATION_KEY]) {
                        "APPLICATION_KEY ausente em CreationExtras"
                    }
                    val databaseId = app.getString(R.string.firestore_database_id)
                    val db = FirebaseFirestore.getInstance(databaseId)
                    VeganFlowViewModel(
                        dataService = DataService(db, auth),
                        currentUserId = currentUserId,
                        nomeInicial = nomeGoogle,
                        emailInicial = emailGoogle
                    )
                }
            }
        )

        ConteudoAutenticadoVeganFlow(
            viewModel = veganFlowViewModel,
            configInicial = configNovoUsuarioPendente,
            aoConsumirConfigInicial = { configNovoUsuarioPendente = null },
            aoEncerrarSessao = {
                GerenciadorAutenticacaoGoogle.encerrarSessao(
                    credentialManager = credentialManager,
                    scope = scope,
                    aoConcluir = {}
                )
            }
        )
    }
}
