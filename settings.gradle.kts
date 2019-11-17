pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
		maven("http://kotlin.bintray.com/kotlinx")
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }

    resolutionStrategy {
        eachPlugin {
            // Allow applying android plugin using the Plugin DSL in kts.
            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}

rootProject.name = "kotlin-native-template"

include(":app")

enableFeaturePreview("GRADLE_METADATA")