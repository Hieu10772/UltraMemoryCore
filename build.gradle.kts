plugins {
    id("java")
    id("net.fabricmc.fabric-loom") version "1.17.12"
}

version = project.property("mod_version") as String
group = project.property("mod_group") as String

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")

    // ModMenu
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    // Mojang Official Mappings cho 26.2
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    
    // Sử dụng string invoke để định nghĩa configuration khi accessor chưa sinh
    "mappings"(loom.officialMojangMappings())

    "modImplementation"("net.fabricmc:fabric-loader:${property("loader_version")}")
    "modImplementation"("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    "modImplementation"("com.terraformersmc:modmenu:${property("modmenu_version")}")

    // Explicit dependencies for standalone benchmark and future optimization code
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("it.unimi.dsi:fastutil:8.5.18")
}

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
