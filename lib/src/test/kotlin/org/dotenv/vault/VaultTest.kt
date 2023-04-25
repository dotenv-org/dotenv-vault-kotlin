package org.dotenv.vault

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class VaultTest {

    @Test
    fun someLibraryMethodReturnsTrue() {
        val classUnderTest = dotenvVault() {
            ignoreIfMalformed = true
        }
        classUnderTest.printEntries()
        //assertTrue(classUnderTest.someLibraryMethod(), "someLibraryMethod should return 'true'")
    }

    @Test
    fun loadEncryptedDevelopmentVault() {
        val classUnderTest = dotenvVault() {
            ignoreIfMalformed = true
            systemProperties = false
        }
//        classUnderTest.printEntries()
        val devVaultContent = classUnderTest.entries().find { it.key == "DOTENV_VAULT_DEVELOPMENT"}
        assertNotNull(devVaultContent)
        println("found encrypted vault ${devVaultContent.key} = ${devVaultContent.value}")


        classUnderTest.printEntries()
        val decryptedValue = classUnderTest["MY_TEST_EV1"]
        assertEquals("my test ev 1", decryptedValue)
        //assertTrue(classUnderTest.someLibraryMethod(), "someLibraryMethod should return 'true'")
    }

    @Test
    fun testDecodeKeyUri() {
        val findEnvironmentVaultKey = "dotenv://:key_14968ef8b3f56cbcfcaa83197efa34dba567e715e82ee69e050258a5522100f6@dotenv.local/vault/.env.vault?environment=development"
        val keyPart = findEnvironmentVaultKey.substring(14, 14 + 64)
        println("key : ${keyPart}")
        assertEquals("14968ef8b3f56cbcfcaa83197efa34dba567e715e82ee69e050258a5522100f6", keyPart)
    }


}
