plugins {
	id("fabric-loom") version "0.10-SNAPSHOT"
	kotlin("jvm") version "1.6.10"
	`maven-publish`
}

group = "net.stckoverflw"
version = "1.0.0"

val sourceCompatibility = JavaVersion.VERSION_17
val targetCompatibility = JavaVersion.VERSION_17

val archives_base_name: String by project
val mod_version: String by project
val maven_group: String by project

repositories {
	maven("https://jitpack.io")
}

val minecraft_version: String by project
val yarn_mappings: String by project
val loader_version: String by project

val pehkui_version: String by project

dependencies {

	minecraft("com.mojang:minecraft:$minecraft_version")
	mappings("net.fabricmc:yarn:$yarn_mappings:v2")
	modImplementation("net.fabricmc:fabric-loader:$loader_version")

	modApi("com.github.Virtuoel:Pehkui:${pehkui_version}")
}

tasks {
	withType<JavaCompile> {
		// ensure that the encoding is set to UTF-8, no matter what the system default is
		// this fixes some edge cases with special characters not displaying correctly
		// see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
		// If Javadoc is generated, this must be specified in that task too.
		options.encoding = "UTF-8"

		// The Minecraft launcher currently installs Java 8 for users, so your mod probably wants to target Java 8 too
		// JDK 9 introduced a new way of specifying this that will make sure no newer classes or methods are used.
		// We'll use that if it's available, but otherwise we'll use the older option.
		val targetVersion = 17
		if (JavaVersion.current().isJava9Compatible) {
			options.release.convention(targetVersion)
		}
	}

	processResources {
		inputs.property("version",  project.version)

		filesMatching("fabric.mod.json") {
			this.expand("version" to project.version)
		}
	}

	jar {
		from("LICENSE") {
			rename { "${it}_${archives_base_name}" }
		}
	}

	val sourcesJar by creating(Jar::class) {
		dependsOn(classes)
		archiveClassifier.convention("sources")
		from(sourceSets["main"].allSource)
	}

	publishing {
		publications {
			create<MavenPublication>("maven") {
				// add all the jars that should be included when publishing to maven
				artifact(remapJar.get())
				artifact(sourcesJar)
			}
		}

		// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
		repositories {
			// Add repositories to publish to here.
			// Notice: This block does NOT have the same function as the block in the top level.
			// The repositories here will be used for publishing your artifact, not for
			// retrieving dependencies.
		}
	}
}
