import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("plugin.serialization") version "1.6.10"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
}

group = "ru.krgolovin"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

buildscript {
	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-serialization")
	}
}

dependencies {
	implementation("org.telegram:telegrambots-spring-boot-starter:5.7.1")

	implementation("com.squareup.retrofit2:retrofit:2.9.0")

	// https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-serialization-json
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
	// https://mvnrepository.com/artifact/com.jakewharton.retrofit/retrofit2-kotlinx-serialization-converter
	implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

	implementation("com.squareup.okhttp3:okhttp:4.9.0")

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.junit.jupiter:junit-jupiter:5.7.0")
	implementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
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
