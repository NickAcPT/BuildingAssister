plugins {
    java
    id("xyz.jpenilla.run-paper") version "1.0.4"
    id("io.papermc.paperweight.userdev") version "1.2.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "io.github.nickacpt"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    paperDevBundle("1.17.1-R0.1-SNAPSHOT")
    implementation("com.github.stefvanschie.inventoryframework:IF:0.10.3")
}

tasks {
    build {
        dependsOn(reobfJar)
        dependsOn(shadowJar)
    }
    runServer {
        minecraftVersion("1.17.1")
    }
}