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

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    // implementation("com.google.guava:guava:30.1.1-jre")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
//    api("org.apache.commons:commons-math3:3.6.1")

    api("io.github.cdimascio:dotenv-kotlin:6.4.1")
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