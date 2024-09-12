import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

// Load local properties
val localProperties = Properties()
runCatching { localProperties.load(rootProject.file("local.properties").inputStream()) }
    .getOrElse { it.printStackTrace() }

// Load signing properties from the signing.properties file
val signingProperties = Properties()
val signingPropertiesFile = rootProject.file("signing.properties")
runCatching { signingProperties.load(signingPropertiesFile.inputStream()) }
    .getOrElse { it.printStackTrace() }

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.apollo)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.android.maps.utils)
            implementation(libs.maps.utils.ktx)
            implementation(libs.accompanist.permissions)
            implementation(libs.play.services.location)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.navigation.compose)
            implementation(libs.apollo.runtime)
            implementation(libs.maps.compose)
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.no.arg)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.mp)
            implementation(libs.coil.network.ktor)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "org.cdu.codefair.alertcity"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
            resources.srcDirs("src/commonMain/resources")
        }
        getByName("debug") {
            manifest.srcFile("src/androidDebug/AndroidManifest.xml")
        }
    }

    defaultConfig {
        applicationId = "org.cdu.codefair.alertcity"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        // Add the Google Maps API key to AndroidManifest
        manifestPlaceholders["mapsApiKey"] = "${localProperties["GOOGLE_MAPS_API_KEY"]}"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        create("release") {
            keyAlias = signingProperties["RELEASE_KEY_ALIAS"] as String
            keyPassword = signingProperties["RELEASE_KEY_PASSWORD"] as String
            storeFile = file(signingProperties["RELEASE_KEYSTORE_FILE"] as String)
            storePassword = signingProperties["RELEASE_KEYSTORE_PASSWORD"] as String
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

apollo {
    service("service") {
        packageName.set("org.cdu.codefair.alertcity")
        codegenModels.set("operationBased")
        generateDataBuilders.set(true)
        generateFragmentImplementations.set(true)
        generateSchema.set(true)
        generateKotlinModels.set(true)

        introspection {
            endpointUrl = localProperties["BACKEND_URL"] as String
            schemaFile.set(file("src/commonMain/graphql/schema.graphqls"))
        }
    }
}

buildkonfig {
    packageName = "org.cdu.codefair.alertcity"

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "BACKEND_URL",
            localProperties["BACKEND_URL"]?.toString() ?: ""
        )
    }

}