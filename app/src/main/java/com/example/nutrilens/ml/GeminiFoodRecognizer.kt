package com.example.nutrilens.ml

import android.graphics.Bitmap
import com.example.nutrilens.BuildConfig
import com.example.nutrilens.domain.model.FoodRecognitionResult
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

class GeminiFoodRecognizer {

    private val model = GenerativeModel(
        modelName = "gemini-3.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun recognize(bitmap: Bitmap): FoodRecognitionResult {
        if (BuildConfig.GEMINI_API_KEY.isBlank()) {
            return FoodRecognitionResult("Unknown", 0f, false)
        }

        return try {
            val inputContent = content {
                image(bitmap)
                text("""Identify the specific food in this image. Reply with ONLY a single word: the food name in lowercase. Do not describe what it looks like, do not add extra words, do not explain. Examples: "bread", "pizza", "apple", "sushi", "pasta", "rice", "chicken", "salad", "cake", "burger". If you cannot identify a specific food, reply with exactly "unknown".""")
            }

            val response = model.generateContent(inputContent)
            var text = response.text?.trim()?.lowercase() ?: ""

            text = text.removeSurrounding("\"").removeSurrounding("'")
                .removeSurrounding("`").removeSurrounding("**").trim()
            text = text.split("\n").first().split(",").first().trim()

            val rejectWords = listOf(
                "sorry", "cannot", "unknown", "i don't", "i'm not", "not sure",
                "pattern", "texture", "background", "image", "photo", "picture",
                "color", "shape", "item", "object", "ingredient", "dish", "food",
                "meal", "cuisine", "snack", "plate", "bowl", "table", "top",
                "looks like", "appears to be", "this is", "that is", "it is"
            )

            val isRejected = text.isBlank() || text.length > 40 ||
                rejectWords.any { text.contains(it) }

            if (isRejected) {
                FoodRecognitionResult("Unknown", 0f, false)
            } else {
                FoodRecognitionResult(
                    foodName = text.replaceFirstChar { it.uppercase() },
                    confidence = 0.9f,
                    isRecognized = true
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            FoodRecognitionResult("Unknown", 0f, false)
        }
    }
}
