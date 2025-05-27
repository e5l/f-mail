plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    
    // Playwright for browser automation
    implementation("com.microsoft.playwright:playwright:1.40.0")
    
    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    // Assertions
    testImplementation("org.assertj:assertj-core:3.24.2")
    
    // Test containers for backend services
    testImplementation("org.testcontainers:testcontainers:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")
    
    // HTTP client for API testing
    testImplementation("io.ktor:ktor-client-core:2.3.7")
    testImplementation("io.ktor:ktor-client-cio:2.3.7")
    testImplementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    testImplementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    
    // Coroutines for async tests
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
    
    // Set test environment variables
    environment("HEADLESS", System.getenv("HEADLESS") ?: "false")
    environment("BASE_URL", System.getenv("BASE_URL") ?: "http://localhost:8080")
    environment("SLOW_MO", System.getenv("SLOW_MO") ?: "0")
}

// Task to install Playwright browsers
tasks.register<JavaExec>("installPlaywright") {
    classpath = configurations.runtimeClasspath.get()
    mainClass.set("com.microsoft.playwright.CLI")
    args = listOf("install", "chromium")
}

// Run e2e tests
tasks.register<Test>("e2eTest") {
    dependsOn("installPlaywright")
    useJUnitPlatform {
        includeTags("e2e")
    }
}