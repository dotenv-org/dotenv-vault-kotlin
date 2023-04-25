package org.dotenv.vault

import kotlin.test.Test
import kotlin.test.assertTrue
//BinaryVersionedFile(file=file:///home/marcel/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-test/1.8.21/13f401560fd0e89a3a80fef78ccf3c626f945201/kotlin-test-1.8.21.jar, version=1.8.0, supportedVersion=1.5.1)
class LibraryTest {
    @Test fun someLibraryMethodReturnsTrue() {
        val classUnderTest = Library()
        assertTrue(classUnderTest.someLibraryMethod(), "someLibraryMethod should return 'true'")
    }
}
