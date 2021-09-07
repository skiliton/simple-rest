import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

plugins {
    idea
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.google.protobuf") version "0.8.17"
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
}

group = "com.ajax"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val grpcVersion = "1.40.0"
val protobufVersion = "3.17.2"
val protocVersion = protobufVersion

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("redis.clients:jedis")
    implementation("io.nats:jnats:2.10.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jeasy:easy-random-core:4.0.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.grpc:grpc-netty-shaded:${grpcVersion}")
    implementation ("io.grpc:grpc-protobuf:${grpcVersion}")
    implementation ("io.grpc:grpc-stub:${grpcVersion}")
    implementation("com.google.protobuf:protobuf-java-util:${protobufVersion}")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("io.grpc:grpc-netty-shaded:${grpcVersion}")

    implementation(platform("io.projectreactor:reactor-bom:2020.0.10"))
    implementation("io.projectreactor:reactor-core")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.grpc:grpc-testing:${grpcVersion}")
    testImplementation("org.mockito:mockito-core:3.12.4")
    testImplementation("io.projectreactor:reactor-test")

}

idea {
    module {
        generatedSourceDirs.addAll(listOf(
            file("${protobuf.protobuf.generatedFilesBaseDir}/main/grpc"),
            file("${protobuf.protobuf.generatedFilesBaseDir}/main/java")
        ))
    }
}

protobuf {

    protoc {
        artifact = "com.google.protobuf:protoc:${protocVersion}"
    }
    plugins {
        id("grpc"){
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }

    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
