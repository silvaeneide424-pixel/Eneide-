package com.example.utils

import com.example.types.Cliente
import com.example.types.ItemNutricional
import com.example.types.ItemPedido
import com.example.types.ItemRefeicao
import com.example.types.Pedido
import com.example.types.PerguntaFrequente
import com.example.types.PlanoAlimentar
import com.example.types.Produto
import com.example.types.Refeicao
import com.example.types.StatusPedido
import com.example.types.SuplementoSugerido

object ConstrutoresEntidades {
    fun construirProduto(
        existente: Produto?,
        nome: String,
        categoria: String,
        descricaoCurta: String,
        descricaoCompleta: String,
        preco: Double,
        estoque: Int,
        unidade: String,
        ingredientesTexto: String,
        beneficiosTexto: String,
        modoUso: String
    ): Produto {
        return Produto(
            id = existente?.id ?: Formatadores.gerarIdUnico(),
            nome = nome.trim(),
            categoria = categoria,
            descricaoCurta = descricaoCurta.trim(),
            descricaoCompleta = descricaoCompleta.ifBlank { descricaoCurta }.trim(),
            preco = preco,
            estoqueSimulado = estoque,
            unidade = unidade.ifBlank { "Unidade" },
            ingredientes = Formatadores.dividirListaTexto(ingredientesTexto)
                .ifEmpty { listOf("Extrato botânico padronizado 100% vegano") },
            beneficios = Formatadores.dividirListaTexto(beneficiosTexto)
                .ifEmpty { listOf("Alta biodisponibilidade e suporte nutricional") },
            alergenicos = existente?.alergenicos ?: listOf("Não contém glúten", "Não contém lactose"),
            indicacaoUso = existente?.indicacaoUso ?: "Suplementação nutricional plant-based.",
            modoUso = modoUso.trim(),
            composicao = existente?.composicao ?: "Fórmula 100% vegetal limpa.",
            porcaoReferencia = existente?.porcaoReferencia ?: "1 dose padrão",
            tabelaNutricional = existente?.tabelaNutricional ?: listOf(
                ItemNutricional("Valor Energético", "95 kcal", "5%"),
                ItemNutricional("Compostos Ativos", "Dose Plena", "100%")
            ),
            restricoes = existente?.restricoes ?: listOf("Uso adulto."),
            perguntasFrequentes = existente?.perguntasFrequentes ?: listOf(
                PerguntaFrequente("Possui certificação vegana?", "Sim, 100% livre de ingredientes de origem animal.")
            ),
            seloDestaque = existente?.seloDestaque ?: "Fórmula Clean Label",
            statusDisponibilidade = Formatadores.calcularStatusEstoque(estoque)
        )
    }

    fun construirPedido(
        sequencia: Int,
        cliente: Cliente,
        itens: List<ItemPedido>,
        subtotal: Double,
        desconto: Double,
        total: Double,
        metodoPagamento: String,
        observacoes: String
    ): Pedido {
        return Pedido(
            id = Formatadores.gerarIdUnico(),
            codigoPedido = Formatadores.gerarCodigoPedido(sequencia),
            clienteId = cliente.id,
            nomeCliente = cliente.nomeCompleto,
            itens = itens,
            subtotal = subtotal,
            desconto = desconto,
            total = total,
            status = StatusPedido.PAGO.rotulo,
            metodoPagamento = metodoPagamento,
            dataCriacao = Formatadores.dataAtual2026(),
            observacoes = observacoes.ifBlank { "Venda registrada no PDV Vegan Flow." }.trim()
        )
    }

    fun construirPlanoAlimentar(
        existente: PlanoAlimentar?,
        cliente: Cliente,
        titulo: String,
        objetivo: String,
        calorias: Int,
        proteinas: Int,
        carboidratos: Int,
        gorduras: Int,
        refeicao1Texto: String,
        refeicao2Texto: String,
        produtosSelecionados: List<Produto>,
        orientacoes: String
    ): PlanoAlimentar {
        val sups = produtosSelecionados.map { p ->
            SuplementoSugerido(
                produtoId = p.id,
                nomeSuplemento = p.nome,
                doseRecomendada = p.porcaoReferencia,
                horarioMomento = "Conforme prescrição diária",
                justificativaClinica = p.descricaoCurta
            )
        }
        val refeicoes = existente?.refeicoes ?: listOf(
            Refeicao(
                id = Formatadores.gerarIdUnico(),
                nomeRefeicao = "Desjejum & Pós-Treino",
                horarioSugerido = "08:00",
                itens = listOf(
                    ItemRefeicao(refeicao1Texto, "1 porção completa", 28, 340, "Panqueca proteica de grão-de-bico")
                ),
                observacaoRefeicao = "Associar suplementos matinais."
            ),
            Refeicao(
                id = Formatadores.gerarIdUnico(),
                nomeRefeicao = "Almoço Equilibrado",
                horarioSugerido = "13:00",
                itens = listOf(
                    ItemRefeicao(refeicao2Texto, "320g", 32, 520, "Tempeh assado com arroz integral e feijão")
                ),
                observacaoRefeicao = "Incluir fonte de vitamina C na salada."
            )
        )
        return PlanoAlimentar(
            id = existente?.id ?: Formatadores.gerarIdUnico(),
            clienteId = cliente.id,
            nomeCliente = cliente.nomeCompleto,
            titulo = titulo.trim(),
            objetivoPrincipal = objetivo.trim(),
            caloriasDiarias = calorias,
            proteinasGramas = proteinas,
            carboidratosGramas = carboidratos,
            gordurasGramas = gorduras,
            refeicoes = refeicoes,
            suplementosSugeridos = sups,
            orientacoesGerais = orientacoes.trim(),
            dataCriacao = existente?.dataCriacao ?: Formatadores.dataAtual2026(),
            ativo = true
        )
    }
}
