package org.dotenv.vault.utilities

import io.github.cdimascio.dotenv.DotenvException
import java.net.URI


data class DotenvKey(
    val environment: String,
    val decodeKey: String
)

fun decodeDotenvKeyFromUri(environmentVaultKey: String): DotenvKey {
    try {
        val uri = URI(environmentVaultKey)
        val environmentValue = uri.findParameterValue("environment")
            ?: throw DotenvException("unable to determine environment from key uri")

        val password = uri.findPasswordValue() ?: throw DotenvException("unable to find key in userinfo")
        // fix: this won't always start with key_. It could start with custom_, or local_, etc. Instead get the last 64 characters. see golang example: https://github.com/dotenv-org/godotenvvault/blob/master/godotenvvault.go#L301
        return DotenvKey(environmentValue, password.removePrefix("key_"))
    } catch (e: Exception) {
        throw DotenvException("unable to find key in uri: ${environmentVaultKey}")
    }
}


fun decodeKeyFromUri(environmentVaultKey: String): ByteArray {
    return environmentVaultKey.fromHexString()
}

fun URI.findPasswordValue(): String? = if (userInfo.isEmpty()) null else userInfo.split(":").last()

fun URI.findParameterValue(parameterName: String): String? {
    return rawQuery.split('&').map {
        val parts = it.split('=')
        val name = parts.firstOrNull() ?: ""
        val value = parts.drop(1).firstOrNull() ?: ""
        Pair(name, value)
    }.firstOrNull { it.first == parameterName }?.second
}

fun String.fromHexString() = this.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
