import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("net.kyori.indra") version "3.1.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.kyori.blossom") version "1.3.1"
}

group = "com.redearcadia"
version = "5.0.0-beta.11"
description = "JDA packed in a plugin."

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Velocity
    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")

    // Discord
    implementation("net.dv8tion:JDA:5.0.0-beta.11") {
        exclude("club.minnced", "opus-java")
    }
}

indra {
    javaVersions {
        target(17)
    }
}

blossom {
    val plugin = "src/main/java/com/redearcadia/jdavelocity/JDAVelocity.java"
    replaceToken("{VERSION}", project.version, plugin)
    replaceToken("{DESCRIPTION}", project.description, plugin)
}

tasks {
    withType<ShadowJar> {
        // Remove archive classifier from output jar
        archiveClassifier.set("")

        // Copy all final jar to build directory
        doLast {
            copy {
                from(archiveFile)
                into("${rootProject.projectDir}/build")
            }
        }
    }

    build {
        dependsOn(shadowJar)
    }
}