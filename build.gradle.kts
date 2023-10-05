// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    kotlin("jvm") version "1.9.10" apply false
    id("com.google.dagger.hilt.android") version "2.46" apply false
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.9.10"))
    }
}