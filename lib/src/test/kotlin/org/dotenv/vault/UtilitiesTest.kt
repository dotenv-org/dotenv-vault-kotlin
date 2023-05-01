package org.dotenv.vault

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvException
import org.dotenv.vault.utilities.decodeDotenvKeyFromUri
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.test.Test

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
}

