pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }

        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    repositories {
        // Fabric
        maven("https://maven.fabricmc.net/")

        // Mod Menu
        maven("https://maven.terraformersmc.com/releases/")

        // Cloth Config
        maven("https://maven.shedaniel.me/")

        // Maven Central
        mavenCentral()
    }
}

rootProject.name = "UltraMemoryCore"
