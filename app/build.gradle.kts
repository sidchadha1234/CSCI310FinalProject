plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 33
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
}

dependencies {


    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-storage:latest_version")
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.firebaseui:firebase-ui-database:8.0.0") // Use the appropriate version
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("androidx.test:core:1.5.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    testImplementation("org.robolectric:robolectric:4.7.3")
// Robolectric
    androidTestImplementation("androidx.test.ext:truth:1.5.0")

// Shadow of the Android Toast class for Robolectric
    testImplementation("org.robolectric:shadows-multidex:4.7.3")
// Mockito for mocking in tests
    testImplementation("org.mockito:mockito-core:4.+")

    implementation("androidx.test.ext:junit:1.1.5")

    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")






}
