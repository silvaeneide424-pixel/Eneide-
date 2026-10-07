package com.example.types

import com.squareup.moshi.JsonClass

enum class StatusPedido(val rotulo: String) {
    PENDENTE("Pendente"),
    PAGO("Pago"),
    EM_SEPARACAO("Em Separação"),
    ENVIADO("Enviado"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado")
}

enum class MetodoPagamento(val rotulo: String) {
    PIX("Pix Instantâneo"),
    CARTAO_CREDITO("Cartão de Crédito"),
    BOLETO("Boleto Faturado"),
    LINK_PAGAMENTO("Link de Pagamento")
}

@JsonClass(generateAdapter = true)
data class ItemPedido(
    val produtoId: String,
    val nomeProduto: String,
    val quantidade: Int,
    val precoUnitario: Double,
    val subtotalItem: Double
)

@JsonClass(generateAdapter = true)
data class Pedido(
    val id: String,
    val codigoPedido: String,
    val clienteId: String,
    val nomeCliente: String,
    val itens: List<ItemPedido>,
    val subtotal: Double,
    val desconto: Double,
    val total: Double,
    val status: String,
    val metodoPagamento: String,
    val dataCriacao: String,
    val observacoes: String
)
