package org.dotenv.vault.utilities

fun getKeyPartFromKeyURI(findEnvironmentVaultKey: String): String {
    val startIndex = findEnvironmentVaultKey.indexOf("key_") + 4
    return findEnvironmentVaultKey.substring(startIndex, startIndex + 64)
}

fun decodeKeyFromUri(findEnvironmentVaultKey: String): ByteArray {
    val keyPart = getKeyPartFromKeyURI(findEnvironmentVaultKey)
    println("decoding key $keyPart from hex string")
    return keyPart.fromHexString()
}

fun String.fromHexString() = this.chunked(2).map { it.toInt(16).toByte() }.toByteArray()