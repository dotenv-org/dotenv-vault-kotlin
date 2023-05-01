plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
    maven(url = "https://www.jitpack.io")
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    api("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // Use mockito for tests
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "org.dotenv.vault.kotlin"
            artifactId = "dotenv-vault-kotlin"
            version = "0.0.1"
            from(components["java"])
        }
    }
}