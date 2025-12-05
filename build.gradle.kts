plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "be.kdg.programming3"
version = "1.0-SNAPSHOT"
description = "spring"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")
        implementation("org.webjars:bootstrap:5.3.3")
        implementation("org.webjars:jquery:3.7.1")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation ("org.springframework.boot:spring-boot-starter-jdbc")

        runtimeOnly("org.postgresql:postgresql")
        runtimeOnly ("com.h2database:h2")

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        developmentOnly("org.springframework.boot:spring-boot-devtools")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
