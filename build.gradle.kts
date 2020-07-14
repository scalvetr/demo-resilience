import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("org.springframework.boot") version "2.3.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"

    kotlin("jvm") version "1.3.72"
    //https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    kotlin("kapt") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"

    id("com.github.lkishalmi.gatling") version "3.3.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    jcenter()
}

extra["springCloudVersion"] = "Hoxton.SR6"
extra["resilience4jVersion"] = "1.5.0"
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
    //implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
    // option 2
    implementation("io.github.resilience4j:resilience4j-spring-boot2:${property("resilience4jVersion")}")
    implementation("io.github.resilience4j:resilience4j-circuitbreaker:${property("resilience4jVersion")}")
    implementation("io.github.resilience4j:resilience4j-bulkhead:${property("resilience4jVersion")}")
    implementation("io.github.resilience4j:resilience4j-retry:${property("resilience4jVersion")}")
    implementation("io.github.resilience4j:resilience4j-cache:${property("resilience4jVersion")}")
    implementation("io.github.resilience4j:resilience4j-timelimiter:${property("resilience4jVersion")}")
    implementation("io.github.resilience4j:resilience4j-ratelimiter:${property("resilience4jVersion")}")
    implementation("io.github.resilience4j:resilience4j-kotlin:${property("resilience4jVersion")}")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // to allow anotations
    implementation("org.springframework.boot:spring-boot-starter-aop")

    //export metrics
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

tasks.withType<BootRun> {
    logger.warn("checking arguments")
    if (project.hasProperty("args")) {
        logger.warn("arguments found!")
        var arguments = project.property("args") as String
        logger.warn("arguments {}", arguments)
        jvmArgs = arguments.split(",")
    } else {

        logger.warn("arguments NOT found")
    }
}