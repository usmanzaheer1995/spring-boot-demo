import nu.studer.gradle.jooq.JooqGenerate
import org.flywaydb.gradle.task.FlywayMigrateTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.graalvm.buildtools.native") version "0.9.28"
    id("org.flywaydb.flyway") version "10.1.0"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    id("nu.studer.jooq") version "9.0"
    id("org.openapi.generator") version "5.3.0"
}

group = "org.usmanzaheer1995"
version = "0.0.1-SNAPSHOT"

ext["jooq.version"] = jooq.version.get()

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.5.4")
        classpath("org.flywaydb:flyway-database-postgresql:10.1.0")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jdbc:3.2.2") {
        exclude(group = "org.springframework", module = "spring-core")
    }
    implementation("org.springframework:spring-core:6.1.3")
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    implementation("org.springdoc:springdoc-openapi-starter-common:2.3.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.jooq:jooq:3.19.3")
    implementation("org.flywaydb:flyway-core:10.1.0")
    jooqGenerator("org.postgresql:postgresql:42.5.4")
    developmentOnly("org.springframework.boot:spring-boot-devtools:3.2.2")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose:3.2.2")
    runtimeOnly("org.postgresql:postgresql:42.5.4")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.2") {
        exclude(group = "com.jayway.jsonpath", module = "json-path")
    }
    testImplementation("com.jayway.jsonpath:json-path:2.9.0")
    testImplementation("org.springframework.boot:spring-boot-testcontainers:3.2.2")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")
    testImplementation("org.testcontainers:postgresql:1.17.6")
    testImplementation("io.mockk:mockk:1.13.8")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

flyway {
    url = "jdbc:postgresql://0.0.0.0:5431/mydatabase"
    driver = "org.postgresql.Driver"
    user = "myuser"
    password = "secret"
    schemas = arrayOf("public")
    locations = arrayOf("filesystem:${project.projectDir}/src/main/resources/db/migration")
}

jooq {
    configurations {
        create("") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://0.0.0.0:5431/mydatabase"
                    user = "myuser"
                    password = "secret"
                    properties.add(Property().apply {
                        key = "ssl"
                        value = "false"
                    })
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    generate.apply {
                        isRelations = true
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "org.usmanzaheer1995.springbootdemo.persistence.db"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.named<JooqGenerate>("generateJooq") {
    (launcher::set)(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(21))
    })
}

val oasPackage = "org.usmanzaheer1995.springbootdemo.openapi"
val oasSpecLocation = "api-definition.yaml"
val oasGenOutputDir = project.layout.buildDirectory.dir("generated-oas")

openApiGenerate {
    inputSpec = project.file("${project.rootDir}/$oasSpecLocation").path
    generatorName = "kotlin-spring"
    outputDir = project.file(oasGenOutputDir).path
    apiPackage = "$oasPackage.api"
    modelPackage = "$oasPackage.model"
    configOptions = mapOf(
        "dateLibrary" to "java8",
        "interfaceOnly" to "true",
        "useTags" to "true"
    )
}

sourceSets {
    main {
        kotlin {
            srcDir("build/generated-src/jooq")
            srcDir("build/generated-oas/src/main/kotlin")
        }
    }
}
