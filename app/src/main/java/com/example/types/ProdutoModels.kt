package com.example.types

import com.squareup.moshi.JsonClass

enum class StatusDisponibilidade(val rotulo: String) {
    DISPONIVEL("Disponível"),
    ESTOQUE_BAIXO("Estoque Baixo"),
    ESGOTADO("Esgotado")
}

enum class CategoriaProduto(val rotulo: String) {
    PROTEINAS("Proteínas Vegetais"),
    PERFORMANCE("Performance & Força"),
    VITAMINAS("Vitaminas & Minerais"),
    LONGEVIDADE("Longevidade & Foco"),
    OMEGAS("Lipídios & Ômegas")
}

@JsonClass(generateAdapter = true)
data class ItemNutricional(
    val nutriente: String,
    val quantidadePorPorcao: String,
    val percentualVD: String
)

@JsonClass(generateAdapter = true)
data class PerguntaFrequente(
    val pergunta: String,
    val resposta: String
)

@JsonClass(generateAdapter = true)
data class Produto(
    val id: String,
    val nome: String,
    val categoria: String,
    val descricaoCurta: String,
    val descricaoCompleta: String,
    val preco: Double,
    val estoqueSimulado: Int,
    val unidade: String,
    val ingredientes: List<String>,
    val beneficios: List<String>,
    val alergenicos: List<String>,
    val indicacaoUso: String,
    val modoUso: String,
    val composicao: String,
    val porcaoReferencia: String,
    val tabelaNutricional: List<ItemNutricional>,
    val restricoes: List<String>,
    val perguntasFrequentes: List<PerguntaFrequente>,
    val seloDestaque: String,
    val statusDisponibilidade: String
)
