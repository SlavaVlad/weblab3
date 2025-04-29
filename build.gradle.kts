import org.gradle.api.tasks.bundling.War

plugins {
    // WAR-плагин для сборки .war
    war

    // стандартные Java/Kotlin плагины
    java
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.allopen") version "2.0.20"
    kotlin("plugin.noarg") version "2.0.20"
}

group = "com.nano"
version = "1.0-SNAPSHOT"

// Разрешаем аннотации JPA-классов в Kotlin
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}
noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

repositories {
    mavenCentral()
    // репозиторий для ICEfaces-ACE
    maven {
        url = uri("https://repository.jboss.org/nexus/content/repositories/releases")
    }
}

dependencies {
    // API Java EE (FacesContext и прочее). Реализация на WildFly.
    providedCompile("jakarta.platform:jakarta.jakartaee-api:9.1.0")
//    // Можно убрать, если уверены, что WildFly даёт JSF 2.3
//    implementation("jakarta.faces:jakarta.faces-api:")

    // PrimeFaces и ICEfaces-ACE
    implementation("org.primefaces:primefaces:12.0.0")
    implementation("org.icefaces:icefaces-ace:4.3.0")

    // EclipseLink JPA
    compileOnly("jakarta.platform:jakarta.jakartaee-api:9.1.0")
    implementation("org.eclipse.persistence:eclipselink:4.0.2")

    // Hibernate Validator для @Validate
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")

    // Oracle JDBC — на сервере может быть свой драйвер, тогда используйте providedCompile
    providedCompile("com.oracle.database.jdbc:ojdbc8:21.9.0.0")

    // Тесты JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
}

tasks {
    // Кодировка
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    // Переименовать WAR
    named<War>("war") {
        archiveFileName.set("points-app.war")
    }
}
