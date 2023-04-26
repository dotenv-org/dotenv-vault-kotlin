package org.dotenv.vault

import io.github.cdimascio.dotenv.*
import io.github.cdimascio.dotenv.internal.DotenvParser
import org.dotenv.vault.utilities.decodeKeyFromUri
import java.util.Base64

private const val DEVELOPMENT_VAULT_KEY = "DOTENV_VAULT_DEVELOPMENT"

class DotenvVaultAdapter(private val unencryptedDotenv: Dotenv, private val key: String? = null): Dotenv {
    private lateinit var vaultEntries: Map<String, String>
    private lateinit var vaultDotEnvEntries: List<DotenvEntry>

    init {
        decryptVolt()
    }

    fun decodeEncryptedEnvVaultContent(): String {
        // TODO QUESTION how to tell which of the environments to load if there's many present
        val devEntry = unencryptedDotenv.entries().find { it.key == DEVELOPMENT_VAULT_KEY }
        devEntry?.let {
            val encryptedEnvContent = devEntry.value
            println("found environment vault in .env.vault: $encryptedEnvContent")
            return encryptedEnvContent
        }
        throw DotenvException("could not find encrypted vault")
    }

    @Throws(DotenvException::class)
    fun decryptVolt() {
        val foundEnvironmentVaultKey = key ?: findEnvironmentVaultKey()
        val encryptedVaultContent = decodeEncryptedEnvVaultContent()
        val dotenvVaultKey = decodeKeyFromUri(foundEnvironmentVaultKey)

        val secretKey = createKeyFromBytes(dotenvVaultKey)
        val dotenvFileContent =
            decrypt(secretKey, Base64.getDecoder().decode(encryptedVaultContent))
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


    private fun findEnvironmentVaultKey(): String {
        val keyEntry = unencryptedDotenv.entries().find { it.key == "DOTENV_KEY_DEVELOPMENT" }
        if (keyEntry?.value != null) {
            println("found key entry in current system env vars ${keyEntry.value}")
            return keyEntry.value
        }

        println("cannot find key entry in current system env vars")
        val decodedKeyEntryFromFile = decodeEncryptedEnvVaultKeyFile()
        return decodedKeyEntryFromFile
    }

    private fun decodeEncryptedEnvVaultKeyFile(): String {
        try {
            val dotenvKeys = Dotenv
                .configure()
                .filename(".env.keys")

            val delegate = dotenvKeys.load()
            val keyEntry = delegate.entries().find { it.key == "DOTENV_KEY_DEVELOPMENT" }
            return keyEntry?.value ?: throw DotenvException("could not find environment key")
        } catch (e: DotenvException) {
            throw DotenvException("could not find environment key")
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


