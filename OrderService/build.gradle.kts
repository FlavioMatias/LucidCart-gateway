import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    idea
}

group = "com"
version = "0.0.1-SNAPSHOT"
description = "order microservice"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

/* ----------------------------------------------------
   CONFIGURAÇÃO PARA EXECUTAR XJC (gerar classes JAXB)
----------------------------------------------------- */
configurations {
    create("jaxb")
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web-services")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Banco
    runtimeOnly("com.h2database:h2")

    // JAXB 3.x (compatível com Java 21 E com XJC clássico)
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:3.0.2")
    implementation("wsdl4j:wsdl4j:1.6.3")
    // XJC (somente versão 3 funciona corretamente)
    "jaxb"("org.glassfish.jaxb:jaxb-xjc:3.0.2")
    "jaxb"("org.glassfish.jaxb:jaxb-core:3.0.2")
    "jaxb"("org.glassfish.jaxb:txw2:3.0.2")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testes
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
    testLogging {
        events = setOf(TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.PASSED)
    }
}

tasks.withType<KotlinCompile> {
    dependsOn("generateJaxb") 
    kotlinOptions {
        jvmTarget = "21"
    }
}
/* ----------------------------------------------------
   TASK generateJaxb: gera as classes do XSD
----------------------------------------------------- */
tasks.register<JavaExec>("generateJaxb") {
    // Usamos 'file()' para garantir que os caminhos absolutos sejam resolvidos corretamente.
    val xsdDirFileObject = file("src/main/resources/xsd")
    val xsdFileObject = file("$xsdDirFileObject/order.xsd") // Referência direta ao arquivo

    val outputDir = layout.buildDirectory.dir("generated/sources/jaxb").get().asFile

    inputs.file(xsdFileObject) // Marcamos o input como o arquivo específico
    outputs.dir(outputDir)

    doFirst {
        outputDir.mkdirs()
    }

    classpath = configurations["jaxb"]
    mainClass.set("com.sun.tools.xjc.Driver")

    // Passamos o caminho absoluto *sem* o prefixo URI 'file://' manualmente
    args = listOf(
        "-d", outputDir.absolutePath,
        "-p", "com.orderservice.contract",
        xsdFileObject.absolutePath // Apenas o caminho do sistema de arquivos
    )
}

/* ----------------------------------------------------
   Adiciona código gerado ao sourceSet
----------------------------------------------------- */
// Usa layout.buildDirectory para ser mais idiomático em Kotlin DSL
sourceSets["main"].java.srcDir(layout.buildDirectory.dir("generated/sources/jaxb").get().asFile)

idea {
    module {
        generatedSourceDirs.add(layout.buildDirectory.dir("generated/sources/jaxb").get().asFile)
    }
}