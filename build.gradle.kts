plugins {
    java
    id("xyz.jpenilla.run-paper") version "1.0.4"
    id("io.papermc.paperweight.userdev") version "1.2.0"
}

group = "io.github.nickacpt"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    paperDevBundle("1.17.1-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn(reobfJar)
    }
    runServer {
        minecraftVersion("1.17.1")
    }
}