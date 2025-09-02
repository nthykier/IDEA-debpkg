import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

plugins {
    // Java support
    id("java")
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "1.17.4"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "2.4.0"
}

fun properties(key: String) = project.findProperty(key).toString()

group = properties("pluginGroup")
version = properties("pluginVersion")

buildscript {
    repositories {
        mavenCentral()
    }
}

// Configure project's dependencies
repositories {
    mavenCentral()
}
dependencies {
    implementation("org.yaml:snakeyaml:2.3")
    implementation("org.apache.commons:commons-text:1.12.0")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
}

// Configure gradle-intellij-plugin plugin.
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    downloadSources.set(properties("platformDownloadSources").toBoolean())
    updateSinceUntilBuild.set(true)
    plugins.set(listOf(
        "org.jetbrains.plugins.yaml:212.4746.16",
        /* When we are ready to work with makefile support
            "name.kropp.intellij.makefile:212.4746.52".
        */
    ))
}

changelog {
    version.set(properties("pluginVersion"))
    groups.set(emptyList())
}

// Include the generated files in the source set
sourceSets["main"].java.srcDir("src/main/gen")

java {
    val v = JavaVersion.toVersion(properties("javaVersion"))
    sourceCompatibility = v
    targetCompatibility = v
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    // Set the JVM compatibility versions
    properties("javaVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
        }
    }
    runIde {
        autoReloadPlugins.set(true)
    }

    patchPluginXml {
        val pluginVersion = properties("pluginVersion")
        version.set(pluginVersion)
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set("")

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
            File(projectDir, "README.md").readText().lines().run {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md file:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end))
            }.joinToString("\n").run { markdownToHTML(this) }
        )

        val changelog = project.changelog // local variable for configuration cache compatibility

        changeNotes.set(provider {
            changelog.renderItem(
                (changelog.getOrNull(pluginVersion) ?: changelog.getUnreleased())
                    .withHeader(false)
                    .withEmptySections(false),
                Changelog.OutputType.HTML
            )
        })
    }

    runPluginVerifier {
        ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty))
    }

    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set((System.getenv("PUBLISH_TOKEN")))
        channels.set(listOf(properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()))
    }
}
