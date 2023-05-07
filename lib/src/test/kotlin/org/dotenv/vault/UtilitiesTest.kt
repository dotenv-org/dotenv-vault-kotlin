package org.dotenv.vault

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvException
import org.dotenv.vault.utilities.decodeDotenvKeyFromUri
import org.dotenv.vault.utilities.printEntries
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.test.Test
import kotlin.test.assertEquals

class UtilitiesTest {

    @Test(expected = DotenvException::class)
    fun verifyUrlDecodingWithInvalidURI() {
        val badUri = "http://localhost"
        val decodedUri = decodeDotenvKeyFromUri(badUri)
        println(decodedUri)
    }

    @Test(expected = DotenvException::class)
    fun verifyUrlDecodingWithInvalidKeyURI() {
        val uriWithNoEnv = "dotenv://:key_aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@dotenv.local/vault/.env.vault?test=test"
        val decodedUri = decodeDotenvKeyFromUri(uriWithNoEnv)
        println(decodedUri)
    }

    @Test
    fun verifyUrlDecodingWithValidPasswordURI() {
        val uriWithNoEnv = "dotenv://:key_aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@dotenv.local/vault/.env_test.vault?environment=development"
        val decodedUri = decodeDotenvKeyFromUri(uriWithNoEnv)
        println(decodedUri)
        assertEquals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", decodedUri.decodeKey)
    }

    @Test
    fun verifyUrlDecodingWithDifferentPasswordPrefixURI() {
        //valid dotenv://:key_14968ef8b3f56cbcfcaa83197efa34dba567e715e82ee69e050258a5522100f6@dotenv.local/vault/.env_test.vault?environment=development
        val uriWithNoEnv = "dotenv://:local_aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@dotenv.local/vault/.env_test.vault?environment=development"
        val decodedUri = decodeDotenvKeyFromUri(uriWithNoEnv)
        println(decodedUri)
        assertEquals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", decodedUri.decodeKey)
    }

    @Test
    fun verifyUrlDecodingWithShortPasswordURI() {
        //valid dotenv://:key_14968ef8b3f56cbcfcaa83197efa34dba567e715e82ee69e050258a5522100f6@dotenv.local/vault/.env_test.vault?environment=development
        val uriWithNoEnv = "dotenv://:key_aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@dotenv.local/vault/.env_test.vault?environment=development"
        val decodedUri = decodeDotenvKeyFromUri(uriWithNoEnv)
        println(decodedUri)
    }

    @Test(expected = DotenvException::class)
    fun verifyAdapterMainClass() {
        val mock = mock<Dotenv> {
            on { entries() } doReturn emptySet()
        }
        val adapter = DotenvVaultAdapter(mock)
        println(adapter.printEntries())
    }

}

