package com.example.services

import com.example.types.CategoriaProduto
import com.example.types.ItemNutricional
import com.example.types.PerguntaFrequente
import com.example.types.Produto
import com.example.types.StatusDisponibilidade

object DadosSimuladosProdutos {
    fun obterProdutosIniciais(): List<Produto> = listOf(
        Produto(
            id = "prod-1",
            nome = "Vegan Pro Blend Isolado (Ervilha & Arroz) - Cacau Belga",
            categoria = CategoriaProduto.PROTEINAS.rotulo,
            descricaoCurta = "24g de proteína vegetal isolada com aminograma completo e enzimas digestivas.",
            descricaoCompleta = "Blend sinérgico de proteína isolada de ervilha amarela canadense e proteína orgânica de arroz integral germinado. Adoçado com taumatina e estévia reb-A, livre de textura arenosa e enriquecido com bromelina para absorção rápida.",
            preco = 169.90,
            estoqueSimulado = 38,
            unidade = "Pote 900g",
            ingredientes = listOf(
                "Proteína isolada de ervilha",
                "Proteína concentrada de arroz integral germinado",
                "Cacau alcalino puro em pó",
                "Triglicerídeos de cadeia média (TCM de coco)",
                "Glicosídeos de esteviol e taumatina"
            ),
            beneficios = listOf(
                "Síntese proteica e hipertrofia muscular eficiente",
                "4,8g de BCAA e 4,2g de Glutamina natural por dose",
                "Zero estufamento gástrico — hipoalergênico"
            ),
            alergenicos = listOf("Não contém glúten", "Não contém soja", "Não contém lactose"),
            indicacaoUso = "Atletas, praticantes de musculação e pacientes em transição ou dieta plant-based com meta proteica elevada.",
            modoUso = "Diluir 1 medidor cheio (30g) em 250ml de água gelada ou bebida vegetal de amêndoas no pós-treino ou lanche.",
            composicao = "Proporção 70% Ervilha Isolada + 30% Arroz Germinado (PDCAAS = 1.0).",
            porcaoReferencia = "30g (1 dosador)",
            tabelaNutricional = listOf(
                ItemNutricional("Valor Energético", "118 kcal", "6%"),
                ItemNutricional("Proteínas", "24 g", "48%"),
                ItemNutricional("Carboidratos", "2,1 g", "1%"),
                ItemNutricional("Gorduras Totais", "1,5 g", "3%"),
                ItemNutricional("Ferro Biodisponível", "4,2 mg", "30%"),
                ItemNutricional("Sódio", "145 mg", "7%")
            ),
            restricoes = listOf("Pacientes com doença renal crônica severa devem ajustar dose com nutricionista."),
            perguntasFrequentes = listOf(
                PerguntaFrequente("Fica arenoso na coqueteleira?", "Não! A micronização a frio garante textura cremosa semelhante ao shake tradicional."),
                PerguntaFrequente("Pode ser aquecido em receitas?", "Sim, mantém estabilidade nutricional em panquecas, mingaus de aveia e bolos.")
            ),
            seloDestaque = "Mais Vendido • 24g Proteína",
            statusDisponibilidade = StatusDisponibilidade.DISPONIVEL.rotulo
        ),
        Produto(
            id = "prod-2",
            nome = "Creatina Monohidratada Ultrapura Vegan 100% Micronizada",
            categoria = CategoriaProduto.PERFORMANCE.rotulo,
            descricaoCurta = "Creatina vegana de síntese 100% não animal para força explosiva e neuroproteção.",
            descricaoCompleta = "Creatina monohidratada com pureza analítica de 99,9% obtida por síntese química limpa a partir de sarcosinato e cianamida, Essencial para veganos e vegetarianos cujos estoques intramusculares basais são naturalmente menores.",
            preco = 124.90,
            estoqueSimulado = 9,
            unidade = "Pote 300g",
            ingredientes = listOf("Creatina monohidratada micronizada malha 200 mesh"),
            beneficios = listOf(
                "Aumento significativo de força e potência anaeróbica",
                "Suporte cognitivo e redução da fadiga mental",
                "Melhora na ressíntese de ATP celular"
            ),
            alergenicos = listOf("Zero alergênicos", "Sem glúten", "Sem aditivos ou corantes"),
            indicacaoUso = "Praticantes de atividades físicas, idosos para preservação de massa magra e estudantes em alta demanda mental.",
            modoUso = "Consumir 3g a 5g (1 dosador raso) todos os dias, inclusive nos dias sem treino, preferencialmente junto a uma refeição.",
            composicao = "100% Creatina Monohidratada Ultrapura.",
            porcaoReferencia = "3g (1 dosador)",
            tabelaNutricional = listOf(
                ItemNutricional("Valor Energético", "0 kcal", "0%"),
                ItemNutricional("Creatina Monohidratada", "3.000 mg", "**")
            ),
            restricoes = listOf("Ingerir no mínimo 35ml de água por kg de peso corporal ao dia."),
            perguntasFrequentes = listOf(
                PerguntaFrequente("Creatina retém líquido subcutâneo?", "Não, a retenção hídrica ocorre exclusivamente dentro da célula muscular (intracelular).")
            ),
            seloDestaque = "Essencial Plant-Based",
            statusDisponibilidade = StatusDisponibilidade.ESTOQUE_BAIXO.rotulo
        ),
        Produto(
            id = "prod-3",
            nome = "Vitamina B12 Metilcobalamina Ativa Sublingual Gotas Sabor Frutas Vermelhas",
            categoria = CategoriaProduto.VITAMINAS.rotulo,
            descricaoCurta = "Forma coenzimada ativa (9,9 mcg/gota) de rápida absorção sublingual sem conversão hepática.",
            descricaoCompleta = "Metilcobalamina obtida por biofermentação natural em veículo de glicerina vegetal orgânica. Indispensável para manutenção da bainha de mielina, formação de hemácias e controle da homocisteína em adeptos do estilo vegano.",
            preco = 78.50,
            estoqueSimulado = 52,
            unidade = "Frasco 30ml",
            ingredientes = listOf("Metilcobalamina", "Água purificada", "Glicerina vegetal", "Aroma natural de amora e framboesa"),
            beneficios = listOf(
                "Absorção direta pela mucosa sublingual",
                "Prevenção de anemia megaloblástica e neuropatias",
                "Mais disposição energética e foco diário"
            ),
            alergenicos = listOf("Livre de glúten", "Sem açúcar", "Sem álcool"),
            indicacaoUso = "Uso diário ou semanal obrigatório para veganos estritos, ovolactovegetarianos e indivíduos com B12 sérica abaixo de 450 pg/mL.",
            modoUso = "Pingar 1 a 5 gotas embaixo da língua, aguardar 30 segundos antes de engolir, conforme prescrição nutricional.",
            composicao = "Metilcobalamina bioidêntica 9,9 mcg por gota (412% VD).",
            porcaoReferencia = "1 gota (0,05 ml)",
            tabelaNutricional = listOf(
                ItemNutricional("Vitamina B12 (Metilcobalamina)", "9,9 mcg", "412%")
            ),
            restricoes = listOf("Manter o frasco protegido da luz solar direta pois a cobalamina é fotossensível."),
            perguntasFrequentes = listOf(
                PerguntaFrequente("Qual a diferença para a cianocobalamina?", "A metilcobalamina já está na forma metilada ativa, pronta para uso celular imediato.")
            ),
            seloDestaque = "Alta Biodisponibilidade",
            statusDisponibilidade = StatusDisponibilidade.DISPONIVEL.rotulo
        ),
        Produto(
            id = "prod-4",
            nome = "Ômega-3 Vegetal de Microalgas Schizochytrium (DHA 500mg + EPA 250mg)",
            categoria = CategoriaProduto.OMEGAS.rotulo,
            descricaoCurta = "Óleo puro da fonte primária marinha cultivada em biorreatores fechados livres de mercúrio.",
            descricaoCompleta = "Diferente do óleo de linhaça ou chia (que fornecem apenas ALA com baixa conversão em DHA), o Ômega-3 de Microalgas entrega DHA e EPA prontos para o cérebro, retina e sistema cardiovascular, em cápsulas softgel de amido de mandioca.",
            preco = 184.00,
            estoqueSimulado = 7,
            unidade = "60 Cápsulas Veg",
            ingredientes = listOf("Óleo de microalgas Schizochytrium sp.", "Vitamina E natural (tocoferóis)", "Cápsula vegetal de carragena e mandioca"),
            beneficios = listOf(
                "Ação anti-inflamatória sistêmica potente",
                "Memória, neuroplasticidade e saúde ocular",
                "Zero contaminação por metais pesados e sustentável"
            ),
            alergenicos = listOf("Não contém derivados de peixe ou crustáceos", "Sem glúten"),
            indicacaoUso = "Gestantes veganas, atletas em recuperação articular e suporte cognitivo de longo prazo.",
            modoUso = "Ingerir 2 cápsulas ao dia junto ao almoço ou jantar para otimizar a emulsificação lipídica.",
            composicao = "750mg de ácidos graxos ômega-3 de cadeia longa por dose (500mg DHA + 250mg EPA).",
            porcaoReferencia = "2 cápsulas (2,2g)",
            tabelaNutricional = listOf(
                ItemNutricional("Valor Energético", "18 kcal", "1%"),
                ItemNutricional("Gorduras Poli-insaturadas", "1,5 g", "8%"),
                ItemNutricional("Ácido Docosahexaenoico (DHA)", "500 mg", "**"),
                ItemNutricional("Ácido Eicosapentaenoico (EPA)", "250 mg", "**"),
                ItemNutricional("Vitamina E", "10 mg", "67%")
            ),
            restricoes = listOf("Pacientes em uso de anticoagulantes orais devem informar o médico antes de doses altas."),
            perguntasFrequentes = listOf(
                PerguntaFrequente("Deixa retrogosto residual?", "Não provoca refluxo nem odor marinho acentuado graças ao cultivo fechado e vitamina E antioxidante.")
            ),
            seloDestaque = "DHA + EPA Direto",
            statusDisponibilidade = StatusDisponibilidade.ESTOQUE_BAIXO.rotulo
        ),
        Produto(
            id = "prod-5",
            nome = "Nitro Green Pré-Treino Botânico (Beterraba, Matcha, Beta-Alanina & L-Tirosina)",
            categoria = CategoriaProduto.PERFORMANCE.rotulo,
            descricaoCurta = "Vasodilatação por nitratos naturais da beterraba + energia limpa sem taquicardia.",
            descricaoCompleta = "Pré-treino formulado com extrato padronizado de beterraba vermelha (rico em óxido nítrico natural), cafeína natural microencapsulada do Matcha japonês com L-Teanina natural, Beta-Alanina e L-Tirosina para foco sustentado.",
            preco = 139.90,
            estoqueSimulado = 26,
            unidade = "Pote 300g",
            ingredientes = listOf("Beta-alanina", "Extrato seco de beterraba", "L-Tirosina", "Matcha cerimonial", "Gengibre em pó", "Limão desidratado"),
            beneficios = listOf(
                "Aumento do fluxo sanguíneo e entrega de oxigênio muscular",
                "Tamponamento da acidez muscular (carnosina)",
                "Energia estável sem efeito rebote ('crash')"
            ),
            alergenicos = listOf("Não contém glúten", "Sem corantes artificiais"),
            indicacaoUso = "Treinos intensos de musculação, corrida, ciclismo ou cross-training.",
            modoUso = "Misturar 10g (1 dosador) em 200ml de água fria 25 minutos antes do início do exercício.",
            composicao = "2000mg Beta-Alanina + 300mg Nitrato Vegetal + 120mg Cafeína Botânica + 500mg L-Tirosina.",
            porcaoReferencia = "10g (1 dosador)",
            tabelaNutricional = listOf(
                ItemNutricional("Valor Energético", "24 kcal", "1%"),
                ItemNutricional("Beta-Alanina", "2.000 mg", "**"),
                ItemNutricional("L-Tirosina", "500 mg", "**"),
                ItemNutricional("Cafeína Natural", "120 mg", "**")
            ),
            restricoes = listOf("Não recomendado após as 18h para indivíduos sensíveis à cafeína ou gestantes."),
            perguntasFrequentes = listOf(
                PerguntaFrequente("Causa formigamento leve?", "A parestesia leve é reação natural e inofensiva da Beta-Alanina, desaparecendo em 20 minutos.")
            ),
            seloDestaque = "Óxido Nítrico Natural",
            statusDisponibilidade = StatusDisponibilidade.DISPONIVEL.rotulo
        ),
        Produto(
            id = "prod-6",
            nome = "Pro-Colágeno Vegan Derma (Aminoácidos Precursores + Silício Orgânico & Vit C)",
            categoria = CategoriaProduto.LONGEVIDADE.rotulo,
            descricaoCurta = "Pool exato de Glicina, Prolina e Lisina fermentadas com ácido ortosilícico e acerola.",
            descricaoCompleta = "Como não existe colágeno de origem vegetal, o Pro-Colágeno Vegan entrega os aminoácidos precursores na exata proporção do colágeno humano tipo I e III, associados aos cofatores enzimáticos obrigatórios (Vitamina C da acerola, Zinco quelato e Silício Orgânico).",
            preco = 154.00,
            estoqueSimulado = 19,
            unidade = "Lata 240g",
            ingredientes = listOf("L-Glicina", "L-Prolina", "L-Lisina", "Extrato de acerola orgânica", "Ácido ortosilícico estabilizado", "Bisglicinato de zinco"),
            beneficios = listOf(
                "Estímulo endógeno real dos fibroblastos dérmicos",
                "Fortalecimento de unhas, fios capilares e cartilagens",
                "Ação antioxidante contra radicais livres UV"
            ),
            alergenicos = listOf("Zero glúten", "Zero soja", "100% origem por fermentação vegetal"),
            indicacaoUso = "Cuidado estético integrativo, saúde articular e prevenção da flacidez em dietas plant-based.",
            modoUso = "Diluir 1 scoop (8g) em 150ml de água à noite ou longe de refeições pesadas.",
            composicao = "5g Aminoácidos Precursores + 15mg Silício + 100mg Vitamina C Natural.",
            porcaoReferencia = "8g (1 medidor)",
            tabelaNutricional = listOf(
                ItemNutricional("Valor Energético", "28 kcal", "1%"),
                ItemNutricional("Aminoácidos Precursores", "5.000 mg", "**"),
                ItemNutricional("Vitamina C Natural", "100 mg", "100%"),
                ItemNutricional("Zinco Quelato", "7 mg", "100%")
            ),
            restricoes = listOf("Uso adulto."),
            perguntasFrequentes = listOf(
                PerguntaFrequente("É melhor que colágeno bovino hidrolisado?", "Sim, pois fornece os aminoácidos livres já prontos sem depender de quebra peptídica incompleta.")
            ),
            seloDestaque = "Estética & Articulações",
            statusDisponibilidade = StatusDisponibilidade.DISPONIVEL.rotulo
        )
    )
}
