import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType


plugins {
    kotlin("multiplatform")
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("http://kotlin.bintray.com/kotlinx")
    maven("http://kotlin.bintray.com/kotlin-eap")
}

val hostOs = System.getProperty("os.name")
val mingwDir: String by rootProject.extra
val mingwPath = File(mingwDir)

kotlin {
    createRequestedTarget("app").apply {
		binaries {
            executable {
                entryPoint = "org.notlocalhost.template"
                when (preset) {
                    presets["macosX64"] -> linkerOpts(
					    "-L/opt/local/lib",
					    "-L/usr/local/lib"
					)
                    presets["linuxX64"] -> linkerOpts(
					    "-L/usr/lib64",
					    "-L/usr/lib/x86_64-linux-gnu"
					)
                    presets["mingwX64"] -> linkerOpts(
                        "-L${mingwPath.resolve("lib")}",
                        "-Wl,-Bstatic",
                        "-lstdc++",
                        "-static",
                        "-mwindows"
                    )
				}
				
				runTask?.workingDir(project.provider {
                    val app: KotlinNativeTarget by kotlin.targets
                    app.binaries.getExecutable(buildType).outputDirectory
                })
			}
		}
		
		/* compilations["main"].cinterops {
            val <defFileName> by creating {
                when (preset) {
                    presets["macosX64"] -> includeDirs()
                    presets["linuxX64"] -> includeDirs()
                    presets["mingwX64"] -> {
					    listOf(
							// "<include dir>", ..
                        ).forEach {
                            includeDirs(mingwPath.resolve(it))
                        }
					}
                }
            }
		} */
	}
	
	sourceSets {
		val commonMain by getting {
			dependencies {
				implementation(kotlin("stdlib-common"))
			}
		}
	}
}

fun createRequestedTarget(name: String): KotlinNativeTarget = with(kotlin) {
    return when {
		hostOs == "Mac OS X" -> macosX64(name)
		hostOs == "Linux" -> linuxX64(name)
		hostOs.startsWith("Windows") -> mingwX64(name)
		else -> throw GradleException("Host OS '$hostOs' is not supported in Kotlin/Native $project.")
    }.also {
        println("$project has been configured for ${it.preset?.name} platform.")
    }
}