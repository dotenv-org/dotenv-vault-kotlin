package org.dotenv.vault

import io.github.cdimascio.dotenv.Configuration
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvEntry
import io.github.cdimascio.dotenv.DotenvException
import org.dotenv.vault.DotenvVault.Companion.loadVault
import java.util.logging.Logger

class DotenvVault(val dotenv: DotenvVaultAdapter) : Dotenv {
    companion object {
        private val LOG: Logger = Logger.getLogger(this::class.java.simpleName)

        fun loadVault(key: String?, config: Configuration): Dotenv {
            try {
                val loadedEnvFile = initializeDotenvWithConfig(config)
                val vaultAdapter = DotenvVaultAdapter(loadedEnvFile, key)
                return DotenvVault(vaultAdapter)
            } catch (e: DotenvException) {
                LOG.fine("unable to load encrypted vault file. trying with .env file.")
            }
            return loadUnencryptedEnvFile(config)
        }

        private fun initializeDotenvWithConfig(config: Configuration): Dotenv {
            val filename = ".env.vault"
            return if (config.filename == ".env") { // when default filename is used (when it's omitted ) try to load .env.vault file
                initializeDotenvWithConfig(filename, config.directory)
            } else {
                initializeDotenvWithConfig(config.filename, config.directory)
            }
        }

        private fun loadUnencryptedEnvFile(config: Configuration): Dotenv {
            val dotenv = Dotenv
                .configure()
                .directory(config.directory)
                .filename(config.filename)

            if (config.ignoreIfMalformed) dotenv.ignoreIfMalformed()
            if (config.ignoreIfMissing) dotenv.ignoreIfMissing()
            if (config.systemProperties) dotenv.systemProperties()
            return dotenv.load()
        }


        private fun initializeDotenvWithConfig(filename: String, directory: String): Dotenv {
            val dotenv = Dotenv
                .configure()
                .directory(directory)
                .filename(filename)
            return dotenv.load()
        }
    }

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

fun dotenvVault(key: String? = null, block: Configuration.() -> Unit = {}): Dotenv {
    val config = Configuration()
    block(config)
    return loadVault(key, config)
}