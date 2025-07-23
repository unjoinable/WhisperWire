plugins {
    id("java")
}

group = "io.github.unjoinable"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.minestom:minestom:2025.07.11-1.21.7")
    implementation("net.dv8tion:JDA:5.6.1")
    implementation("org.tomlj:tomlj:1.1.1")
    implementation("org.jspecify:jspecify:1.0.0")
    implementation("ch.qos.logback:logback-classic:1.5.18")
}

tasks.test {
    useJUnitPlatform()
}