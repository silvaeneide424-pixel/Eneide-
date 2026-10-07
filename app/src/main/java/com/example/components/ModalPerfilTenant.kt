package com.example.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.types.UsuarioSistema

@Composable
fun ModalPerfilTenant(
    usuario: UsuarioSistema,
    aoSalvarPerfil: (UsuarioSistema) -> Unit,
    aoEncerrarSessao: () -> Unit,
    aoFechar: () -> Unit
) {
    var nome by remember { mutableStateOf(usuario.nome) }
    var cargo by remember { mutableStateOf(usuario.cargo) }
    var crn by remember { mutableStateOf(usuario.crnOuRegistro) }
    var erro by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = aoFechar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 16.dp)
                .testTag("modal_perfil_tenant"),
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "CONTA MULTI-TENANT & POLÍTICA RLS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Perfil do Usuário Autenticado",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "E-mail Google: ${usuario.email} • Tenant UID: ${usuario.id.take(12)}...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it; erro = null },
                    label = { Text("Nome de Exibição *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = cargo,
                    onValueChange = { cargo = it; erro = null },
                    label = { Text("Especialidade / Cargo *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = crn,
                    onValueChange = { crn = it; erro = null },
                    label = { Text("Registro Profissional (CRN) *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (erro != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(erro.orEmpty(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                }

                Spacer(modifier = Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = aoFechar, modifier = Modifier.weight(1f)) {
                        Text("Fechar")
                    }
                    Button(
                        onClick = {
                            if (nome.isBlank() || cargo.isBlank() || crn.isBlank()) {
                                erro = "⚠️ Preencha todos os campos do perfil."
                            } else {
                                aoSalvarPerfil(
                                    usuario.copy(
                                        nome = nome.trim(),
                                        cargo = cargo.trim(),
                                        crnOuRegistro = crn.trim()
                                    )
                                )
                            }
                        },
                        modifier = Modifier.weight(1f).testTag("botao_salvar_perfil_tenant")
                    ) {
                        Text("Salvar Perfil")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = aoEncerrarSessao,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth().testTag("botao_sair_conta_google")
                ) {
                    Text("Sair da Conta Google (Trocar Usuário)")
                }
            }
        }
    }
}
