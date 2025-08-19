plugins {
    id("droidkaigi.convention.kmp-feature")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.soilForm)
            implementation(libs.filekitDialogsCompose)
        }
    }
}
