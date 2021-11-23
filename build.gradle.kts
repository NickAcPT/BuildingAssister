plugins {
    java
    id("xyz.jpenilla.run-paper") version "1.0.4"
}

group = "io.github.nickacpt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")

}

tasks {
    runServer {
        minecraftVersion("1.17.1")
    }
}