package com.example.utils

import com.example.types.ItemPedido
import com.example.types.StatusDisponibilidade
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

object Formatadores {
    private val formatoMoedaBr = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

    fun formatarMoeda(valor: Double): String {
        return formatoMoedaBr.format(valor)
    }

    fun gerarIdUnico(): String {
        return UUID.randomUUID().toString()
    }

    fun gerarCodigoPedido(sequencia: Int): String {
        return "VF-2026-${(1000 + sequencia)}"
    }

    fun dataAtual2026(): String {
        return "06/10/2026"
    }

    fun calcularSubtotalItens(itens: List<ItemPedido>): Double {
        return itens.sumOf { it.subtotalItem }
    }

    fun calcularTotalPedido(subtotal: Double, desconto: Double): Double {
        return (subtotal - desconto).coerceAtLeast(0.0)
    }

    fun calcularStatusEstoque(estoque: Int): String {
        return when {
            estoque <= 0 -> StatusDisponibilidade.ESGOTADO.rotulo
            estoque <= 12 -> StatusDisponibilidade.ESTOQUE_BAIXO.rotulo
            else -> StatusDisponibilidade.DISPONIVEL.rotulo
        }
    }

    fun validarEmail(email: String): Boolean {
        val limpo = email.trim()
        return limpo.contains("@") && limpo.contains(".") && limpo.length >= 6
    }

    fun validarTelefone(telefone: String): Boolean {
        val digitos = telefone.filter { it.isDigit() }
        return digitos.length in 10..13
    }

    fun dividirListaTexto(texto: String): List<String> {
        return texto.split(",", ";", "\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }
}
