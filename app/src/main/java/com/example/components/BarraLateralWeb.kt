package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hooks.AbaNavegacao
import com.example.types.UsuarioSistema
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.MintSoft
import com.example.ui.theme.PureWhite

@Composable
fun BarraLateralWeb(
    abaAtual: AbaNavegacao,
    usuario: UsuarioSistema,
    aoSelecionarAba: (AbaNavegacao) -> Unit,
    aoAbrirPerfilTenant: () -> Unit,
    aoAlternarModoCelular: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(210.dp)
            .fillMaxHeight()
            .testTag("barra_lateral_modelo_site"),
        color = EmeraldDeep,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MintSoft.copy(alpha = 0.14f), RoundedCornerShape(14.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Eco,
                        contentDescription = null,
                        tint = MintSoft,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "VEGAN FLOW",
                            style = MaterialTheme.typography.titleSmall,
                            color = PureWhite,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Versão Modelo Site",
                            style = MaterialTheme.typography.labelSmall,
                            color = MintSoft
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                AbaNavegacao.entries.forEach { aba ->
                    val selecionada = abaAtual == aba
                    val icone = when (aba) {
                        AbaNavegacao.PAINEL -> Icons.Filled.Dashboard
                        AbaNavegacao.PRODUTOS -> Icons.Filled.Inventory2
                        AbaNavegacao.CLIENTES -> Icons.Filled.Groups
                        AbaNavegacao.PEDIDOS -> Icons.AutoMirrored.Filled.ReceiptLong
                        AbaNavegacao.PLANOS -> Icons.Filled.RestaurantMenu
                    }
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = aba.titulo,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (selecionada) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selecionada,
                        onClick = { aoSelecionarAba(aba) },
                        icon = { Icon(imageVector = icone, contentDescription = aba.titulo) },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MintSoft,
                            selectedIconColor = EmeraldDeep,
                            selectedTextColor = EmeraldDeep,
                            unselectedIconColor = MintSoft,
                            unselectedTextColor = PureWhite
                        ),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }

            Column {
                HorizontalDivider(color = MintSoft.copy(alpha = 0.25f))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = aoAbrirPerfilTenant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = null, tint = MintSoft, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = usuario.nome.substringBefore(" ").ifBlank { "Conta" },
                        color = PureWhite,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(
                    onClick = aoAlternarModoCelular,
                    modifier = Modifier.fillMaxWidth().testTag("botao_voltar_modo_celular")
                ) {
                    Icon(Icons.Filled.PhoneAndroid, contentDescription = null, tint = MintSoft, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Modo Celular", color = MintSoft, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
