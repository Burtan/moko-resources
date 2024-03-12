/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.generator.resources.image

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeSpec.Builder
import dev.icerock.gradle.generator.Constants
import dev.icerock.gradle.generator.Constants.Jvm
import dev.icerock.gradle.generator.Constants.PlatformDetails
import dev.icerock.gradle.generator.PlatformResourceGenerator
import dev.icerock.gradle.generator.addJvmPlatformResourceClassLoaderProperty
import dev.icerock.gradle.generator.addValuesFunction
import dev.icerock.gradle.metadata.resource.ImageMetadata
import java.io.File

internal class JvmImageResourceGenerator(
    private val className: String,
    private val resourcesGenerationDir: File,
) : PlatformResourceGenerator<ImageMetadata> {
    override fun imports(): List<ClassName> = emptyList()

    override fun generateInitializer(metadata: ImageMetadata): CodeBlock {
        val item: ImageMetadata.ImageQualityItem = metadata.getHighestQualityItem()
        val fileName = "${metadata.key}.${item.filePath.extension}"
        return CodeBlock.of(
            "ImageResource(resourcesClassLoader = %L, filePath = %S)",
            "${PlatformDetails.platformDetailsPropertyName}.${Jvm.resourcesClassLoaderPropertyName}",
            "$IMAGES_DIR/$fileName"
        )
    }

    override fun generateResourceFiles(data: List<ImageMetadata>) {
        generateHighestQualityImageResources(resourcesGenerationDir, data, IMAGES_DIR)
    }

    override fun generateBeforeProperties(
        builder: TypeSpec.Builder,
        metadata: List<ImageMetadata>,
        modifiers: List<KModifier>,
    ) {
        builder.addJvmPlatformResourceClassLoaderProperty(
            modifiers = modifiers,
            resourcesClassName = className
        )
    }

    override fun generateAfterProperties(
        builder: Builder,
        metadata: List<ImageMetadata>,
        modifiers: List<KModifier>,
    ) {
        builder.addValuesFunction(
            modifiers = modifiers,
            metadata = metadata,
            classType = Constants.imageResourceName
        )
    }

    private companion object {
        const val IMAGES_DIR = "images"
    }
}
