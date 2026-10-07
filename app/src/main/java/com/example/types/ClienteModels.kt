package com.example.types

import com.squareup.moshi.JsonClass

enum class CanalAtendimento(val rotulo: String) {
    CONSULTA_ONLINE("Consulta Online"),
    WHATSAPP("WhatsApp Consultivo"),
    PRESENCIAL("Avaliação Presencial"),
    RETORNO("Retorno Nutricional")
}

@JsonClass(generateAdapter = true)
data class InteracaoAtendimento(
    val id: String,
    val data: String,
    val canal: String,
    val resumo: String,
    val responsavel: String,
    val proximosPassos: String
)

@JsonClass(generateAdapter = true)
data class Cliente(
    val id: String,
    val nomeCompleto: String,
    val telefone: String,
    val email: String,
    val cidadeLocalizacao: String,
    val preferenciasAlimentares: List<String>,
    val objetivos: List<String>,
    val restricoes: List<String>,
    val alergias: List<String>,
    val observacoes: String,
    val dataCadastro: String,
    val historicoInteracoes: List<InteracaoAtendimento>
)
