package com.example.nutrilens.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nutrilens.R
import com.example.nutrilens.data.database.FoodRiskDatabase
import com.example.nutrilens.data.entity.FoodLogEntity
import com.example.nutrilens.data.repository.FoodRepository
import com.example.nutrilens.databinding.FragmentResultBinding
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val repository: FoodRepository by lazy {
        FoodRepository(FoodRiskDatabase.getInstance(requireContext()).foodLogDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodName = arguments?.getString("foodName") ?: ""
        val calories = arguments?.getInt("calories") ?: 0
        val sugar = arguments?.getFloat("sugar")?.toDouble() ?: 0.0
        val riskLevel = arguments?.getString("riskLevel") ?: ""

        binding.foodValue.text = foodName
        binding.caloriesValue.text = "$calories kcal"
        binding.sugarValue.text = "$sugar g"
        binding.riskLevelValue.text = riskLevel

        val riskColorRes = when (riskLevel) {
            "LOW" -> R.color.risk_low_text
            "MEDIUM" -> R.color.risk_medium_text
            else -> R.color.risk_high_text
        }
        binding.riskLevelDisplay.text = riskLevel
        binding.riskLevelDisplay.setTextColor(
            ContextCompat.getColor(requireContext(), riskColorRes)
        )

        binding.saveButton.setOnClickListener {
            lifecycleScope.launch {
                repository.saveFood(
                    FoodLogEntity(
                        foodName = foodName,
                        calories = calories,
                        sugarContent = sugar,
                        riskLevel = riskLevel
                    )
                )
                if (isAdded) {
                    findNavController().navigate(R.id.homeFragment) {
                        popUpTo(R.id.homeFragment) { inclusive = true }
                    }
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
