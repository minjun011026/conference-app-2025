plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.spotless")
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)

            implementation(libs.kotlinxSerializationJson)
            api(libs.kotlinxDatetime)
            api(libs.kotlinxCollectionsImmutable)
            api(libs.soilQueryCore)
        }

        androidMain.dependencies {
            implementation(libs.androidxAppCompat)
        }
    }
}
