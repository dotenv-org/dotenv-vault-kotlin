# dotenv-vault-kotlin

Extends the proven & trusted foundation of [dotenv-kotlin](https://github.com/cdimascio/dotenv-kotlin), with `.env.vault` file support.

<p align="center">
  <img src="https://raw.githubusercontent.com/cdimascio/dotenv-kotlin/master/assets/kotlin-dotenv-logo.png" alt="dotenv" />
</p>

## Install

### Maven
```xml
<dependency>
    <groupId>com.github.dotenv-org</groupId>
    <artifactId>dotenv-vault-kotlin</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Gradle
#### Gradle Groovy DSL
```groovy
implementation 'com.github.dotenv-org:dotenv-vault-kotlin:-SNAPSHOT'

```

#### Gradle Kotlin DSL
```kotlin

implementation("com.github.dotenv-org:dotenv-vault-kotlin:-SNAPSHOT")
```

## Usage
Use `dotenv.get("...")` instead of Java's `System.getenv(...)`. Here's [why](#faq).

Create a `.env` file in the root of your project

```dosini
# formatted as key=value
MY_ENV_VAR1=some_value
MY_EVV_VAR2=some_value
```

or with **Kotlin**

```kotlin
import org.dotenv.vault.dotenvVault

val dotenv = dotenvVault()
dotenv["MY_ENV_VAR1"]
```

## Android Usage

- Create an assets folder in `app/src/main/assets`
- Add `env.vault` *(no dot)* to the assets folder


- Configure dotenv to search `/assets` for a file with name `env.vault` and provide the key via BuildConfig

	```kotlin
	val dotenv = dotenvVault(BuildConfig.DOTENV_KEY) {
	    directory = "/assets"
	    filename = "env.vault" // instead of '.env', use 'env'
	}
	dotenv["MY_ENV_VAR1"]
	```

**Note:** The above configuration is required because dot files in `/assets` do not appear to resolve on Android. *(Seeking recommendations from the Android community on how `dotenv-kotlin` configuration should work in order to provide the best experience for Android developers)*

Alternatively, if you are using Provider `android.resource` you may specify

```
 directory = "android.resource://com.example.myapp/raw"
```

#### Add Dotenv Key 

Add `DOTENV_KEY` to `local.properties` or your build system enviroment variables.

### Add the key to your build configuration

### Add key into your build config in your gradle.properties android->build types -> debug / release
```kotlin
buildConfigField "String", "DOTENV_KEY", System.getenv("DOTENV_KEY") ?: ""
```


Or Add key into your local.properties
```kotlin
buildConfigField "String", "DOTENV_KEY", System.getenv("DOTENV_KEY") ?: ""
```
