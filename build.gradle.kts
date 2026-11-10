// Plugins declarados aquí (apply false) y aplicados de verdad en app/build.gradle.kts:
// así Gradle los resuelve una sola vez para todo el proyecto.
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20" apply false
}
