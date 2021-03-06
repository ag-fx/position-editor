import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.31"
    antlr
    idea
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    antlr("org.antlr:antlr4:4.7")
    implementation("org.antlr:antlr4-runtime:4.7")

    compile("org.apache.commons:commons-csv:1.5")
    compile("no.tornado:tornadofx:1.7.17")
    compile("de.jensd:fontawesomefx:8.9")
}

val genSrcDir = file("src/main/gen")
val genDir = genSrcDir.resolve("com/zelkatani/antlr")

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

sourceSets["main"].java {
    srcDir(genSrcDir)
}

idea {
    module {
        generatedSourceDirs.add(genSrcDir)
    }
}

tasks.withType<AntlrTask> {
    outputDirectory = genDir
    arguments.addAll(listOf(
        "-package", "com.zelkatani.antlr",
        "-visitor",
        "-long-messages",
        "-Werror"))
}

tasks.named<Jar>("jar") {
    manifest.attributes["Main-Class"] = "com.zelkatani.gui.app.Editor"
    from(configurations.runtimeClasspath.map { if (it.isDirectory) it as Any else zipTree(it) })
    from(sourceSets["main"].output)
    archiveName = "position-editor.jar"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xallow-result-return-type")
    }

    dependsOn(tasks.getByName("generateGrammarSource"))
}