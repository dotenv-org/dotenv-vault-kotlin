package org.dotenv.vault

import io.github.cdimascio.dotenv.*
import io.github.cdimascio.dotenv.internal.DotenvParser
import org.dotenv.vault.utilities.decodeDotenvKeyFromUri
import org.dotenv.vault.utilities.decodeKeyFromUri
import java.util.*
import javax.crypto.BadPaddingException

//TODO figure out the environment that needs to be loaded from parsing the key
private const val VAULT_KEY_PREFIX = "DOTENV_VAULT_"
private const val dotenvKeyVar = "DOTENV_KEY"

class DotenvVaultAdapter(private val unencryptedDotenv: Dotenv, private val key: String? = null): Dotenv {
    private lateinit var vaultEntries: Map<String, String>
    private lateinit var vaultDotEnvEntries: List<DotenvEntry>

    init {
        decryptVolt()
    }

    fun getEnvironmentVaultKey(enviroment: String) = VAULT_KEY_PREFIX + enviroment.uppercase(Locale.getDefault())

    fun decodeEncryptedEnvVaultContent(enviroment: String): String {
        val environmentVaultKey = getEnvironmentVaultKey(enviroment)
        val devEntry = unencryptedDotenv.entries().find { it.key == environmentVaultKey }
        devEntry?.let {
            val encryptedEnvContent = devEntry.value
            println("using vault data in .env.vault for environment $enviroment = $encryptedEnvContent")
            return encryptedEnvContent
        }
        throw DotenvException("could not find encrypted vault for key ${environmentVaultKey}")
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
            throw DotenvException("unable to decrypt vault. verify that your ${dotenvKeyVar} environment variable is properly set")
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




    private fun findEnvironmentVaultKey(): String {
        val keyEntry = unencryptedDotenv.entries().find { it.key == dotenvKeyVar }
        if (keyEntry?.value != null) {
            println("found key entry in current system env vars ${keyEntry.value}")
            return keyEntry.value
        } else {
            println("cannot find key entry $dotenvKeyVar in current system env vars")
        }
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
            throw DotenvException("could not find environment key file")
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


