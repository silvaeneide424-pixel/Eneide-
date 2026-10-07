package com.example.services

import com.example.types.ItemRefeicao
import com.example.types.PlanoAlimentar
import com.example.types.Refeicao
import com.example.types.SuplementoSugerido
import com.example.types.UsuarioSistema

object DadosSimuladosPlanos {
    fun obterUsuarioPadrao(): UsuarioSistema = UsuarioSistema(
        id = "usr-01",
        nome = "Dra. Clara Azevedo",
        cargo = "Nutricionista Clínica & Esportiva Plant-Based",
        crnOuRegistro = "CRN-3 48291",
        email = "clara.azevedo@veganflow.com.br"
    )

    fun obterPlanosIniciais(): List<PlanoAlimentar> = listOf(
        PlanoAlimentar(
            id = "plano-1",
            clienteId = "cli-1",
            nomeCliente = "Mariana Costa Ferreira",
            titulo = "Protocolo Hipertrofia Plant-Based Limpo",
            objetivoPrincipal = "Ganho de Massa Magra e Força (Low-FODMAP pré-treino)",
            caloriasDiarias = 2150,
            proteinasGramas = 128,
            carboidratosGramas = 245,
            gordurasGramas = 62,
            refeicoes = listOf(
                Refeicao(
                    id = "ref-101",
                    nomeRefeicao = "Pré-Treino Rápido",
                    horarioSugerido = "06:20",
                    itens = listOf(
                        ItemRefeicao("Banana nanica madura amassada", "1 unidade média (100g)", 1, 92, "2 tâmaras Medjool sem caroço"),
                        ItemRefeicao("Aveia em flocos finos sem glúten", "30g (2 col. sopa)", 4, 115, "Flocos de quinoa real")
                    ),
                    observacaoRefeicao = "Consumir 40 min antes do treino com 300ml de água."
                ),
                Refeicao(
                    id = "ref-102",
                    nomeRefeicao = "Pós-Treino / Café da Manhã",
                    horarioSugerido = "08:30",
                    itens = listOf(
                        ItemRefeicao("Shake Vegan Pro Blend Cacau + Leite de Amêndoas", "30g pó + 250ml bebida", 25, 155, "30g proteína de ervilha neutra batida com cacau"),
                        ItemRefeicao("Tofu mexido com cúrcuma e azeite extravirgem", "120g de tofu firme", 14, 145, "Homus de grão-de-bico germinado (90g)"),
                        ItemRefeicao("Pão de fermentação natural integral", "2 fatias (70g)", 6, 170, "Tapioca com sementes de chia (60g)")
                    ),
                    observacaoRefeicao = "Adicionar 5g de Creatina Vegan diretamente no shake pós-treino."
                ),
                Refeicao(
                    id = "ref-103",
                    nomeRefeicao = "Almoço Completo",
                    horarioSugerido = "13:00",
                    itens = listOf(
                        ItemRefeicao("Tempeh grelhado no shoyu de coco", "130g", 26, 240, "Edamame cozido no vapor (160g)"),
                        ItemRefeicao("Arroz negro ou integral + Feijão fradinho", "120g arroz + 100g feijão", 13, 250, "Quinoa mista cozida (150g)"),
                        ItemRefeicao("Brócolis ninja no vapor + Sementes de abóbora", "100g + 15g sementes", 7, 115, "Couve-flor assada com tahine")
                    ),
                    observacaoRefeicao = "Espremer meio limão sobre os vegetais para triplicar a absorção do ferro não-heme."
                ),
                Refeicao(
                    id = "ref-104",
                    nomeRefeicao = "Jantar Recuperador",
                    horarioSugerido = "20:00",
                    itens = listOf(
                        ItemRefeicao("Moqueca proteica de grão-de-bico e cogumelos Paris", "220g", 18, 290, "Lentilha síria com tofu defumado (200g)"),
                        ItemRefeicao("Purê rústico de mandioquinha (batata-baroa)", "130g", 2, 135, "Abóbora cabotiá assada (160g)")
                    ),
                    observacaoRefeicao = "Evitar líquidos volumosos durante a refeição."
                )
            ),
            suplementosSugeridos = listOf(
                SuplementoSugerido(
                    produtoId = "prod-1",
                    nomeSuplemento = "Vegan Pro Blend Isolado - Cacau Belga",
                    doseRecomendada = "30g (1 scoop)",
                    horarioMomento = "08:30 (Pós-Treino)",
                    justificativaClinica = "Garante pico de leucina para síntese proteica miofibrilar imediata."
                ),
                SuplementoSugerido(
                    produtoId = "prod-2",
                    nomeSuplemento = "Creatina Monohidratada Ultrapura Vegan",
                    doseRecomendada = "5g (1 dosador cheio)",
                    horarioMomento = "08:30 (Junto ao Shake)",
                    justificativaClinica = "Saturação dos estoques de fosfocreatina muscular em dieta estritamente vegetal."
                ),
                SuplementoSugerido(
                    produtoId = "prod-3",
                    nomeSuplemento = "Vitamina B12 Metilcobalamina Gotas",
                    doseRecomendada = "3 gotas sublinguais (29,7 mcg)",
                    horarioMomento = "Ao acordar em jejum",
                    justificativaClinica = "Otimização dos níveis séricos acima de 500 pg/mL e metabolismo energético."
                )
            ),
            orientacoesGerais = "Meta hídrica: 3,0 litros de água/dia. Deixar leguminosas de molho por 12h com descarte da água para eliminar fitatos.",
            dataCriacao = "02/10/2026",
            ativo = true
        ),
        PlanoAlimentar(
            id = "plano-2",
            clienteId = "cli-2",
            nomeCliente = "Rafael Mendes Oliveira",
            titulo = "Endurance Meia Maratona & Foco Cognitivo",
            objetivoPrincipal = "Performance Aeróbica e Recuperação Anti-inflamatória",
            caloriasDiarias = 2680,
            proteinasGramas = 135,
            carboidratosGramas = 365,
            gordurasGramas = 74,
            refeicoes = listOf(
                Refeicao(
                    id = "ref-201",
                    nomeRefeicao = "Desjejum Energético",
                    horarioSugerido = "07:00",
                    itens = listOf(
                        ItemRefeicao("Panqueca de aveia, banana e pasta de amêndoas", "140g", 16, 380, "Mingau proteico de aveia com mirtilos"),
                        ItemRefeicao("Tofu grelhado com orégano e tomate cereja", "100g", 11, 110, "Pasta de grão-de-bico com linhaça dourada")
                    ),
                    observacaoRefeicao = "Excelente carga glicêmica controlada para manhã de trabalho."
                ),
                Refeicao(
                    id = "ref-202",
                    nomeRefeicao = "Almoço Anti-inflamatório",
                    horarioSugerido = "12:30",
                    itens = listOf(
                        ItemRefeicao("Lentilha vermelha cozida com quinoa real", "240g", 22, 340, "Feijão azuki com arroz cateto integral"),
                        ItemRefeicao("Hambúrguer artesanal de proteína de ervilha", "120g", 24, 210, "Seitan grelhado acebolado (110g)"),
                        ItemRefeicao("Salada de folhas verdes escuras, beterraba crua e nozes", "150g", 6, 165, "Salada de espinafre, cenoura e castanha-do-pará")
                    ),
                    observacaoRefeicao = "Ingerir as 2 cápsulas de Ômega-3 de Microalgas junto ao almoço."
                )
            ),
            suplementosSugeridos = listOf(
                SuplementoSugerido(
                    produtoId = "prod-5",
                    nomeSuplemento = "Nitro Green Pré-Treino Botânico",
                    doseRecomendada = "10g em 200ml de água",
                    horarioMomento = "25 min antes da corrida",
                    justificativaClinica = "Nitratos da beterraba reduzem o custo de oxigênio em corridas de longa distância."
                ),
                SuplementoSugerido(
                    produtoId = "prod-4",
                    nomeSuplemento = "Ômega-3 Vegetal de Microalgas DHA/EPA",
                    doseRecomendada = "2 cápsulas (750mg DHA+EPA)",
                    horarioMomento = "12:30 (Almoço)",
                    justificativaClinica = "Modulação inflamatória articular pós-impacto e neuroproteção."
                )
            ),
            orientacoesGerais = "Nos treinos acima de 14km aos sábados, repor eletrólitos e 30g de carboidrato a cada 45 minutos.",
            dataCriacao = "28/09/2026",
            ativo = true
        ),
        PlanoAlimentar(
            id = "plano-3",
            clienteId = "cli-3",
            nomeCliente = "Camila Rocha Almeida",
            titulo = "Nutrição Estética Integrativa & Longevidade",
            objetivoPrincipal = "Estímulo de Colágeno Endógeno, Saúde Capilar e Antioxidantes",
            caloriasDiarias = 1820,
            proteinasGramas = 98,
            carboidratosGramas = 190,
            gordurasGramas = 58,
            refeicoes = listOf(
                Refeicao(
                    id = "ref-301",
                    nomeRefeicao = "Bowl Matinal Antioxidante",
                    horarioSugerido = "08:00",
                    itens = listOf(
                        ItemRefeicao("Iogurte natural de coco sem açúcar com chia e morangos", "180g", 8, 210, "Smoothie de frutas vermelhas com leite de castanhas"),
                        ItemRefeicao("Sementes de abóbora tostadas e amêndoas laminadas", "25g", 7, 145, "Mix de nozes-pecã e gergelim negro")
                    ),
                    observacaoRefeicao = "Rico em zinco, selênio e polifenóis protetores da derme."
                ),
                Refeicao(
                    id = "ref-302",
                    nomeRefeicao = "Lanche da Tarde Proteico",
                    horarioSugerido = "16:30",
                    itens = listOf(
                        ItemRefeicao("Shake Vegan Pro Blend Cacau Belga batido com gelo", "30g", 24, 118, "Mousse rápida de abacate (60g) com 1 scoop de proteína")
                    ),
                    observacaoRefeicao = "Garante o aporte de aminoácidos essenciais sem picos de insulina."
                )
            ),
            suplementosSugeridos = listOf(
                SuplementoSugerido(
                    produtoId = "prod-6",
                    nomeSuplemento = "Pro-Colágeno Vegan Derma",
                    doseRecomendada = "8g (1 medidor) em 150ml de água",
                    horarioMomento = "21:30 (Ceia Noturna)",
                    justificativaClinica = "Fornece Glicina, Prolina, Lisina e Silício Orgânico durante o pico noturno de regeneração tecidual."
                ),
                SuplementoSugerido(
                    produtoId = "prod-3",
                    nomeSuplemento = "Vitamina B12 Metilcobalamina Gotas",
                    doseRecomendada = "2 gotas sublinguais",
                    horarioMomento = "08:00 (Jejum)",
                    justificativaClinica = "Essencial para divisão celular rápida do folículo piloso."
                )
            ),
            orientacoesGerais = "Priorizar alimentos ricos em betacaroteno (cenoura, abóbora, manga) e licopeno (molho de tomate caseiro com azeite).",
            dataCriacao = "04/10/2026",
            ativo = true
        ),
        PlanoAlimentar(
            id = "plano-4",
            clienteId = "cli-4",
            nomeCliente = "Lucas Albuquerque Santos",
            titulo = "Recomposição Corporal Surf & Funcional",
            objetivoPrincipal = "Redução de Percentual de Gordura mantendo 150g de Proteína",
            caloriasDiarias = 2380,
            proteinasGramas = 150,
            carboidratosGramas = 260,
            gordurasGramas = 64,
            refeicoes = listOf(
                Refeicao(
                    id = "ref-401",
                    nomeRefeicao = "Pós-Surf Matinal",
                    horarioSugerido = "09:00",
                    itens = listOf(
                        ItemRefeicao("Smoothie Tropical: Açaí puro zero xarope + Vegan Pro Blend + Banana", "350ml", 28, 360, "Vitamina de manga, aveia e proteína vegetal"),
                        ItemRefeicao("Crepioca vegana de farinha de grão-de-bico com tofu", "120g", 17, 230, "Sanduíche integral com pasta de tofu defumado")
                    ),
                    observacaoRefeicao = "Reposição imediata de glicogênio muscular pós-mar."
                )
            ),
            suplementosSugeridos = listOf(
                SuplementoSugerido(
                    produtoId = "prod-1",
                    nomeSuplemento = "Vegan Pro Blend Isolado - Cacau Belga",
                    doseRecomendada = "30g (1 scoop)",
                    horarioMomento = "09:00 e 17:30",
                    justificativaClinica = "Fracionamento proteico para atingir 150g/dia com facilidade e baixa caloria."
                ),
                SuplementoSugerido(
                    produtoId = "prod-2",
                    nomeSuplemento = "Creatina Monohidratada Ultrapura Vegan",
                    doseRecomendada = "5g",
                    horarioMomento = "09:00",
                    justificativaClinica = "Manutenção da potência de remada e explosão muscular em déficit calórico leve."
                )
            ),
            orientacoesGerais = "Atenção total para evitar castanha-de-caju em granolas comerciais devido à alergia registrada.",
            dataCriacao = "01/10/2026",
            ativo = true
        )
    )
}
