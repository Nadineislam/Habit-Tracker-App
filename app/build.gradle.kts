plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.dagger.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.habittrackerapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.habittrackerapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Dagger Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)
    // Hilt Navigation
    implementation(libs.hilt.navigation)

    // Navigation
    implementation("androidx.navigation:navigation-runtime-ktx:2.8.7")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.7")
//
    implementation (libs.core.common)
    implementation (libs.core.runtime)

    androidTestImplementation ( libs.core.testing)

    implementation (libs.room.runtime)
    annotationProcessor (libs.room.compiler)
    kapt (libs.room.compiler)

    kapt ("android.arch.persistence.room:compiler:1.1.1")
    kaptAndroidTest ("androidx.room:room-compiler:2.6.1")

    // RxJava + RxAndroid support for Room
    implementation (libs.rxandroid)
    implementation (libs.room.rxjava2)

    // Test helpers
    testImplementation (libs.room.testing)

    testImplementation ("androidx.arch.core:core-testing:2.2.0")






}