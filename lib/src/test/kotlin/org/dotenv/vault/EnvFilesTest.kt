package org.dotenv.vault

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class EnvFilesTest {

    @Test
    fun loadVaultByDefault() {
        val classUnderTest = dotenvVault()
        val decryptedValue = classUnderTest["MY_TEST_EV1"]
        assertEquals("my test ev 1", decryptedValue)
        val devVaultContent = classUnderTest.entries().find { it.key == "DOTENV_VAULT_DEVELOPMENT" }
        assertNotNull(devVaultContent)
    }

    @Test
    fun loadVaultByDefaultUsingVaultFile() {
        val classUnderTest = dotenvVault() {
            filename = ".env.vault"
        }
        val decryptedValue = classUnderTest["MY_TEST_EV1"]
        assertEquals("my test ev 1", decryptedValue)
    }

    @Test
    fun loadVaultByDefaultUsingEnvFile() {
        val classUnderTest = dotenvVault() {
            filename = "dotenvOnly/.env"
        }
        val decryptedValue = classUnderTest["MY_TEST_EV1"]
        assertEquals("my test ev 1", decryptedValue)

        val devVaultContent = classUnderTest.entries().find { it.key == "DOTENV_VAULT_DEVELOPMENT" }
        assertNull(devVaultContent)
    }
}
