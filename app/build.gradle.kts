plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.HabitTracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.HabitTracker"
        minSdk = 24
        targetSdk = 34
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core AndroidX libraries
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation ("com.google.android.material:material:1.8.0")

    implementation ("com.google.android.gms:play-services-fitness:21.0.1")  // Google Fit SDK
    implementation ("com.google.android.gms:play-services-auth:20.3.0")     // Google Sign-In SDK
    implementation ("com.google.android.material:material:1.6.0")


    // Material Components for AppBarLayout
        implementation ("com.google.android.material:material:1.9.0")

        // AppCompat library for Toolbar
        implementation ("androidx.appcompat:appcompat:1.6.1")

        // ConstraintLayout (if you are using it in other parts of your project)
        implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    annotationProcessor ("androidx.room:room-compiler:2.5.1")
    implementation ("androidx.room:room-runtime:2.5.1")
    implementation ("androidx.room:room-ktx:2.5.1")
    implementation ("androidx.room:room-runtime:2.5.0")
    annotationProcessor ("androidx.room:room-compiler:2.5.0")

    implementation ("com.google.android.gms:play-services-fitness:21.0.1")
    implementation ("com.google.android.gms:play-services-auth:20.0.1")



    implementation ("com.google.firebase:firebase-messaging:23.1.0")

    //for syncing of data
    implementation ("com.google.android.gms:play-services-fitness:21.0.1")
    implementation ("com.google.android.gms:play-services-auth:20.1.0")

    // Material Design Components
    implementation("com.google.android.material:material:1.10.0")

    // Firebase BoM (Bill of Materials) for consistent versioning
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-messaging")

    // Google Sign-In API
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Room components
    implementation("androidx.room:room-runtime:2.6.0")
    annotationProcessor("androidx.room:room-compiler:2.6.0")

    // Image loading libraries
    implementation("com.github.bumptech.glide:glide:4.13.2")
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.2")
    implementation("com.squareup.picasso:picasso:2.71828")

    // Testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
