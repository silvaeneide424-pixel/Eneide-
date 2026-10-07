package com.example.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun ModalConfirmacao(
    titulo: String,
    mensagem: String,
    textoConfirmar: String = "Confirmar",
    ehDestrutivo: Boolean = true,
    aoConfirmar: () -> Unit,
    aoCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = aoCancelar,
        icon = {
            Icon(
                imageVector = Icons.Filled.WarningAmber,
                contentDescription = titulo,
                tint = if (ehDestrutivo) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = mensagem,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = aoConfirmar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (ehDestrutivo) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.testTag("modal_confirmacao_botao_confirmar")
            ) {
                Text(textoConfirmar)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = aoCancelar,
                modifier = Modifier.testTag("modal_confirmacao_botao_cancelar")
            ) {
                Text("Cancelar")
            }
        }
    )
}
