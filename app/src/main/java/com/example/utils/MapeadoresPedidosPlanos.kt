package com.example.utils

import com.example.types.ItemPedido
import com.example.types.ItemRefeicao
import com.example.types.Pedido
import com.example.types.PlanoAlimentar
import com.example.types.Refeicao
import com.example.types.SuplementoSugerido
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue

object MapeadoresPedidosPlanos {

    fun pedidoParaMapaCriacao(ped: Pedido, userId: String): Map<String, Any> = mapOf(
        "id" to ped.id,
        "userId" to userId,
        "codigoPedido" to ped.codigoPedido,
        "clienteId" to ped.clienteId,
        "nomeCliente" to ped.nomeCliente,
        "itens" to ped.itens.map {
            mapOf(
                "produtoId" to it.produtoId,
                "nomeProduto" to it.nomeProduto,
                "quantidade" to it.quantidade,
                "precoUnitario" to it.precoUnitario,
                "subtotalItem" to it.subtotalItem
            )
        },
        "subtotal" to ped.subtotal,
        "desconto" to ped.desconto,
        "total" to ped.total,
        "status" to ped.status,
        "metodoPagamento" to ped.metodoPagamento,
        "dataCriacao" to ped.dataCriacao,
        "observacoes" to ped.observacoes,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )

    @Suppress("UNCHECKED_CAST")
    fun documentoParaPedido(doc: DocumentSnapshot): Pedido {
        val d = doc.data.orEmpty()
        val itens = (d["itens"] as? List<Map<String, Any?>>).orEmpty().map {
            ItemPedido(
                produtoId = (it["produtoId"] as? String).orEmpty(),
                nomeProduto = (it["nomeProduto"] as? String).orEmpty(),
                quantidade = (it["quantidade"] as? Number)?.toInt() ?: 1,
                precoUnitario = (it["precoUnitario"] as? Number)?.toDouble() ?: 0.0,
                subtotalItem = (it["subtotalItem"] as? Number)?.toDouble() ?: 0.0
            )
        }
        return Pedido(
            id = (d["id"] as? String) ?: doc.id,
            codigoPedido = (d["codigoPedido"] as? String).orEmpty(),
            clienteId = (d["clienteId"] as? String).orEmpty(),
            nomeCliente = (d["nomeCliente"] as? String).orEmpty(),
            itens = itens,
            subtotal = (d["subtotal"] as? Number)?.toDouble() ?: 0.0,
            desconto = (d["desconto"] as? Number)?.toDouble() ?: 0.0,
            total = (d["total"] as? Number)?.toDouble() ?: 0.0,
            status = (d["status"] as? String).orEmpty(),
            metodoPagamento = (d["metodoPagamento"] as? String).orEmpty(),
            dataCriacao = (d["dataCriacao"] as? String).orEmpty(),
            observacoes = (d["observacoes"] as? String).orEmpty()
        )
    }

    fun planoParaMapaCriacao(pl: PlanoAlimentar, userId: String): Map<String, Any> =
        planoCamposMutaveis(pl) + mapOf(
            "id" to pl.id,
            "userId" to userId,
            "dataCriacao" to pl.dataCriacao,
            "createdAt" to FieldValue.serverTimestamp()
        )

    fun planoCamposMutaveis(pl: PlanoAlimentar): Map<String, Any> = mapOf(
        "clienteId" to pl.clienteId,
        "nomeCliente" to pl.nomeCliente,
        "titulo" to pl.titulo,
        "objetivoPrincipal" to pl.objetivoPrincipal,
        "caloriasDiarias" to pl.caloriasDiarias,
        "proteinasGramas" to pl.proteinasGramas,
        "carboidratosGramas" to pl.carboidratosGramas,
        "gordurasGramas" to pl.gordurasGramas,
        "refeicoes" to pl.refeicoes.map { ref ->
            mapOf(
                "id" to ref.id,
                "nomeRefeicao" to ref.nomeRefeicao,
                "horarioSugerido" to ref.horarioSugerido,
                "observacaoRefeicao" to ref.observacaoRefeicao,
                "itens" to ref.itens.map { item ->
                    mapOf(
                        "alimento" to item.alimento,
                        "quantidadeMedida" to item.quantidadeMedida,
                        "proteinaG" to item.proteinaG,
                        "kcal" to item.kcal,
                        "substituicaoSugerida" to item.substituicaoSugerida
                    )
                }
            )
        },
        "suplementosSugeridos" to pl.suplementosSugeridos.map { sup ->
            mapOf(
                "produtoId" to sup.produtoId,
                "nomeSuplemento" to sup.nomeSuplemento,
                "doseRecomendada" to sup.doseRecomendada,
                "horarioMomento" to sup.horarioMomento,
                "justificativaClinica" to sup.justificativaClinica
            )
        },
        "orientacoesGerais" to pl.orientacoesGerais,
        "ativo" to pl.ativo,
        "updatedAt" to FieldValue.serverTimestamp()
    )

    @Suppress("UNCHECKED_CAST")
    fun documentoParaPlano(doc: DocumentSnapshot): PlanoAlimentar {
        val d = doc.data.orEmpty()
        val refeicoes = (d["refeicoes"] as? List<Map<String, Any?>>).orEmpty().map { r ->
            val itens = (r["itens"] as? List<Map<String, Any?>>).orEmpty().map { i ->
                ItemRefeicao(
                    alimento = (i["alimento"] as? String).orEmpty(),
                    quantidadeMedida = (i["quantidadeMedida"] as? String).orEmpty(),
                    proteinaG = (i["proteinaG"] as? Number)?.toInt() ?: 0,
                    kcal = (i["kcal"] as? Number)?.toInt() ?: 0,
                    substituicaoSugerida = (i["substituicaoSugerida"] as? String).orEmpty()
                )
            }
            Refeicao(
                id = (r["id"] as? String).orEmpty(),
                nomeRefeicao = (r["nomeRefeicao"] as? String).orEmpty(),
                horarioSugerido = (r["horarioSugerido"] as? String).orEmpty(),
                itens = itens,
                observacaoRefeicao = (r["observacaoRefeicao"] as? String).orEmpty()
            )
        }
        val sups = (d["suplementosSugeridos"] as? List<Map<String, Any?>>).orEmpty().map { s ->
            SuplementoSugerido(
                produtoId = (s["produtoId"] as? String).orEmpty(),
                nomeSuplemento = (s["nomeSuplemento"] as? String).orEmpty(),
                doseRecomendada = (s["doseRecomendada"] as? String).orEmpty(),
                horarioMomento = (s["horarioMomento"] as? String).orEmpty(),
                justificativaClinica = (s["justificativaClinica"] as? String).orEmpty()
            )
        }
        return PlanoAlimentar(
            id = (d["id"] as? String) ?: doc.id,
            clienteId = (d["clienteId"] as? String).orEmpty(),
            nomeCliente = (d["nomeCliente"] as? String).orEmpty(),
            titulo = (d["titulo"] as? String).orEmpty(),
            objetivoPrincipal = (d["objetivoPrincipal"] as? String).orEmpty(),
            caloriasDiarias = (d["caloriasDiarias"] as? Number)?.toInt() ?: 0,
            proteinasGramas = (d["proteinasGramas"] as? Number)?.toInt() ?: 0,
            carboidratosGramas = (d["carboidratosGramas"] as? Number)?.toInt() ?: 0,
            gordurasGramas = (d["gordurasGramas"] as? Number)?.toInt() ?: 0,
            refeicoes = refeicoes,
            suplementosSugeridos = sups,
            orientacoesGerais = (d["orientacoesGerais"] as? String).orEmpty(),
            dataCriacao = (d["dataCriacao"] as? String).orEmpty(),
            ativo = (d["ativo"] as? Boolean) ?: true
        )
    }
}
