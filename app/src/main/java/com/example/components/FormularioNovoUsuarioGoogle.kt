package com.example.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FormularioNovoUsuarioGoogle(
    cargoNovoUsuario: String,
    aoMudarCargo: (String) -> Unit,
    crnNovoUsuario: String,
    aoMudarCrn: (String) -> Unit,
    popularBaseDemo: Boolean,
    aoMudarPopularDemo: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Criar Novo Usuário (Tenant) com Google",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Configure os dados do seu consultório ou operação. Sua conta Google criará um ambiente exclusivo protegido por políticas RLS.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = cargoNovoUsuario,
            onValueChange = aoMudarCargo,
            label = { Text("Especialidade / Cargo Profissional *") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_novo_usuario_cargo"),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = crnNovoUsuario,
            onValueChange = aoMudarCrn,
            label = { Text("Registro Profissional (CRN / CNPJ) *") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_novo_usuario_crn"),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = popularBaseDemo,
                onCheckedChange = aoMudarPopularDemo
            )
            Text(
                text = "Inicializar meu tenant com catálogo vegano e exemplos de demonstração",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
