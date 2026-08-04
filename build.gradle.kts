plugins {
    id("java")
    id("fabric-loom") version "1.12-SNAPSHOT"
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

dependencies {
    val minecraftVersion = project.property("minecraft_version") as String
    val yarnMappings = project.property("yarn_mappings") as String
    val loaderVersion = project.property("loader_version") as String
    val fabricVersion = project.property("fabric_version") as String

    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:${property("cloth_config_version")}")
    modImplementation("com.terraformersmc:modmenu:${property("modmenu_version")}")

    // Explicit dependencies for standalone benchmark and future optimization code
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("it.unimi.dsi:fastutil:8.5.18")
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
