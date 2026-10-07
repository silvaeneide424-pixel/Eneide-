package com.example.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.example.hooks.AbaNavegacao

@Composable
fun BarraNavegacaoInferior(
    abaAtual: AbaNavegacao,
    aoSelecionarAba: (AbaNavegacao) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("barra_navegacao_principal"),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        AbaNavegacao.entries.forEach { aba ->
            val selecionada = abaAtual == aba
            val icone = when (aba) {
                AbaNavegacao.PAINEL -> if (selecionada) Icons.Filled.Dashboard else Icons.Outlined.Dashboard
                AbaNavegacao.PRODUTOS -> if (selecionada) Icons.Filled.Inventory2 else Icons.Outlined.Inventory2
                AbaNavegacao.CLIENTES -> if (selecionada) Icons.Filled.Groups else Icons.Outlined.Groups
                AbaNavegacao.PEDIDOS -> if (selecionada) Icons.AutoMirrored.Filled.ReceiptLong else Icons.AutoMirrored.Outlined.ReceiptLong
                AbaNavegacao.PLANOS -> if (selecionada) Icons.Filled.RestaurantMenu else Icons.Outlined.RestaurantMenu
            }

            NavigationBarItem(
                selected = selecionada,
                onClick = { aoSelecionarAba(aba) },
                icon = {
                    Icon(
                        imageVector = icone,
                        contentDescription = aba.titulo
                    )
                },
                label = {
                    Text(
                        text = aba.titulo,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selecionada) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.testTag("nav_aba_${aba.name.lowercase()}")
            )
        }
    }
}
