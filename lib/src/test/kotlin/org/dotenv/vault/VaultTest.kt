package org.dotenv.vault

import org.dotenv.vault.utilities.printEntries
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class VaultTest {

    @Test
    fun loadEncryptedDevelopmentVault() {
        val classUnderTest = dotenvVault() {
            ignoreIfMalformed = true
            systemProperties = false
        }
        val devVaultContent = classUnderTest.entries().find { it.key == "DOTENV_VAULT_DEVELOPMENT"}
        assertNotNull(devVaultContent)
        println("found encrypted vault ${devVaultContent.key} = ${devVaultContent.value}")

        classUnderTest.printEntries()
        val decryptedValue = classUnderTest["MY_TEST_EV1"]
        assertEquals("my test ev 1", decryptedValue)
    }

    @Test
    fun loadEncryptedDevelopmentVaultWithProvidedKey() {
        val vaultKey = "dotenv://:key_14968ef8b3f56cbcfcaa83197efa34dba567e715e82ee69e050258a5522100f6@dotenv.local/vault/.env.vault?environment=development"
        val classUnderTest = dotenvVault(key = vaultKey) {
            ignoreIfMalformed = true
            systemProperties = false
        }
        val devVaultContent = classUnderTest.entries().find { it.key == "DOTENV_VAULT_DEVELOPMENT"}
        assertNotNull(devVaultContent)
        println("found encrypted vault ${devVaultContent.key} = ${devVaultContent.value}")
        classUnderTest.printEntries()
        val decryptedValue = classUnderTest["MY_TEST_EV1"]
        assertEquals("my test ev 1", decryptedValue)
    }


    @Test
    fun loadEncryptedProductionDevelopmentVaultWithWrongKeyFallsbackToEnvFile() {
        val classUnderTest = dotenvVault() {
            ignoreIfMalformed = true
            systemProperties = false
            directory = "multiEnvironments"
        }
        classUnderTest.printEntries()
        val decryptedValue = classUnderTest["MY_TEST_EV1"]
        assertEquals("my test ev 1", decryptedValue)
    }
}

