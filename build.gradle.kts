import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask

import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation


plugins {
    kotlin("jvm") version ("1.6.10")
    kotlin("plugin.serialization") version("1.6.10")
    id("com.github.johnrengelman.shadow") version ("7.1.1")
    id("net.minecrell.plugin-yml.bukkit") version ("0.5.0")
    id("dev.s7a.gradle.minecraft.server") version ("1.1.0")
}

val projectId = "coins"
val projectName = "Coins"
val projectGroup = "run.dn5.sasa"
val projectDescription = "Coin plugin"
val projectVersion = "1.0-SNAPSHOT"
val artifactName = "${projectName}-${projectVersion}.jar"

group = projectGroup
version = projectVersion
description = projectDescription

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
    implementation("com.charleskorn.kaml:kaml:0.43.0")
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
}

bukkit {
    main = "$projectGroup.${projectId}.PaperPlugin"
    apiVersion = "1.18"
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    authors = listOf("ddPn08")
    depend = listOf("Vault")
    defaultPermission = BukkitPluginDescription.Permission.Default.OP
    commands {
        register("coins") {
            description = "Manage coins"
            aliases = listOf("coin")
            permission = "coins.command"
        }
    }
    permissions {
        register("coins.command") {
            description = "Manage coins"
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}

val relocateShadow by tasks.registering(ConfigureShadowRelocation::class) {
    target = tasks.shadowJar.get()
    prefix = projectGroup
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    shadowJar {
        dependsOn(relocateShadow)
        archiveFileName.set(artifactName)
    }
    register<LaunchMinecraftServerTask>("server") {
        dependsOn("shadowJar")

        val dir = rootDir.resolve(".debug/paper")
        doFirst {
            copy {
                from(buildDir.resolve("libs/${artifactName}"))
                into(dir.resolve("plugins"))
            }
        }

        jarUrl.set(LaunchMinecraftServerTask.JarUrl.Paper("1.18.2"))
        serverDirectory.set(dir)
        agreeEula.set(true)
    }
}