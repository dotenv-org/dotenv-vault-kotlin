# dotenv-vault-kotlin

Extends the proven & trusted foundation of [dotenv-kotlin](https://github.com/cdimascio/dotenv-kotlin), with `.env.vault` file support.

<p align="center">
  <img src="https://raw.githubusercontent.com/cdimascio/dotenv-kotlin/master/assets/kotlin-dotenv-logo.png" alt="dotenv" />
</p>

## Install

### Maven
```xml
<dependency>
    <groupId>io.github.dotenv-org</groupId>
    <artifactId>dotenv-vault-kotlin</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle
#### Gradle Groovy DSL
```groovy
implementation 'io.github.dotenv-org:dotenv-vault-kotlin:0.1.0'
```

#### Gradle Kotlin DSL
```kotlin
implementation("io.github.dotenv-org:dotenv-vault-kotlin:0.1.0")
```

## Usage
Use `dotenv.get("...")` instead of Java's `System.getenv(...)`. Here's [why](#faq).

Create a `.env` file in the root of your project

```dosini
# formatted as key=value
MY_ENV_VAR1=some_value
MY_EVV_VAR2=some_value
```

With **Java**

```java
import io.github.dotenv-org.dotenv.DotenvVault;

DotenvVault dotenv = DotenvVault.load();
dotenv.get("MY_ENV_VAR1")
```

or with **Kotlin**

```kotlin
import io.github.dotenv-org.dotenv.dotenv-vault

val dotenv = dotenvVault()
dotenv["MY_ENV_VAR1"]
```

## Android Usage

- Create an assets folder
- Add `env` *(no dot)* to the assets folder.

	<img src="assets/android-dotenv.png" width="350">

- Configure dotenv to search `/assets` for a file with name `env`

	```kotlin
	val dotenv = dotenv {
	    directory = "/assets"
	    filename = "env" // instead of '.env', use 'env'
	}
	dotenv["MY_ENV_VAR1"]
	```

**Note:** The above configuration is required because dot files in `/assets` do not appear to resolve on Android. *(Seeking recommendations from the Android community on how `dotenv-kotlin` configuration should work in order to provide the best experience for Android developers)*

Alternatively, if you are using Provider `android.resource` you may specify

```
 directory = "android.resource://com.example.dimascio.myapp/raw"
```
