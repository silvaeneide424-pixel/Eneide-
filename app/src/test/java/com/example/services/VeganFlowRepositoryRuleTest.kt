package com.example.services

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import java.util.UUID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class VeganFlowRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun salvarProduto_authenticatedOwner_createsAndObservesInTenant() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val dataService = DataService(firestore, auth)
        val produtoBase = DadosSimuladosProdutos.obterProdutosIniciais().first().copy(
            id = "prod_${UUID.randomUUID()}"
        )

        val saveResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            dataService.salvarProduto(produtoBase, ehEdicao = false)
        }
        assertTrue(saveResult.isSuccess)

        val emittedProducts = withTimeout(FLOW_TIMEOUT_MS) {
            dataService.observarProdutos(aliceUid).first { list -> list.any { it.id == produtoBase.id } }
        }
        assertTrue(emittedProducts.any { it.id == produtoBase.id })
    }

    @Test
    fun obterProdutoDeTenantPorId_crossUserAccess_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val aliceService = DataService(firestore, auth)
        val produtoAlice = DadosSimuladosProdutos.obterProdutosIniciais().first().copy(
            id = "prod_alice_${UUID.randomUUID()}"
        )
        withTimeout(DEFAULT_TIMEOUT_MS) {
            aliceService.salvarProduto(produtoAlice, ehEdicao = false).getOrThrow()
        }

        signInTestUser(BOB_EMAIL)
        val bobService = DataService(firestore, auth)
        try {
            withTimeout(DEFAULT_TIMEOUT_MS) {
                bobService.obterProdutoDeTenantPorId(aliceUid, produtoAlice.id).getOrThrow()
            }
            fail("Expected FirebaseFirestoreException PERMISSION_DENIED for cross-tenant read")
        } catch (e: FirebaseFirestoreException) {
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, e.code)
        }
    }

    @Test
    fun observarProdutos_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        auth.signOut()
        val unauthService = DataService(firestore, auth)

        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                unauthService.observarProdutos(aliceUid).first()
            }
            fail("Expected FirebaseFirestoreException PERMISSION_DENIED for unauthenticated caller")
        } catch (e: Throwable) {
            val firestoreEx = generateSequence(e) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx?.code)
        }
    }

    private companion object {
        const val ALICE_EMAIL = "alice.tenant@veganflow.com.br"
        const val BOB_EMAIL = "bob.tenant@veganflow.com.br"
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 3000L
    }
}
