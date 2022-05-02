plugins {
    java
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("io.papermc.paperweight.userdev") version "1.3.7-SNAPSHOT"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "io.github.nickacpt"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(paperDevBundle("1.18.2-R0.1-SNAPSHOT"))
    implementation("com.github.stefvanschie.inventoryframework:IF:0.10.3")
}

tasks {
    build {
        dependsOn(reobfJar)
        dependsOn(shadowJar)
    }
    runServer {
        minecraftVersion("1.18.2")
    }
}