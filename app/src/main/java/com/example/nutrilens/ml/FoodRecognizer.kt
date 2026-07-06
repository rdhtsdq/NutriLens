package com.example.nutrilens.ml

import android.graphics.Bitmap
import com.example.nutrilens.domain.model.FoodRecognitionResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.tasks.await

class FoodRecognizer {

    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    private val genericLabels = setOf(
        "food", "dish", "meal", "cuisine", "snack", "ingredient",
        "plate", "bowl", "table", "breakfast", "lunch", "dinner",
        "appetizer", "dessert", "baked goods", "pastry", "baking",
        "cooking", "recipe", "produce", "grocery", "restaurant",
        "menu", "topping", "side dish", "main course", "course",
        "container", "packaging", "wrapper", "label", "brand"
    )

    suspend fun recognize(bitmap: Bitmap, confidenceThreshold: Float = 0.5f): FoodRecognitionResult {
        val image = InputImage.fromBitmap(bitmap, 0)
        val labels = labeler.process(image).await()

        val best = labels
            .filter { it.text.lowercase() !in genericLabels }
            .maxByOrNull { it.confidence }

        return if (best != null && best.confidence >= confidenceThreshold) {
            FoodRecognitionResult(
                foodName = best.text.replaceFirstChar { it.uppercase() },
                confidence = best.confidence,
                isRecognized = true
            )
        } else {
            // Don't surface a raw generic ML Kit label as if it were the food — return Unknown
            // consistently instead, same contract as GeminiFoodRecognizer.
            FoodRecognitionResult("Unknown", best?.confidence ?: 0f, false)
        }
    }
}
