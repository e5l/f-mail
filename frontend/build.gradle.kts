plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
}

kotlin {
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            webpackTask {
                mainOutputFileName = "fmail.js"
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val wasmJsMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                
                // Ktor client for API calls
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                
                // Coroutines
                implementation(libs.kotlinx.coroutines.core)
                
                // DateTime
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}
