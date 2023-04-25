package org.dotenv.vault

import kotlin.test.Test

class VaultTest {

    @Test
    fun someLibraryMethodReturnsTrue() {
        val classUnderTest = dotenvVault() {
            ignoreIfMalformed = true
        }
        classUnderTest.printEntries()
        //assertTrue(classUnderTest.someLibraryMethod(), "someLibraryMethod should return 'true'")
    }
}
