package org.dotenv.vault

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvException
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.test.Test

class VaultAdapterTest {

    @Test(expected = DotenvException::class)
    fun verifyVaultAdapterWhenNoKeyFound() {
        val mock = mock<Dotenv> {
            on { entries() } doReturn emptySet()
        }
        val classUnderTest = DotenvVaultAdapter(mock)

        classUnderTest.decryptVolt()
    }
}

