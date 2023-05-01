package org.dotenv.vault

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MultipleEnvironmentsTest {

    @Test
    fun loadVaultForProductionEnvironmentLoadsProdValuesForVariable() {
        val classUnderTest = dotenvVault(key = "dotenv://:key_781d8ba55cdd0c1b88137cf531194c170b7a61f62e669fb52c153dafbdff2c24@dotenv.local/vault/.env.vault?environment=production") {
            directory = "multiEnvironments"
        }
        val decryptedValue = classUnderTest["MY_TEST_EV1"]
        assertEquals("my prod ev 1", decryptedValue)
        val prodVaultContent = classUnderTest.entries().find { it.key == "DOTENV_VAULT_PRODUCTION" }
        assertNotNull(prodVaultContent)
        val devVaultContent = classUnderTest.entries().find { it.key == "DOTENV_VAULT_DEVELOPMENT" }
        assertNotNull(devVaultContent)
    }

    @Test
    fun loadVaultForCustomEnvironmentLoadsCorrectValuesForVariable() {
        val classUnderTest = dotenvVault(key = "dotenv://:key_b2e7ae6abd29f1683ef59e2b3c7cd0d504889c904bc87b22b27fd18bfa95b959@dotenv.local/vault/.env.vault?environment=customEnv") {
            directory = "multiEnvironments"
        }
        val decryptedValue = classUnderTest["MY_TEST_EV1"]
        assertEquals("my custom ev 1", decryptedValue)
        val prodVaultContent = classUnderTest.entries().find { it.key == "DOTENV_VAULT_CUSTOMENV" }
        assertNotNull(prodVaultContent)
    }
}
