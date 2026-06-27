package com.example.nutrilens.ml

import android.graphics.Bitmap
import com.example.nutrilens.domain.model.FoodRecognitionResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.tasks.await

class FoodRecognizer {

    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    suspend fun recognize(bitmap: Bitmap, confidenceThreshold: Float = 0.5f): FoodRecognitionResult {
        val image = InputImage.fromBitmap(bitmap, 0)
        val labels = labeler.process(image).await()

        val best = labels.maxByOrNull { it.confidence }
        return if (best != null && best.confidence >= confidenceThreshold) {
            FoodRecognitionResult(
                foodName = best.text,
                confidence = best.confidence,
                isRecognized = true
            )
        } else {
            FoodRecognitionResult(
                foodName = best?.text ?: "Unknown",
                confidence = best?.confidence ?: 0f,
                isRecognized = false
            )
        }
    }
}
