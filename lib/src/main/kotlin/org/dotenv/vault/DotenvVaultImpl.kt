package org.dotenv.vault

import io.github.cdimascio.dotenv.Configuration
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvEntry
import io.github.cdimascio.dotenv.DotenvException

class DotenvVaultImpl(val dotenv: DotenvVaultAdapter) : Dotenv {
    /**
     * Returns the set of environment variables with values
     * @return the set of [DotenvEntry]s for all environment variables
     */
    override fun entries(): Set<DotenvEntry> {
        return dotenv.entries()
    }

    /**
     * Retrieves the value of the environment variable specified by key
     * @param key the environment variable
     * @return the value of the environment variable or null if not found
     */
    override operator fun get(key: String): String? {
        return dotenv[key]
    }

    override fun get(key: String, defaultValue: String): String = dotenv.get(key, defaultValue)

    /**
     * Returns the set of [DotenvEntry]s matching the filter
     * @param filter the filter e.g. [Dotenv.Filter]
     * @return the set of [DotenvEntry]s for environment variables matching the [Dotenv.Filter]
     */
    override fun entries(filter: Dotenv.Filter): Set<DotenvEntry> = dotenv.entries(filter)
}

fun loadEncryptedVault(config: Configuration): Dotenv {
    val filename = ".env.vault"
    return if (config.filename == ".env") {
        // when default filename is used (when it's omitted ) try to load .env.vault file
        // TODO default value should be removed from the Configuration class and used when omitted, so that other implementations can provide their own defaults
        initializeDotenvForFile(filename, config.directory)
    } else {
        initializeDotenvForFile(config.filename, config.directory)
    }
}

fun loadUnencryptedEnvFile(block: Configuration.() -> Unit = {}): Dotenv {
    val config = Configuration()
    block(config)
    val dotenv = Dotenv
        .configure()
        .directory(config.directory)
        .filename(config.filename)

    if (config.ignoreIfMalformed) dotenv.ignoreIfMalformed()
    if (config.ignoreIfMissing) dotenv.ignoreIfMissing()
    if (config.systemProperties) dotenv.systemProperties()
    return dotenv.load()
}


fun initializeDotenvForFile(filename: String, directory: String): Dotenv {
    val dotenv = Dotenv
        .configure()
        .directory(directory)
        .filename(filename)
    val delegate = dotenv.load()
    return delegate
}


fun dotenvVault(key: String? = null, block: Configuration.() -> Unit = {}): Dotenv {
    val config = Configuration()
    block(config)
    try {
        val loadedEnvFile = loadEncryptedVault(config)
        val vaultAdapter = DotenvVaultAdapter(loadedEnvFile, key)
        return DotenvVaultImpl(vaultAdapter)
    } catch (e: DotenvException) {
        println("unable to load encrypted vault file. falling back to .env file")
    }
    return loadUnencryptedEnvFile(block)
}


