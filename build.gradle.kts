plugins {
    java
    id("fabric-loom") version "1.17-SNAPSHOT"
}

version = property("mod_version") as String
group = property("mod_group") as String

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    val mc = property("minecraft_version") as String

    minecraft("com.mojang:minecraft:$mc")
    mappings(loom.officialMojangMappings())

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

tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
    options.encoding = "UTF-8"
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(
            mapOf(
                "version" to project.version,
                "minecraft_version" to property("minecraft_version"),
                "loader_version" to property("loader_version")
            )
        )
    }
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.name}" }
    }
}
