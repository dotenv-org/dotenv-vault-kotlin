package org.dotenv.vault

import io.github.cdimascio.dotenv.Dotenv
import org.dotenv.vault.utilities.printEntries
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class VaultTest {

    @Test
    fun loadEncryptedDevelopmentVaultEntries() {
        val classUnderTest = dotenvVault() {
        }
        val decryptedValue = classUnderTest.get("MY_TEST_EV1", "")
        assertEquals("my test ev 1", decryptedValue)

        val decryptedNotExisting = classUnderTest.get("NOT_EXISTENT_KEY", "")
        assertEquals("", decryptedNotExisting)

        val declaredInFile = classUnderTest.entries(Dotenv.Filter.DECLARED_IN_ENV_FILE)
        assertNotNull(declaredInFile)
        val itemFound = declaredInFile.find { it.key == "MY_TEST_EV1" }
        assertNotNull(itemFound)
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

