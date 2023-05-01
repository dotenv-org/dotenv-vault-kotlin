package org.dotenv.vault

import io.github.cdimascio.dotenv.*
import io.github.cdimascio.dotenv.internal.DotenvParser
import org.dotenv.vault.utilities.decodeDotenvKeyFromUri
import org.dotenv.vault.utilities.decodeKeyFromUri
import java.util.*
import javax.crypto.BadPaddingException


class DotenvVaultAdapter(private val unencryptedDotenv: Dotenv, private val key: String? = null): Dotenv {
    companion object {
        private const val VAULT_KEY_PREFIX = "DOTENV_VAULT_"
        private const val DOTENV_KEY_ID = "DOTENV_KEY"
    }

    private lateinit var vaultEntries: Map<String, String>
    private lateinit var vaultDotEnvEntries: List<DotenvEntry>

    init {
        decryptVolt()
    }

    private fun getEnvironmentVaultKey(enviroment: String) = VAULT_KEY_PREFIX + enviroment.uppercase(Locale.getDefault())

    private fun decodeEncryptedEnvVaultContent(enviroment: String): String {
        val environmentVaultKey = getEnvironmentVaultKey(enviroment)
        val devEntry = unencryptedDotenv.entries().find { it.key == environmentVaultKey }
        devEntry?.let {
            val encryptedEnvContent = devEntry.value
            println("using vault data in .env.vault for environment $enviroment = ${encryptedEnvContent.substring(0, 10)}...")
            return encryptedEnvContent
        }
        throw DotenvException("could not find encrypted vault for key $environmentVaultKey")
    }

    @Throws(DotenvException::class)
    fun decryptVolt() {
        val foundEnvironmentVaultKey = key ?: findEnvironmentVaultKey()
        val dotenvVaultKey = decodeDotenvKeyFromUri(foundEnvironmentVaultKey)

        val encryptedVaultContent = decodeEncryptedEnvVaultContent(dotenvVaultKey.environment)

        val dotenvFileContent = try {
            val secretKey = createKeyFromBytes(decodeKeyFromUri(dotenvVaultKey.decodeKey))
            decrypt(secretKey, Base64.getDecoder().decode(encryptedVaultContent))
        } catch (e: BadPaddingException) {
            throw DotenvException("unable to decrypt vault. verify that your ${DOTENV_KEY_ID} environment variable is properly set")
        }


        val reader = DotenvParser(
            DotenvVaultReader(dotenvFileContent),
            true,
            true
        )
        val env = reader.parse()
        val envVarsMap = env.associate { it.key to it.value }
        val vaultMap = unencryptedDotenv.entries().associate { it.key to it.value }
        vaultEntries = envVarsMap + vaultMap
        vaultDotEnvEntries = env + unencryptedDotenv.entries()
    }

    fun findEnvironmentVaultKey(): String {
        val keyEntry = unencryptedDotenv.entries().find { it.key == DOTENV_KEY_ID }
        if (keyEntry?.value != null) {
            return keyEntry.value
        } else {
            throw DotenvException("cannot find key entry $DOTENV_KEY_ID in current system env vars")
        }
    }

    /**
     * Returns the set of environment variables with values
     * @return the set of [DotenvEntry]s for all environment variables
     */
    override fun entries(): Set<DotenvEntry> {
        return vaultDotEnvEntries.toSet()
    }

    /**
     * Returns the set of [DotenvEntry]s matching the filter
     * @param filter the filter e.g. [Dotenv.Filter]
     * @return the set of [DotenvEntry]s for environment variables matching the [Dotenv.Filter]
     */
    override fun entries(filter: Dotenv.Filter): Set<DotenvEntry> =
        vaultDotEnvEntries.toSet()

    /**
     * Retrieves the value of the environment variable specified by key
     * @param key the environment variable
     * @return the value of the environment variable or null if not found
     */
    override operator fun get(key: String): String? {
        return vaultEntries[key]
    }

    /**
     * Retrieves the value of the environment variable specified by key.
     * If the key does not exist, then the default value is returned
     * @param key the environment variable
     * @param defaultValue the default value to return
     * @return the value of the environment variable or default value
     */
    override operator fun get(key: String, defaultValue: String): String =
        vaultEntries[key] ?: defaultValue
}


