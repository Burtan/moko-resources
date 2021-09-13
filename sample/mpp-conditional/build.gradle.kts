/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("android-base-convention")
    id("org.jetbrains.kotlin.multiplatform")
    id("com.github.krottv.multiplatform-resources")
    id("detekt-convention")
}

android {
    lintOptions {
        disable("ImpliedQuantity")
    }
}

kotlin {
    android()
    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
    if (onPhone) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }

    targets.getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>("ios") {
        binaries.framework {
            baseName = "MultiPlatformLibrary"
            isStatic = false
        }
    }
}

dependencies {
    commonMainApi(projects.resources)
}

multiplatformResources {
    multiplatformResourcesPackage = "com.icerockdev.library"
}
