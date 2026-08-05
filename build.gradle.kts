plugins {
    id("java")
    id("fabric-loom") version "1.17-SNAPSHOT"
}

version = project.property("mod_version") as String
group = project.property("mod_group") as String

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")

    // ModMenu
    maven("https://maven.terraformersmc.com/releases/")

    // Cloth Config
    maven("https://maven.shedaniel.me/")
}

val minecraft_version: String by project
val loader_version: String by project
val fabric_version: String by project
val cloth_config_version: String by project
val modmenu_version: String by project

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")

    mappings(loom.layered {
        officialMojangMappings()
    })

    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:${property("cloth_config_version")}")
    modImplementation("com.terraformersmc:modmenu:${property("modmenu_version")}")

    implementation("com.google.code.gson:gson:2.13.1")
    implementation("it.unimi.dsi:fastutil:8.5.18")
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
