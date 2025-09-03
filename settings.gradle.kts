rootProject.name = "conference-app-2025"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("gradle-conventions")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        // navigation3 adaptive
        maven {
            url = uri("https://androidx.dev/snapshots/builds/13508953/artifacts/repository")
        }
    }
}

include(
    ":app-android",
    ":app-desktop",
    ":app-shared",
)
include(
    ":core:common",
    ":core:model",
    ":core:droidkaigiui",
    ":core:data",
    ":core:designsystem",
    ":core:testing",
)
include(
    ":feature:sessions",
    ":feature:contributors",
    ":feature:sponsors",
    ":feature:eventmap",
    ":feature:about",
    ":feature:settings",
    ":feature:favorites",
    ":feature:staff",
    ":feature:profile",
)
include(":tools:ksp-processor")
