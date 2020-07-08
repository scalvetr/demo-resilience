import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    jcenter()
}

extra["springCloudVersion"] = "Hoxton.SR6"
extra["resilience4jVersion"] = "1.4.0"
extra["mockkVersion"] = "1.10.0"
extra["springmockkVersion"] = "2.0.2"
extra["striktVersion"] = "0.25.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // option1
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
    // option 2
    //implementation("io.github.resilience4j:resilience4j-spring-boot2:${property("resilience4jVersion")}")
    runtimeOnly("io.github.resilience4j:resilience4j-micrometer:${property("resilience4jVersion")}")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    // test - mock librery
    testImplementation("com.ninja-squad:springmockk:${property("springmockkVersion")}")
    testImplementation("io.mockk:mockk:${property("mockkVersion")}")

    // test - assert librery
    testImplementation("io.strikt:strikt-core:${property("striktVersion")}")
    testImplementation("io.strikt:strikt-mockk:${property("striktVersion")}")
    testImplementation("io.strikt:strikt-spring:${property("striktVersion")}")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
