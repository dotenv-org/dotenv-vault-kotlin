# dotenv-vault-kotlin ![Release](https://jitpack.io/v/dotenv-org/dotenv-vault-kotlin.svg) ![](https://img.shields.io/badge/license-Apache%202.0-orange.svg)

<img src="https://raw.githubusercontent.com/motdotla/dotenv/master/dotenv.svg" alt="dotenv-vault" align="right" width="200" />

Extends the proven & trusted foundation of [dotenv-kotlin](https://github.com/cdimascio/dotenv-kotlin), with `.env.vault` file support.

The extended standard lets you load encrypted secrets from your `.env.vault` file in production (and other) environments. 

* [🌱 Install](#-install)
* [🏗️ Usage (.env)](#%EF%B8%8F-usage)
* [🚀 Deploying (.env.vault) 🆕](#-deploying)
* [🌴 Multiple Environments](#-manage-multiple-environments)
* [📚 Examples](#-examples)
* [❓ FAQ](#-faq)
* [⏱️ Changelog](./CHANGELOG.md)

## 🌱 Install

Add jitpack repository to your `build.gradle` or `builde.gradle.kts` and require the `com.github.dotenv-org:dotenv-vault-kotlin:x.x.x` implementation dependency.

```groovy
// build.gradle
...
repositories {
    ...
    maven { url 'https://jitpack.io' }
}

dependencies {
    ...
    implementation 'com.github.dotenv-org:dotenv-vault-kotlin:0.0.2'
}
```

or

```kotlin
// build.gradle.kts
...
repositories {
    ...
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    ...
    implementation("com.github.dotenv-org:dotenv-vault-kotlin:0.0.2")
}
```

## 🏗️ Usage

Development usage works just like [dotenv-kotlin](https://github.com/cdimascio/dotenv-kotlin). 

Add your application configuration to your `.env` file in the root of your project:

```
S3_BUCKET=YOURS3BUCKET
SECRET_KEY=YOURSECRETKEYGOESHERE
```

As early as possible in your application code, load .env:

#### Kotlin

```kotlin
import org.dotenv.vault.dotenvVault

val dotenv = dotenvVault()
dotenv["S3_BUCKET"]
```

#### Android

- Create an assets folder in `app/src/main/assets`
- Add `env.vault` *(no dot)* to the assets folder


- Configure dotenv to search `/assets` for a file with name `env.vault` and provide the key via BuildConfig

  ```kotlin
  val dotenv = dotenvVault(BuildConfig.DOTENV_KEY) {
      directory = "/assets"
      filename = "env.vault" // instead of '.env', use 'env'
  }
  dotenv["S3_BUCKET"]
  ```

**Note:** The above configuration is required because dot files in `/assets` do not appear to resolve on Android. *(Seeking recommendations from the Android community on how `dotenv-kotlin` configuration should work in order to provide the best experience for Android developers)*

Alternatively, if you are using Provider `android.resource` you may specify

```
 directory = "android.resource://com.example.myapp/raw"
```

##### Add Dotenv Key

Add `DOTENV_KEY` to your build system environment variables.

Add key into your build config in your build.grade inside the android->built types
```kotlin
android {
    buildTypes {
        debug {
            buildConfigField "String", "DOTENV_KEY", System.getenv("DOTENV_KEY") ?: ""    
        }
    } 
}

```

## 🚀 Deploying

[Install](https://www.dotenv.org/install) dotenv-vault.

```
See install instructions at https://www.dotenv.org/install
```

Then encrypt your environment variables by doing:

```shell
dotenv-vault local build
```

This will create an encrypted `.env.vault` file along with a `.env.keys` file containing the encryption keys
Set the `DOTENV_KEY` environment variable by copying and pasting the key value from the `.env.keys` file onto your server or cloud provider. For example in heroku:

```
heroku config:set DOTENV_KEY=<key string from .env.keys>
```

Commit your .env.vault file safely to code and deploy. Your .env.vault fill be decrypted on boot, its environment variables injected, and your app work as expected.

## 🌴 Manage Multiple Environments

You have two options for managing multiple environments - locally managed or vault managed - both use <a href="https://github.com/dotenv-org/dotenv-vault">dotenv-vault</a>.

Locally managed never makes a remote API call. It is completely managed on your machine. Vault managed adds conveniences like backing up your .env file, secure sharing across your team, access permissions, and version history. Choose what works best for you.

#### 💻 Locally Managed

Create a `.env.production` file in the root of your project and put your production values there.

```
# .env.production
S3_BUCKET="PRODUCTION_S3BUCKET"
SECRET_KEY="PRODUCTION_SECRETKEYGOESHERE"
```

Rebuild your `.env.vault` file.

```bash
$ dotenv-vault local build
```

Check your `.env.keys` file. There is a production `DOTENV_KEY` that coincides with the additional `DOTENV_VAULT_PRODUCTION` cipher in your `.env.vault` file.

Set the production `DOTENV_KEY` on your server, recommit your `.env.vault` file to code, and deploy. That's it!

#### 🔐 Vault Managed

Sync your .env file. Run the push command and follow the instructions. [learn more](/docs/sync/quickstart)

```bash
$ dotenv-vault push
```

Manage multiple environments with the included UI. [learn more](/docs/tutorials/environments)

```bash
$ dotenv-vault open
```

Build your `.env.vault` file with multiple environments.

```bash
$ dotenv-vault build
```

Access your `DOTENV_KEY`.

```bash
$ dotenv-vault keys
```

Set the production `DOTENV_KEY` on your server, recommit your `.env.vault` file to code, and deploy. That's it!

## 📚 Examples

* [Android example app](https://github.com/marcel-rf/DotenvVaultAndroidExample)
* [Kotlin example app](https://github.com/dotenv-org/hello-kotlin)
* [Kotlin tests](https://github.com/dotenv-org/dotenv-vault-kotlin/blob/master/lib/src/test/kotlin/org/dotenv/vault/VaultTest.kt#L11)

## ❓ FAQ

#### What happens if `DOTENV_KEY` is not set?

Dotenv Vault gracefully falls back to [dotenv-kotlin](https://github.com/cdimascio/dotenv-kotlin) when `DOTENV_KEY` is not set. This is the default for development so that you can focus on editing your `.env` file and save the `build` command until you are ready to deploy those environment variables changes.

#### Should I commit my `.env` file?

No. We **strongly** recommend against committing your `.env` file to version control. It should only include environment-specific values such as database passwords or API keys. Your production database should have a different password than your development database.

#### Should I commit my `.env.vault` file?

Yes. It is safe and recommended to do so. It contains your encrypted envs, and your vault identifier.

#### Can I share the `DOTENV_KEY`?

No. It is the key that unlocks your encrypted environment variables. Be very careful who you share this key with. Do not let it leak.

## Changelog

See [CHANGELOG.md](CHANGELOG.md)

## License

Apache License 2.0
