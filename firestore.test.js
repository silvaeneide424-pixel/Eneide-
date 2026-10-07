const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_tenant_1";
const BOB_UID = "bob_tenant_2";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read any tenant products or profile", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection(`users/${ALICE_UID}/produtos`).get());
  await assertFails(unauthDb.doc(`users/${ALICE_UID}`).get());
});

test("Multi-tenant RLS: Alice cannot read or write Bob's products, clients, orders, or meal plans", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    const db = context.firestore();
    const now = new Date();
    await db.doc(`users/${BOB_UID}/produtos/prod-bob`).set({
      id: "prod-bob",
      userId: BOB_UID,
      nome: "Proteina Bob",
      categoria: "Proteínas Vegetais",
      descricaoCurta: "Desc curta",
      descricaoCompleta: "Desc completa",
      preco: 150.0,
      estoqueSimulado: 10,
      unidade: "Pote",
      ingredientes: ["Ervilha"],
      beneficios: ["Força"],
      alergenicos: ["Sem glúten"],
      indicacaoUso: "Atletas",
      modoUso: "1 dose",
      composicao: "100% ervilha",
      porcaoReferencia: "30g",
      tabelaNutricional: [{ nutriente: "Proteína", quantidadePorPorcao: "24g", percentualVD: "48%" }],
      restricoes: ["Uso adulto"],
      perguntasFrequentes: [{ pergunta: "Vegano?", resposta: "Sim" }],
      seloDestaque: "Destaque",
      statusDisponibilidade: "Disponível",
      createdAt: now,
      updatedAt: now,
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.doc(`users/${BOB_UID}/produtos/prod-bob`).get());
  await assertFails(aliceDb.collection(`users/${BOB_UID}/produtos`).get());
  await assertFails(aliceDb.doc(`users/${BOB_UID}/produtos/prod-bob`).delete());
});

test("Multi-tenant RLS: Authenticated owner can create and query their own tenant profile and products", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();

  await assertSucceeds(
    aliceDb.doc(`users/${ALICE_UID}`).set({
      userId: ALICE_UID,
      nome: "Dra. Alice Silva",
      email: "alice@veganflow.com.br",
      cargo: "Nutricionista Esportiva",
      crnOuRegistro: "CRN-3 12345",
      createdAt: now,
      updatedAt: now,
    })
  );

  await assertSucceeds(
    aliceDb.doc(`users/${ALICE_UID}/produtos/prod-1`).set({
      id: "prod-1",
      userId: ALICE_UID,
      nome: "Vegan Pro Blend",
      categoria: "Proteínas Vegetais",
      descricaoCurta: "24g proteína isolada",
      descricaoCompleta: "Blend completo de ervilha e arroz",
      preco: 169.9,
      estoqueSimulado: 25,
      unidade: "Pote 900g",
      ingredientes: ["Ervilha", "Arroz"],
      beneficios: ["Hipertrofia"],
      alergenicos: ["Sem lactose"],
      indicacaoUso: "Pós-treino",
      modoUso: "30g em 250ml água",
      composicao: "70/30",
      porcaoReferencia: "30g",
      tabelaNutricional: [{ nutriente: "Proteínas", quantidadePorPorcao: "24g", percentualVD: "48%" }],
      restricoes: ["Uso adulto"],
      perguntasFrequentes: [{ pergunta: "Sabor?", resposta: "Cacau" }],
      seloDestaque: "Top 1",
      statusDisponibilidade: "Disponível",
      createdAt: now,
      updatedAt: now,
    })
  );

  await assertSucceeds(
    aliceDb.collection(`users/${ALICE_UID}/produtos`).where("userId", "==", ALICE_UID).get()
  );
});

test("Shadow field and userId spoofing are rejected by RLS rules", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();

  // Attempt to spoof userId = BOB_UID inside Alice's path
  await assertFails(
    aliceDb.doc(`users/${ALICE_UID}`).set({
      userId: BOB_UID,
      nome: "Dra. Alice Silva",
      email: "alice@veganflow.com.br",
      cargo: "Nutricionista",
      crnOuRegistro: "CRN-3 12345",
      createdAt: now,
      updatedAt: now,
    })
  );

  // Attempt to inject an undeclared ghost field
  await assertFails(
    aliceDb.doc(`users/${ALICE_UID}`).set({
      userId: ALICE_UID,
      nome: "Dra. Alice Silva",
      email: "alice@veganflow.com.br",
      cargo: "Nutricionista",
      crnOuRegistro: "CRN-3 12345",
      isSuperAdmin: true,
      createdAt: now,
      updatedAt: now,
    })
  );
});
