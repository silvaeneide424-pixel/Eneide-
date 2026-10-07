package com.example.types

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItemRefeicao(
    val alimento: String,
    val quantidadeMedida: String,
    val proteinaG: Int,
    val kcal: Int,
    val substituicaoSugerida: String
)

@JsonClass(generateAdapter = true)
data class Refeicao(
    val id: String,
    val nomeRefeicao: String,
    val horarioSugerido: String,
    val itens: List<ItemRefeicao>,
    val observacaoRefeicao: String
)

@JsonClass(generateAdapter = true)
data class SuplementoSugerido(
    val produtoId: String,
    val nomeSuplemento: String,
    val doseRecomendada: String,
    val horarioMomento: String,
    val justificativaClinica: String
)

@JsonClass(generateAdapter = true)
data class PlanoAlimentar(
    val id: String,
    val clienteId: String,
    val nomeCliente: String,
    val titulo: String,
    val objetivoPrincipal: String,
    val caloriasDiarias: Int,
    val proteinasGramas: Int,
    val carboidratosGramas: Int,
    val gordurasGramas: Int,
    val refeicoes: List<Refeicao>,
    val suplementosSugeridos: List<SuplementoSugerido>,
    val orientacoesGerais: String,
    val dataCriacao: String,
    val ativo: Boolean
)

@JsonClass(generateAdapter = true)
data class UsuarioSistema(
    val id: String,
    val nome: String,
    val cargo: String,
    val crnOuRegistro: String,
    val email: String
)

data class MetricasDashboard(
    val faturamentoTotal: Double,
    val ticketMedio: Double,
    val totalPedidos: Int,
    val pedidosAtivos: Int,
    val totalClientes: Int,
    val planosAtivos: Int,
    val produtosEstoqueBaixo: Int
)
