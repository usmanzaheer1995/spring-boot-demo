import nu.studer.gradle.jooq.JooqGenerate
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.graalvm.buildtools.native") version "0.9.28"
    id("org.flywaydb.flyway") version "10.0.0"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    id("nu.studer.jooq") version "9.0"
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
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jooq:jooq:3.19.3")
    jooqGenerator("org.postgresql:postgresql:42.5.4")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    implementation("org.flywaydb:flyway-core:9.16.0")
    runtimeOnly("org.postgresql:postgresql:42.5.4")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
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

sourceSets {
    main {
        kotlin {
            srcDir("build/generated-src/jooq")
        }
    }
}

tasks.named<JooqGenerate>("generateJooq") {
    (launcher::set)(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(21))
    })
}


flyway {
    url = "jdbc:postgresql://0.0.0.0:5431/mydatabase"
    user = "myuser"
    password = "secret"
    schemas = arrayOf("public")
    locations = arrayOf("filesystem:$project.projectDir/src/main/resources/db/migration")
}
