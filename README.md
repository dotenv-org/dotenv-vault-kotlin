# dotenv-vault-kotlin [![Gem Version](https://badge.fury.io/rb/dotenv-vault.svg)](https://badge.fury.io/rb/dotenv-vault)

<img src="https://raw.githubusercontent.com/motdotla/dotenv/master/dotenv.svg" alt="dotenv-vault" align="right" width="200" />

Extends the proven & trusted foundation of [dotenv-kotlin](https://github.com/cdimascio/dotenv-kotlin), with `.env.vault` file support.

The extended standard lets you load encrypted secrets from your `.env.vault` file in production (and other) environments. Brought to you by the same people that pioneered [dotenv-nodejs](https://github.com/motdotla/dotenv).

* [🌱 Install](#-install)
* [🏗️ Usage (.env)](#%EF%B8%8F-usage)
* [🚀 Deploying (.env.vault) 🆕](#-deploying)
* [🌴 Multiple Environments](#-manage-multiple-environments)
* [ ✔ Examples](#-examples)
* [❓ FAQ](#-faq)
* [⏱️ Changelog](./CHANGELOG.md)

## 🌱 Install

### Add repository

Add jitpack repository

```groovy
repositories {
	maven { url 'https://jitpack.io' }
}
```

### Add dependency
#### Gradle Groovy DSL
```groovy
implementation 'com.github.dotenv-org:dotenv-vault-kotlin:-SNAPSHOT'

```

#### Gradle Kotlin DSL
```kotlin

implementation("com.github.dotenv-org:dotenv-vault-kotlin:-SNAPSHOT")

```



## 🚀 Deploying

Encrypt your environment settings by doing:

```shell
npx dotenv-vault local build
```

This will create an encrypted `.env.vault` file along with a `.env.keys` file containing the encryption keys. 
Set the `DOTENV_KEY` environment variable by copying and pasting the key value from the `.env.keys` file onto your server or cloud provider environment variables.

Commit your .env.vault file safely to code and deploy. Your .env.vault fill be decrypted on boot, its environment variables injected, and your app work as expected.

Note that when the `DOTENV_KEY` environment variable is set, environment settings will *always* be loaded from the `.env.vault` file. 
For development use, you can leave the `DOTENV_KEY` environment variable unset and fall back on the `dotenv-kotlin` behaviour of loading from `.env` (see [here in the `dotenv` README](https://github.com/cdimascio/dotenv-kotlin#usage) for the details).

## 🌴 Manage Multiple Environments

You have two options for managing multiple environments - locally managed or vault managed - both use [dotenv-vault](https://github.com/dotenv-org/dotenv-vault).

#### 💻 Locally Managed

Create a `.env.production` file in the root of your project and put your production values there.

```shell
# .env.production
MY_ENV_VAR1="some_value"
MY_EVV_VAR2="some_value"
```

Rebuild your `.env.vault` file.

```shell
npx dotenv-vault local build
```

View your `.env.keys` file. There is a production `DOTENV_KEY` that pairs with the `DOTENV_VAULT_PRODUCTION` cipher in your `.env.vault` file.

Set the production `DOTENV_KEY` enviorment variable with the value of your selected enviroment, recommit your `.env.vault` file to code, and deploy. That's it!

Your .env.vault fill be decrypted on boot, its production environment variables injected, and your app work as expected.

## 🏗️ Usage

Development usage works just like [dotenv-kotlin](https://github.com/cdimascio/dotenv-kotlin). So if you don't add your `.env.vault` file or set the `DOTENV_KEY` the library will fallback to reading your `.env` file.

#### Kotlin
```kotlin
import org.dotenv.vault.dotenvVault

val dotenv = dotenvVault()
dotenv["MY_ENV_VAR1"]
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
  dotenv["MY_ENV_VAR1"]
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

## 🚀 Examples
* [Android example app](https://github.com/marcel-rf/DotenvVaultAndroidExample)
* Kotlin example app
* [Kotlin tests](https://github.com/dotenv-org/dotenv-vault-kotlin/blob/master/lib/src/test/kotlin/org/dotenv/vault/VaultTest.kt#L11)

## ❓ FAQ

#### What happens if `DOTENV_KEY` is not set?

Dotenv Vault gracefully falls back to [dotenv](https://github.com/bkeepers/dotenv) when `DOTENV_KEY` is not set. This is the default for development so that you can focus on editing your `.env` file and save the `build` command until you are ready to deploy those environment variables changes.

#### Should I commit my `.env` file?

No. We **strongly** recommend against committing your `.env` file to version control. It should only include environment-specific values such as database passwords or API keys. Your production database should have a different password than your development database.

#### Should I commit my `.env.vault` file?

Yes. It is safe and recommended to do so. It contains your encrypted envs, and your vault identifier.

#### Can I share the `DOTENV_KEY`?

No. It is the key that unlocks your encrypted environment variables. Be very careful who you share this key with. Do not let it leak.

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

## Changelog

See [CHANGELOG.md](CHANGELOG.md)

## License

MIT