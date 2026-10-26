<h1 align="center">Orbit Compose Multiplatform</h1>

<p align="center">
  <a href="https://central.sonatype.com/artifact/glass.yasan.orbit/ui"><img alt="version" src="https://img.shields.io/maven-central/v/glass.yasan.orbit/ui?label=version"/></a>
  <a href="LICENSE"><img alt="License" src="https://img.shields.io/github/license/yasanglass/orbit-compose-multiplatform.svg"/></a>
  <a href="https://kotlinlang.org"><img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-2.2.21-purple.svg?style=flat"/></a>
  <a href="https://github.com/yasanglass/orbit-compose-multiplatform/actions/workflows/android.yml"><img alt="android" src="https://img.shields.io/github/actions/workflow/status/yasanglass/orbit-compose-multiplatform/android.yml?label=android"/></a>
  <a href="https://github.com/yasanglass/orbit-compose-multiplatform/actions/workflows/desktop.yml"><img alt="desktop" src="https://img.shields.io/github/actions/workflow/status/yasanglass/orbit-compose-multiplatform/desktop.yml?label=desktop"/></a>
  <a href="https://github.com/yasanglass/orbit-compose-multiplatform/actions/workflows/ios.yml"><img alt="ios" src="https://img.shields.io/github/actions/workflow/status/yasanglass/orbit-compose-multiplatform/ios.yml?label=ios"/></a>
  <a href="https://github.com/yasanglass/orbit-compose-multiplatform/actions/workflows/js.yml"><img alt="js" src="https://img.shields.io/github/actions/workflow/status/yasanglass/orbit-compose-multiplatform/js.yml?label=js"/></a>
  <a href="https://github.com/yasanglass/orbit-compose-multiplatform/actions/workflows/wasm.yml"><img alt="wasm" src="https://img.shields.io/github/actions/workflow/status/yasanglass/orbit-compose-multiplatform/wasm.yml?label=wasm"/></a>
  <a href="https://github.com/yasanglass/orbit-compose-multiplatform/actions/workflows/publish.yml"><img alt="publish" src="https://img.shields.io/github/actions/workflow/status/yasanglass/orbit-compose-multiplatform/publish.yml?label=publish"/></a>
  <a href="https://context7.com/yasanglass/orbit-compose-multiplatform/llms.txt"><img alt="Context7" src="https://img.shields.io/badge/context7-llms.txt-blue"/></a>
</p>

> [!CAUTION]
> This is an **unofficial** [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/) port of Kiwi.com's now-archived [Orbit Compose](https://github.com/kiwicom/orbit-compose) library for Android.
>
> This fork is primarily for preservation. While it should be stable, it is not extensively tested and is not meant for production use.

> [!NOTE]
> Try the component catalog [on the web](https://yasan-org.github.io/orbit-compose-multiplatform/) or on macOS via `brew install --cask yasan-org/tap/orbit-compose-multiplatform-catalog`.

## About Orbit

[Orbit](https://orbit.kiwi) is an open-source design system that aims to bring order and consistency to
Kiwi.com products as well as the processes behind building them. It elevates the user experience and increases
the speed and efficiency of how we design and build products.

This library allows developers to implement Orbit in [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/) projects targeting Android, iOS, JVM Desktop, JS, and WebAssembly.

Orbit Compose builds upon the Material 3 library.

## Catalog

Try it [on the web](https://yasan-org.github.io/orbit-compose-multiplatform/) or install it on macOS via [Homebrew](https://github.com/yasan-org/homebrew-tap):

```sh
brew install --cask yasan-org/tap/orbit-compose-multiplatform-catalog
```

## Setup

We publish our releases in [Maven Central](https://search.maven.org/search?q=g:glass.yasan.orbit). To use
this library, you can add the following dependencies to your **Gradle file**:

```
implementation("glass.yasan.orbit:ui:<version>")
implementation("glass.yasan.orbit:icons:<version>")
implementation("glass.yasan.orbit:illustrations:<version>")
```

## How to use?

In order to use one of Orbit's components, simply add them to your Composable function.

```kotlin
@Composable
fun MyScreen() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        val maxWidth = Modifier.fillMaxWidth()
      
        ButtonPrimary(onClick = {}, maxWidth) {
            Text("This is an Orbit primary button")
        }
  
        Spacer(modifier = Modifier.padding(4.dp))
  
        AlertCritical(
            title = { Text("Alert title") },
            content = { Text("Content description") },
            actions = {
                ButtonPrimary(onClick = {}) { Text("Primary") }
                ButtonSecondary(onClick = {}) { Text("Secondary") }
            },
        )
    }
}
```

Colors, typography, and other foundational elements can be used by accessing the `OrbitTheme` object:

```kotlin
Text("Title 1", emphasis = ContentEmphasis.Minor)
Text("Big title in Title 1 style", style = OrbitTheme.typography.title1)
Text("Check your typography styles!", color = OrbitTheme.colors.warning.normal)
```

<table>
<tr>
<td>

![Orbit Button and Alert component](./docs/readme/button_and_alert.png)

<td>

![Orbit typography and colors](./docs/readme/styled_text.png)

</table>

### Icons and Illustrations

Icons and illustrations can be accessed in the same way, with the corresponding `Icons` and `Illustrations`
objects:

```kotlin
Image(painter = Illustrations.AppKiwi, contentDescription = "app_kiwi")
```

## Documentation

The [API documentation](https://kiwicom.github.io/orbit-compose/) lists
all possible types and composables in our library.
