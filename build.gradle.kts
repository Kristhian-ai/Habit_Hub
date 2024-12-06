buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.5.2")
        classpath("com.google.gms:google-services:4.3.15")
        classpath ("com.android.tools.build:gradle:8.0.0")
    }
}

plugins {
    id("com.android.application") version "8.5.2" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
}
