package org.dotenv.vault

import org.dotenv.vault.utilities.decodeDotenvKeyFromUri
import org.dotenv.vault.utilities.findParameterValue
import org.dotenv.vault.utilities.findPasswordValue
import org.dotenv.vault.utilities.printEntries
import java.net.URI
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DotenvVaultKeyTest {

    @Test
    fun `given url when parsed with uri class returns user & environment parameters`() {
        val dotenvKeyUri = "dotenv://:key_14968ef8b3f56cbcfcaa83197efa34dba567e715e82ee69e050258a5522100f6@dotenv.local/vault/.env.vault?environment=development"
        val uri = URI(dotenvKeyUri)
        assertEquals("dotenv", uri.scheme)
        assertEquals("dotenv.local", uri.host)
        val password = uri.findPasswordValue()
        assertNotNull(password)
        assertTrue { password.startsWith("key_") }
        val environmentValue = uri.findParameterValue("environment")
        assertEquals("development", environmentValue)
    }

    @Test
    fun testDecodeKeyUri() {
        val vaultKeyValue = "dotenv://:key_14968ef8b3f56cbcfcaa83197efa34dba567e715e82ee69e050258a5522100f6@dotenv.local/vault/.env.vault?environment=development"
        val decodedKey = decodeDotenvKeyFromUri(vaultKeyValue)
        assertEquals("14968ef8b3f56cbcfcaa83197efa34dba567e715e82ee69e050258a5522100f6", decodedKey.decodeKey)
        assertEquals("development", decodedKey.environment)
    }

}

