package com.example.nutrilens.ml

import android.graphics.Bitmap
import com.example.nutrilens.BuildConfig
import com.example.nutrilens.domain.model.FoodRecognitionResult
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

class GeminiFoodRecognizer {

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun recognize(bitmap: Bitmap): FoodRecognitionResult {
        if (BuildConfig.GEMINI_API_KEY.isBlank()) {
            return FoodRecognitionResult("Unknown", 0f, false)
        }

        return try {
            val inputContent = content {
                image(bitmap)
                text("Identify the food in this image. Respond with only the food name, nothing else.")
            }

            val response = model.generateContent(inputContent)
            val text = response.text?.trim() ?: ""

            if (text.isBlank() || text.contains("sorry", ignoreCase = true) || text.contains("cannot", ignoreCase = true)) {
                FoodRecognitionResult("Unknown", 0f, false)
            } else {
                FoodRecognitionResult(
                    foodName = text.removeSurrounding("\"").removeSurrounding("'"),
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
