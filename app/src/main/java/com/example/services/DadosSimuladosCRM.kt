package com.example.services

import com.example.types.CanalAtendimento
import com.example.types.Cliente
import com.example.types.InteracaoAtendimento
import com.example.types.ItemPedido
import com.example.types.MetodoPagamento
import com.example.types.Pedido
import com.example.types.StatusPedido

object DadosSimuladosCRM {
    fun obterClientesIniciais(): List<Cliente> = listOf(
        Cliente(
            id = "cli-1",
            nomeCompleto = "Mariana Costa Ferreira",
            telefone = "(11) 99412-8830",
            email = "mariana.ferreira@estudioverde.com.br",
            cidadeLocalizacao = "São Paulo, SP",
            preferenciasAlimentares = listOf("Vegana estrita há 4 anos", "Prefere shakes de cacau", "Cozinha prática"),
            objetivos = listOf("Hipertrofia muscular", "Aumento de força no treino de inferiores"),
            restricoes = listOf("Baixa tolerância a FODMAPs no pré-treino"),
            alergias = listOf("Alergia a amendoim"),
            observacoes = "Treina musculação às 07h00 (5x/semana). Exames de Set/2026 mostraram B12 em 380 pg/mL — iniciada reposição sublingual.",
            dataCadastro = "14/02/2026",
            historicoInteracoes = listOf(
                InteracaoAtendimento(
                    id = "int-101",
                    data = "02/10/2026",
                    canal = CanalAtendimento.RETORNO.rotulo,
                    resumo = "Ganho de 1,4 kg de massa magra na bioimpedância em 45 dias. Ótima digestão com o Vegan Pro Blend Cacau.",
                    responsavel = "Dra. Clara Azevedo (CRN-3 48291)",
                    proximosPassos = "Manter 30g de proteína pós-treino e incluir Creatina 5g/dia contínuo."
                ),
                InteracaoAtendimento(
                    id = "int-102",
                    data = "15/08/2026",
                    canal = CanalAtendimento.CONSULTA_ONLINE.rotulo,
                    resumo = "Anamnese inicial e ajuste do aporte proteico diário para 1,8g/kg de peso.",
                    responsavel = "Dra. Clara Azevedo (CRN-3 48291)",
                    proximosPassos = "Solicitar hemograma completo, ferritina e vitamina B12."
                )
            )
        ),
        Cliente(
            id = "cli-2",
            nomeCompleto = "Rafael Mendes Oliveira",
            telefone = "(41) 98827-5104",
            email = "rafael.mendes@techsul.io",
            cidadeLocalizacao = "Curitiba, PR",
            preferenciasAlimentares = listOf("Vegetariano em transição 100% plant-based", "Gosta de sabores cítricos"),
            objetivos = listOf("Performance em corrida (Meia Maratona)", "Foco cognitivo no trabalho"),
            restricoes = listOf("Evitar cafeína após as 16h"),
            alergias = listOf("Intolerância severa à lactose"),
            observacoes = "Desenvolvedor de software, corre 40km semanais. Relatava fadiga vespertina antes de ajustar ferro e ômega-3.",
            dataCadastro = "08/04/2026",
            historicoInteracoes = listOf(
                InteracaoAtendimento(
                    id = "int-201",
                    data = "28/09/2026",
                    canal = CanalAtendimento.WHATSAPP.rotulo,
                    resumo = "Relatou melhora expressiva no pace de corrida usando o Nitro Green 25 min antes dos treinos longos de sábado.",
                    responsavel = "Dr. Lucas Silveira (Consultor Técnico)",
                    proximosPassos = "Renovar estoque de Ômega-3 DHA/EPA e Creatina na próxima semana."
                )
            )
        ),
        Cliente(
            id = "cli-3",
            nomeCompleto = "Camila Rocha Almeida",
            telefone = "(31) 99165-3390",
            email = "camilaa.rocha@gmail.com",
            cidadeLocalizacao = "Belo Horizonte, MG",
            preferenciasAlimentares = listOf("Vegana", "Alimentação orgânica e integral"),
            objetivos = listOf("Saúde da pele e fortalecimento capilar", "Reposição de B12 e Ômega-3"),
            restricoes = listOf("Restrição a adoçantes artificiais"),
            alergias = listOf("Sensibilidade ao glúten não-celíaca"),
            observacoes = "Prioriza produtos clean-label adoçados com taumatina/estévia. Excelente adesão ao protocolo noturno de Pro-Colágeno.",
            dataCadastro = "19/05/2026",
            historicoInteracoes = listOf(
                InteracaoAtendimento(
                    id = "int-301",
                    data = "04/10/2026",
                    canal = CanalAtendimento.PRESENCIAL.rotulo,
                    resumo = "Avaliação dermatofuncional e nutricional: redução visível da queda capilar após 60 dias de aminoácidos precursores + B12.",
                    responsavel = "Dra. Clara Azevedo (CRN-3 48291)",
                    proximosPassos = "Associar 1 dose de Vegan Pro Blend no lanche da tarde para atingir 95g de proteína/dia."
                )
            )
        ),
        Cliente(
            id = "cli-4",
            nomeCompleto = "Lucas Albuquerque Santos",
            telefone = "(48) 99640-7712",
            email = "lucas.surf@florianopolis.com.br",
            cidadeLocalizacao = "Florianópolis, SC",
            preferenciasAlimentares = listOf("Plant-based", "Smoothies com frutas congeladas"),
            objetivos = listOf("Recuperação muscular rápida", "Definição corporal (Recomposição)"),
            restricoes = listOf("Nenhuma restrição clínica"),
            alergias = listOf("Alergia a castanha-de-caju"),
            observacoes = "Pratica surf pela manhã e treino funcional à noite. Necessita de praticidade entre sessões.",
            dataCadastro = "22/07/2026",
            historicoInteracoes = listOf(
                InteracaoAtendimento(
                    id = "int-401",
                    data = "01/10/2026",
                    canal = CanalAtendimento.CONSULTA_ONLINE.rotulo,
                    resumo = "Ajuste de plano alimentar de 2.450 kcal com foco em 150g de proteínas vegetais.",
                    responsavel = "Dra. Clara Azevedo (CRN-3 48291)",
                    proximosPassos = "Acompanhar evolução da dobra abdominal em 30 dias."
                )
            )
        ),
        Cliente(
            id = "cli-5",
            nomeCompleto = "Juliana Ribeiro Vasconcelos",
            telefone = "(21) 98703-4421",
            email = "ju.vasconcelos@arquitetura.rio",
            cidadeLocalizacao = "Rio de Janeiro, RJ",
            preferenciasAlimentares = listOf("Ovolactovegetariana migrando para o veganismo"),
            objetivos = listOf("Emagrecimento saudável com saciedade", "Imunidade e longevidade"),
            restricoes = listOf("Gastrite leve — evitar pimenta e excesso de ácido cítrico em jejum"),
            alergias = listOf("Sem alergias conhecidas"),
            observacoes = "Substituiu o whey tradicional pelo Vegan Pro Blend devido a acne e desconforto abdominal.",
            dataCadastro = "10/09/2026",
            historicoInteracoes = listOf(
                InteracaoAtendimento(
                    id = "int-501",
                    data = "05/10/2026",
                    canal = CanalAtendimento.WHATSAPP.rotulo,
                    resumo = "Dúvida sobre diluição da proteína com leite de aveia no café da manhã. Orientada receita de overnight oats.",
                    responsavel = "Dr. Lucas Silveira (Consultor Técnico)",
                    proximosPassos = "Agendar consulta de retorno para 25/10/2026."
                )
            )
        )
    )

    fun obterPedidosIniciais(): List<Pedido> = listOf(
        Pedido(
            id = "ped-1",
            codigoPedido = "VF-2026-1001",
            clienteId = "cli-1",
            nomeCliente = "Mariana Costa Ferreira",
            itens = listOf(
                ItemPedido("prod-1", "Vegan Pro Blend Isolado - Cacau Belga", 2, 169.90, 339.80),
                ItemPedido("prod-2", "Creatina Monohidratada Ultrapura Vegan", 1, 124.90, 124.90),
                ItemPedido("prod-3", "Vitamina B12 Metilcobalamina Gotas", 1, 78.50, 78.50)
            ),
            subtotal = 543.20,
            desconto = 43.20,
            total = 500.00,
            status = StatusPedido.ENTREGUE.rotulo,
            metodoPagamento = MetodoPagamento.PIX.rotulo,
            dataCriacao = "02/10/2026",
            observacoes = "Kit protocolo hipertrofia trimestral com desconto fidelidade Pix."
        ),
        Pedido(
            id = "ped-2",
            codigoPedido = "VF-2026-1002",
            clienteId = "cli-2",
            nomeCliente = "Rafael Mendes Oliveira",
            itens = listOf(
                ItemPedido("prod-5", "Nitro Green Pré-Treino Botânico", 1, 139.90, 139.90),
                ItemPedido("prod-4", "Ômega-3 Vegetal de Microalgas DHA/EPA", 1, 184.00, 184.00)
            ),
            subtotal = 323.90,
            desconto = 0.0,
            total = 323.90,
            status = StatusPedido.ENVIADO.rotulo,
            metodoPagamento = MetodoPagamento.CARTAO_CREDITO.rotulo,
            dataCriacao = "04/10/2026",
            observacoes = "Envio expresso transportadora para Curitiba/PR."
        ),
        Pedido(
            id = "ped-3",
            codigoPedido = "VF-2026-1003",
            clienteId = "cli-3",
            nomeCliente = "Camila Rocha Almeida",
            itens = listOf(
                ItemPedido("prod-6", "Pro-Colágeno Vegan Derma", 2, 154.00, 308.00),
                ItemPedido("prod-3", "Vitamina B12 Metilcobalamina Gotas", 1, 78.50, 78.50)
            ),
            subtotal = 386.50,
            desconto = 16.50,
            total = 370.00,
            status = StatusPedido.PAGO.rotulo,
            metodoPagamento = MetodoPagamento.PIX.rotulo,
            dataCriacao = "05/10/2026",
            observacoes = "Retirada agendada no consultório após avaliação."
        ),
        Pedido(
            id = "ped-4",
            codigoPedido = "VF-2026-1004",
            clienteId = "cli-4",
            nomeCliente = "Lucas Albuquerque Santos",
            itens = listOf(
                ItemPedido("prod-1", "Vegan Pro Blend Isolado - Cacau Belga", 1, 169.90, 169.90),
                ItemPedido("prod-2", "Creatina Monohidratada Ultrapura Vegan", 1, 124.90, 124.90)
            ),
            subtotal = 294.80,
            desconto = 0.0,
            total = 294.80,
            status = StatusPedido.EM_SEPARACAO.rotulo,
            metodoPagamento = MetodoPagamento.LINK_PAGAMENTO.rotulo,
            dataCriacao = "06/10/2026",
            observacoes = "Acluir folder de receitas proteicas veganas na caixa."
        ),
        Pedido(
            id = "ped-5",
            codigoPedido = "VF-2026-1005",
            clienteId = "cli-5",
            nomeCliente = "Juliana Ribeiro Vasconcelos",
            itens = listOf(
                ItemPedido("prod-1", "Vegan Pro Blend Isolado - Cacau Belga", 1, 169.90, 169.90),
                ItemPedido("prod-4", "Ômega-3 Vegetal de Microalgas DHA/EPA", 1, 184.00, 184.00)
            ),
            subtotal = 353.90,
            desconto = 13.90,
            total = 340.00,
            status = StatusPedido.PENDENTE.rotulo,
            metodoPagamento = MetodoPagamento.BOLETO.rotulo,
            dataCriacao = "06/10/2026",
            observacoes = "Aguardando compensação bancária."
        )
    )
}
