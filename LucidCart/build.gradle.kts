plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.8"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "com.flavex"
version = "0.0.1-SNAPSHOT"
description = "gateway for project lucidcart"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.14")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.19.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.19.2")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.hibernate.validator:hibernate-validator")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // SOAP / JAXB
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.4")

    // JAX-WS (Metro) — runtime para consumir SOAP
    implementation("com.sun.xml.ws:jaxws-rt:4.0.2")
}
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// -----------------------------------------------------------
// Adiciona src/generated/java como fonte
// -----------------------------------------------------------
sourceSets {
    main {
        java.srcDir("src/generated/java")
    }
}

// -----------------------------------------------------------
// Classpath isolado do wsimport (SEM usar classpath do projeto)
// -----------------------------------------------------------
val jaxbTools by configurations.creating

dependencies {
    jaxbTools("com.sun.xml.ws:jaxws-tools:3.0.2")
}

// -----------------------------------------------------------
// Task correta para gerar classes JAXB via wsimport
// -----------------------------------------------------------
val generateJaxb by tasks.registering(JavaExec::class) {
    group = "soap"
    description = "Gera classes JAXB a partir do WSDL"

    // garante que o diretório existe antes do wsimport rodar
    doFirst {
        val dir = file("src/generated/java")
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    mainClass.set("com.sun.tools.ws.WsImport")
    classpath = jaxbTools

    args(
        "-keep",
        "-extension",
        "-verbose",
        "-s", "src/generated/java",
        "-p", "com.LucidCart.gateway.order.jabx",
        "http://localhost:8082/ws/order.wsdl"
    )
}


