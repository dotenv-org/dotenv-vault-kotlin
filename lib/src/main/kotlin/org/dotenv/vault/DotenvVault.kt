package org.dotenv.vault

import io.github.cdimascio.dotenv.Configuration
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvEntry

class DotenvVault(private val dotenvDelegate: Dotenv) {
    fun printEntries() {
        for (e in dotenvDelegate.entries()) {
            println("${e.key} = ${e.value}")
        }
    }

    fun decrypt(): Boolean {
        return true
    }

    /**
     * Returns the set of environment variables with values
     * @return the set of [DotenvEntry]s for all environment variables
     */
    fun entries(): Set<DotenvEntry> = dotenvDelegate.entries()

    /**
     * Returns the set of [DotenvEntry]s matching the filter
     * @param filter the filter e.g. [Dotenv.Filter]
     * @return the set of [DotenvEntry]s for environment variables matching the [Dotenv.Filter]
     */
    fun entries(filter: Dotenv.Filter): Set<DotenvEntry> = dotenvDelegate.entries(filter)

    /**
     * Retrieves the value of the environment variable specified by key
     * @param key the environment variable
     * @return the value of the environment variable
     */
    operator fun get(key: String): String = dotenvDelegate[key]

    /**
     * Retrieves the value of the environment variable specified by key.
     * If the key does not exist, then the default value is returned
     * @param key the environment variable
     * @param defaultValue the default value to return
     * @return the value of the environment variable or default value
     */
    operator fun get(key: String, defaultValue: String): String =
        dotenvDelegate.get(key, defaultValue)
}


fun dotenvVault(block: Configuration.() -> Unit = {}): DotenvVault {
    val config = Configuration()
    block(config)
    val dotenv = Dotenv
        .configure()
        .directory(config.directory)
        .filename(config.filename)
    if (config.ignoreIfMalformed) dotenv.ignoreIfMalformed()
    if (config.ignoreIfMissing) dotenv.ignoreIfMissing()
    if (config.systemProperties) dotenv.systemProperties()
    val delegate = dotenv.load()
    return DotenvVault(delegate)
}
