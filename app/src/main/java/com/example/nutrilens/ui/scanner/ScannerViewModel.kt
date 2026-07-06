package com.example.nutrilens.ui.scanner

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrilens.domain.model.FoodRecognitionResult
import com.example.nutrilens.domain.model.NutritionInfo
import com.example.nutrilens.domain.service.HealthRiskCalculator
import com.example.nutrilens.domain.service.NutritionEngine
import com.example.nutrilens.ml.FoodRecognizer
import com.example.nutrilens.ml.GeminiFoodRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CapturedImage(
    val bitmap: Bitmap,
    val foodName: String,
    val confidence: Float,
    val calories: Int,
    val sugar: Double,
    val riskLevel: String,
    val isRecognized: Boolean
)

class ScannerViewModel(application: Application) : AndroidViewModel(application) {

    private val geminiRecognizer by lazy { GeminiFoodRecognizer() }
    private val mlKitRecognizer by lazy { FoodRecognizer() }
    private val nutritionEngine = NutritionEngine()
    private val riskCalculator = HealthRiskCalculator()

    private val _capturedImage = MutableStateFlow<CapturedImage?>(null)
    val capturedImage: StateFlow<CapturedImage?> = _capturedImage.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    var lastCapturedFoodName: String = ""
        private set

    private val genericLabels = setOf(
        "food", "dish", "meal", "cuisine", "snack", "ingredient",
        "plate", "bowl", "table", "top", "background", "pattern",
        "breakfast", "lunch", "dinner", "appetizer", "dessert",
        "baked goods", "pastry", "baking", "cooking", "recipe",
        "produce", "grocery", "market", "restaurant", "menu",
    )

    fun onImageCaptured(bitmap: Bitmap) {
        viewModelScope.launch {
            _isProcessing.value = true

            val geminiResult = try {
                geminiRecognizer.recognize(bitmap)
            } catch (e: Exception) {
                FoodRecognitionResult("Unknown", 0f, false)
            }

            val result = if (geminiResult.isRecognized) {
                geminiResult
            } else {
                try {
                    mlKitRecognizer.recognize(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                    FoodRecognitionResult("Unknown", 0f, false)
                }
            }

            val isValid = result.isRecognized &&
                result.foodName !in genericLabels &&
                nutritionEngine.has(result.foodName)

            val nutrition = if (isValid) {
                nutritionEngine.estimate(result.foodName)
            } else {
                NutritionInfo(0, 0.0)
            }
            val riskLevel = if (isValid) {
                riskCalculator.calculate(nutrition.calories, nutrition.sugar)
            } else {
                "UNKNOWN"
            }

            lastCapturedFoodName = result.foodName

            _capturedImage.value = CapturedImage(
                bitmap = bitmap,
                foodName = result.foodName,
                confidence = result.confidence,
                calories = nutrition.calories,
                sugar = nutrition.sugar,
                riskLevel = riskLevel,
                isRecognized = isValid
            )

            _isProcessing.value = false
        }
    }

    fun clearCapture() {
        _capturedImage.value = null
        lastCapturedFoodName = ""
    }
}
