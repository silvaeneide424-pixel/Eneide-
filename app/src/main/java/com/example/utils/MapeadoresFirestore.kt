package com.example.utils

import com.example.types.Cliente
import com.example.types.InteracaoAtendimento
import com.example.types.ItemNutricional
import com.example.types.ItemPedido
import com.example.types.ItemRefeicao
import com.example.types.Pedido
import com.example.types.PerguntaFrequente
import com.example.types.PlanoAlimentar
import com.example.types.Produto
import com.example.types.Refeicao
import com.example.types.SuplementoSugerido
import com.example.types.UsuarioSistema
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue

object MapeadoresFirestore {

    fun perfilParaMapaCriacao(perfil: UsuarioSistema, userId: String): Map<String, Any> = mapOf(
        "userId" to userId,
        "nome" to perfil.nome.ifBlank { "Profissional Vegan Flow" },
        "email" to perfil.email.ifBlank { "contato@veganflow.com.br" },
        "cargo" to perfil.cargo.ifBlank { "Nutricionista & Gestor" },
        "crnOuRegistro" to perfil.crnOuRegistro.ifBlank { "CRN Ativo" },
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )

    fun perfilParaMapaAtualizacao(perfil: UsuarioSistema): Map<String, Any> = mapOf(
        "nome" to perfil.nome.ifBlank { "Profissional Vegan Flow" },
        "email" to perfil.email.ifBlank { "contato@veganflow.com.br" },
        "cargo" to perfil.cargo.ifBlank { "Nutricionista & Gestor" },
        "crnOuRegistro" to perfil.crnOuRegistro.ifBlank { "CRN Ativo" },
        "updatedAt" to FieldValue.serverTimestamp()
    )

    fun documentoParaPerfil(doc: DocumentSnapshot): UsuarioSistema {
        val data = doc.data.orEmpty()
        return UsuarioSistema(
            id = (data["userId"] as? String) ?: doc.id,
            nome = (data["nome"] as? String) ?: "Profissional Vegan Flow",
            cargo = (data["cargo"] as? String) ?: "Nutricionista Plant-Based",
            crnOuRegistro = (data["crnOuRegistro"] as? String) ?: "CRN Ativo",
            email = (data["email"] as? String) ?: ""
        )
    }

    fun produtoParaMapaCriacao(prod: Produto, userId: String): Map<String, Any> =
        produtoCamposMutaveis(prod) + mapOf(
            "id" to prod.id,
            "userId" to userId,
            "createdAt" to FieldValue.serverTimestamp()
        )

    fun produtoCamposMutaveis(prod: Produto): Map<String, Any> = mapOf(
        "nome" to prod.nome,
        "categoria" to prod.categoria,
        "descricaoCurta" to prod.descricaoCurta,
        "descricaoCompleta" to prod.descricaoCompleta,
        "preco" to prod.preco,
        "estoqueSimulado" to prod.estoqueSimulado,
        "unidade" to prod.unidade,
        "ingredientes" to prod.ingredientes,
        "beneficios" to prod.beneficios,
        "alergenicos" to prod.alergenicos,
        "indicacaoUso" to prod.indicacaoUso,
        "modoUso" to prod.modoUso,
        "composicao" to prod.composicao,
        "porcaoReferencia" to prod.porcaoReferencia,
        "tabelaNutricional" to prod.tabelaNutricional.map {
            mapOf("nutriente" to it.nutriente, "quantidadePorPorcao" to it.quantidadePorPorcao, "percentualVD" to it.percentualVD)
        },
        "restricoes" to prod.restricoes,
        "perguntasFrequentes" to prod.perguntasFrequentes.map {
            mapOf("pergunta" to it.pergunta, "resposta" to it.resposta)
        },
        "seloDestaque" to prod.seloDestaque,
        "statusDisponibilidade" to prod.statusDisponibilidade,
        "updatedAt" to FieldValue.serverTimestamp()
    )

    @Suppress("UNCHECKED_CAST")
    fun documentoParaProduto(doc: DocumentSnapshot): Produto {
        val d = doc.data.orEmpty()
        val tab = (d["tabelaNutricional"] as? List<Map<String, Any?>>).orEmpty().map {
            ItemNutricional(
                nutriente = (it["nutriente"] as? String).orEmpty(),
                quantidadePorPorcao = (it["quantidadePorPorcao"] as? String).orEmpty(),
                percentualVD = (it["percentualVD"] as? String).orEmpty()
            )
        }
        val faqs = (d["perguntasFrequentes"] as? List<Map<String, Any?>>).orEmpty().map {
            PerguntaFrequente(
                pergunta = (it["pergunta"] as? String).orEmpty(),
                resposta = (it["resposta"] as? String).orEmpty()
            )
        }
        return Produto(
            id = (d["id"] as? String) ?: doc.id,
            nome = (d["nome"] as? String).orEmpty(),
            categoria = (d["categoria"] as? String).orEmpty(),
            descricaoCurta = (d["descricaoCurta"] as? String).orEmpty(),
            descricaoCompleta = (d["descricaoCompleta"] as? String).orEmpty(),
            preco = (d["preco"] as? Number)?.toDouble() ?: 0.0,
            estoqueSimulado = (d["estoqueSimulado"] as? Number)?.toInt() ?: 0,
            unidade = (d["unidade"] as? String).orEmpty(),
            ingredientes = (d["ingredientes"] as? List<String>).orEmpty(),
            beneficios = (d["beneficios"] as? List<String>).orEmpty(),
            alergenicos = (d["alergenicos"] as? List<String>).orEmpty(),
            indicacaoUso = (d["indicacaoUso"] as? String).orEmpty(),
            modoUso = (d["modoUso"] as? String).orEmpty(),
            composicao = (d["composicao"] as? String).orEmpty(),
            porcaoReferencia = (d["porcaoReferencia"] as? String).orEmpty(),
            tabelaNutricional = tab,
            restricoes = (d["restricoes"] as? List<String>).orEmpty(),
            perguntasFrequentes = faqs,
            seloDestaque = (d["seloDestaque"] as? String).orEmpty(),
            statusDisponibilidade = (d["statusDisponibilidade"] as? String).orEmpty()
        )
    }

    fun clienteParaMapaCriacao(cli: Cliente, userId: String): Map<String, Any> =
        clienteCamposMutaveis(cli) + mapOf(
            "id" to cli.id,
            "userId" to userId,
            "createdAt" to FieldValue.serverTimestamp()
        )

    fun clienteCamposMutaveis(cli: Cliente): Map<String, Any> = mapOf(
        "nomeCompleto" to cli.nomeCompleto,
        "telefone" to cli.telefone,
        "email" to cli.email,
        "cidadeLocalizacao" to cli.cidadeLocalizacao,
        "preferenciasAlimentares" to cli.preferenciasAlimentares,
        "objetivos" to cli.objetivos,
        "restricoes" to cli.restricoes,
        "alergias" to cli.alergias,
        "observacoes" to cli.observacoes,
        "dataCadastro" to cli.dataCadastro,
        "historicoInteracoes" to cli.historicoInteracoes.map {
            mapOf(
                "id" to it.id,
                "data" to it.data,
                "canal" to it.canal,
                "resumo" to it.resumo,
                "responsavel" to it.responsavel,
                "proximosPassos" to it.proximosPassos
            )
        },
        "updatedAt" to FieldValue.serverTimestamp()
    )

    @Suppress("UNCHECKED_CAST")
    fun documentoParaCliente(doc: DocumentSnapshot): Cliente {
        val d = doc.data.orEmpty()
        val interacoes = (d["historicoInteracoes"] as? List<Map<String, Any?>>).orEmpty().map {
            InteracaoAtendimento(
                id = (it["id"] as? String).orEmpty(),
                data = (it["data"] as? String).orEmpty(),
                canal = (it["canal"] as? String).orEmpty(),
                resumo = (it["resumo"] as? String).orEmpty(),
                responsavel = (it["responsavel"] as? String).orEmpty(),
                proximosPassos = (it["proximosPassos"] as? String).orEmpty()
            )
        }
        return Cliente(
            id = (d["id"] as? String) ?: doc.id,
            nomeCompleto = (d["nomeCompleto"] as? String).orEmpty(),
            telefone = (d["telefone"] as? String).orEmpty(),
            email = (d["email"] as? String).orEmpty(),
            cidadeLocalizacao = (d["cidadeLocalizacao"] as? String).orEmpty(),
            preferenciasAlimentares = (d["preferenciasAlimentares"] as? List<String>).orEmpty(),
            objetivos = (d["objetivos"] as? List<String>).orEmpty(),
            restricoes = (d["restricoes"] as? List<String>).orEmpty(),
            alergias = (d["alergias"] as? List<String>).orEmpty(),
            observacoes = (d["observacoes"] as? String).orEmpty(),
            dataCadastro = (d["dataCadastro"] as? String).orEmpty(),
            historicoInteracoes = interacoes
        )
    }
}
