package org.dotenv.vault

import io.github.cdimascio.dotenv.*
import io.github.cdimascio.dotenv.internal.DotenvParser
import java.util.Base64

private val DEVELOPMENT_VAULT_KEY = "DOTENV_VAULT_DEVELOPMENT"

class DotenvVault(private val dotenvDelegate: Dotenv, private val key: String?) {
    private val vaultCrypto: VaultCrypto = VaultCrypto()
    private lateinit var vaultEntries: Map<String, String>
    private lateinit var vaultDotEnvEntries: List<DotenvEntry>

    companion object {
        fun getKeyPartFromKeyURI(findEnvironmentVaultKey: String): String {
            val startIndex = findEnvironmentVaultKey.indexOf("key_") + 4
            return findEnvironmentVaultKey.substring(startIndex, startIndex + 64)
        }
    }

    init {
        decryptVolt()
    }

    fun printEntries() {
        for (e in vaultEntries) {
            println("${e.key} = ${e.value}")
        }
        for (e in dotenvDelegate.entries()) {
            println("${e.key} = ${e.value}")
        }
    }

    fun decodeEncryptedEnvVaultContent(): String {
        // TODO QUESTION how to tell which of the environments to load if there's many present
        val devEntry = dotenvDelegate.entries().find { it.key == DEVELOPMENT_VAULT_KEY }
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

        val secretKey = vaultCrypto.createKeyFromBytes(dotenvVaultKey)
        val dotenvFileContent =
            vaultCrypto.decrypt(secretKey, Base64.getDecoder().decode(encryptedVaultContent))
        val reader = DotenvParser(
            DotenvVaultReader(dotenvFileContent),
            true,
            true
        )
        val env = reader.parse()
        val envVarsMap = env.associate { it.key to it.value }
        vaultEntries = envVarsMap
        vaultDotEnvEntries = env
    }


    private fun decodeKeyFromUri(findEnvironmentVaultKey: String): ByteArray {
        val keyPart = getKeyPartFromKeyURI(findEnvironmentVaultKey)
        println("decoding key $keyPart from hex string")
        return keyPart.fromHexString()
    }

    private fun findEnvironmentVaultKey(): String {
        val keyEntry = dotenvDelegate.entries().find { it.key == "DOTENV_KEY_DEVELOPMENT" }
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
    fun entries(): Set<DotenvEntry> {
        return vaultDotEnvEntries.toSet() + dotenvDelegate.entries()
    }

    /**
     * Returns the set of [DotenvEntry]s matching the filter
     * @param filter the filter e.g. [Dotenv.Filter]
     * @return the set of [DotenvEntry]s for environment variables matching the [Dotenv.Filter]
     */
    fun entries(filter: Dotenv.Filter): Set<DotenvEntry> =
        vaultDotEnvEntries.toSet() + dotenvDelegate.entries(filter)

    /**
     * Retrieves the value of the environment variable specified by key
     * @param key the environment variable
     * @return the value of the environment variable
     */
    operator fun get(key: String): String = vaultEntries[key] ?: dotenvDelegate[key]

    /**
     * Retrieves the value of the environment variable specified by key.
     * If the key does not exist, then the default value is returned
     * @param key the environment variable
     * @param defaultValue the default value to return
     * @return the value of the environment variable or default value
     */
    operator fun get(key: String, defaultValue: String): String =
        vaultEntries[key] ?: dotenvDelegate.get(key, defaultValue)
}
//
//data class VaultConfiguration(
//    val directory: String = "./",
//    val filename: String = ".env.vault",
//    val key: String?,
//    /**
//     * Do not throw an exception when .env is malformed
//     */
//    val ignoreIfMalformed: Boolean = false,
//    /**
//     * Do not throw an exception when .env is missing
//     */
//    val ignoreIfMissing: Boolean = false,
//    /**
//     * Set env vars into System properties. Enables fetch them via e.g. System.getProperty(...)
//     */
//    var systemProperties: Boolean = false,
//)

// TODO change this builder to return the same interface using DotenvVaultImpl, so it's not required to change the type
fun dotenvVault(key: String? = null, block: Configuration.() -> Unit = {}): DotenvVault {
    // TODO decrypt file and feed it to the dotenv tool
    // using public DotenvImpl(List<DotenvEntry> envVars) where envVars can be fed to the constructor
    // however the load method doesn't support it and the DotenvImpl is package private, so cannot be initialized from outside
    // go and check how the decryption works to see if it's feasible.
    val config = Configuration()
    config.filename = ".env.vault"
    block(config)
    val dotenv = Dotenv
        .configure()
        .directory(config.directory)
        .filename(config.filename)

    if (config.ignoreIfMalformed) dotenv.ignoreIfMalformed()
    if (config.ignoreIfMissing) dotenv.ignoreIfMissing()
    if (config.systemProperties) dotenv.systemProperties()
    val delegate = dotenv.load()
    return DotenvVault(delegate, key)
}
