package droidkaigi.primitive

import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import util.getDefaultPackageName

plugins {
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

val configurationName = when {
    extensions.findByType(KotlinMultiplatformExtension::class.java) != null -> "commonMainImplementation"
    extensions.findByType(KotlinJvmProjectExtension::class.java) != null -> "implementation"
    extensions.findByType(KotlinAndroidProjectExtension::class.java) != null -> "implementation"
    else -> "implementation"
}

dependencies {
    add(configurationName, compose.components.resources)
}

compose {
    resources {
        val namespace = getDefaultPackageName(project.name)
        packageOfResClass = namespace
        nameOfResClass = namespace.split(".").last().capitalized() + "Res"
        generateResClass = always
    }
}
