plugins {
    id("java")
    id("net.fabricmc.fabric-loom") version "1.17.12"
}

version = project.property("mod_version") as String
group = project.property("mod_group") as String

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
    maven("https://libraries.mojang.net/")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")

    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    implementation("com.terraformersmc:modmenu:${property("modmenu_version")}")

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:${project.property("cloth_config_version")}")

    implementation("com.google.code.gson:gson:2.13.1")
    implementation("it.unimi.dsi:fastutil:8.5.18")
}

@Suppress("DEPRECATION")
loom {
    runs {
        named("client") {
            client()
            runDir = "run"
        }
        named("server") {
            server()
            runDir = "run"
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
    options.encoding = "UTF-8"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    withSourcesJar()
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(
            mapOf(
                "version" to project.version,
                "minecraft_version" to project.property("minecraft_version"),
                "loader_version" to project.property("loader_version")
            )
        )
    }
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.name}" }
    }
}

tasks.register<JavaExec>("runBenchmark") {
    group = "benchmark"
    description = "Runs the standalone UltraMemoryCore memory allocation benchmark."

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.example.ultramemorycore.util.MemoryBenchmark")
}
