import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask

plugins {
    kotlin("jvm") version ("1.6.10")
    id("com.github.johnrengelman.shadow") version ("7.1.1")
    id("net.minecrell.plugin-yml.bukkit") version ("0.5.0")
    id("dev.s7a.gradle.minecraft.server") version ("1.1.0")
}

val projectId = "template"
val projectName = "Template"
val projectGroup = "run.dn5"
val projectDescription = "Paper plugin template"
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
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}

bukkit {
    main = "$projectGroup.${rootProject.name}.PaperPlugin"
    apiVersion = "1.18"
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    authors = listOf("ddPn08")
    defaultPermission = BukkitPluginDescription.Permission.Default.OP
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    shadowJar {
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