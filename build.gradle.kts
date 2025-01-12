plugins {
    id("java")
}

group = "ru.godl1ght"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.1")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.4.1")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.1")
    implementation("org.postgresql:postgresql:*")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.4.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.1")
    compileOnly("org.projectlombok:lombok:1.18.36")
}

tasks.withType<JavaCompile> { options.encoding = "UTF-8" }
tasks.withType<Javadoc> { options.encoding = "UTF-8" }