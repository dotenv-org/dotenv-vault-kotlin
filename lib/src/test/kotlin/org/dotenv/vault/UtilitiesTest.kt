package org.dotenv.vault

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvException
import org.dotenv.vault.utilities.decodeDotenvKeyFromUri
import org.dotenv.vault.utilities.printEntries
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.test.Test
import kotlin.test.assertEquals

private const val validAAAKey = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

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
        assertEquals(validAAAKey, decodedUri.decodeKey)
    }

    @Test
    fun verifyUrlDecodingWithDifferentPasswordPrefixURI() {
        val uriWithNoEnv = "dotenv://:local_aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@dotenv.local/vault/.env_test.vault?environment=development"
        val decodedUri = decodeDotenvKeyFromUri(uriWithNoEnv)
        println(decodedUri)
        assertEquals(validAAAKey, decodedUri.decodeKey)
        assertEquals("development", decodedUri.environment)
    }

    @Test
    fun verifyUrlDecodingPasswordAndEnvironmentURI() {
        val uriWithNoEnv = "dotenv://:key_aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@dotenv.local/vault/.env_test.vault?environment=development"
        val decodedUri = decodeDotenvKeyFromUri(uriWithNoEnv)
        println(decodedUri)
        assertEquals(validAAAKey, decodedUri.decodeKey)
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

