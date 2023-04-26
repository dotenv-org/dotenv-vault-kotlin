package org.dotenv.vault

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MultipleEnvironmentsTest {

    @Test
    fun loadVaultByDefault() {
        val classUnderTest = dotenvVault() {
            directory = "multiEnvironments"
        }
        val decryptedValue = classUnderTest["MY_TEST_EV1"]
        assertEquals("my prod ev 1", decryptedValue)
        val prodVaultContent = classUnderTest.entries().find { it.key == "DOTENV_VAULT_PRODUCTION" }
        assertNotNull(prodVaultContent)
        val devVaultContent = classUnderTest.entries().find { it.key == "DOTENV_VAULT_DEVELOPMENT" }
        assertNotNull(devVaultContent)
    }
}
