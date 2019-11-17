plugins {
    kotlin("multiplatform") version "1.3.50" apply false
}

allprojects {
    repositories {
	    mavenLocal()
		jcenter()
        mavenCentral()
		maven("http://kotlin.bintray.com/kotlinx")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
    }
}

val hostOs = System.getProperty("os.name")
val isMacos   by extra(hostOs == "Mac OS X")
val isLinux   by extra(hostOs == "Linux")
val isWindows by extra(hostOs.startsWith("Windows"))

extra["mingwDir"] = System.getenv("MINGW64_DIR") ?: "C:/msys64/mingw64"

extra["hostPresetName"] = when {
    isMacos -> "macosX64"
    isLinux -> "linuxX64"
    isWindows -> "mingwX64"
    else -> error("Unsupported host platform")
}

val buildAll by tasks.creating {
    dependsOn(":app:assemble")
}