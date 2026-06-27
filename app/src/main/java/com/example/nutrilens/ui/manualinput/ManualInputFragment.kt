package com.example.nutrilens.ui.manualinput

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nutrilens.R
import com.example.nutrilens.databinding.FragmentManualInputBinding

class ManualInputFragment : Fragment() {

    private var _binding: FragmentManualInputBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ManualInputViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManualInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val initialFoodName = arguments?.getString("foodName") ?: ""
        binding.foodNameInput.setText(initialFoodName)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding.saveButton.isEnabled = binding.foodNameInput.text?.isNotBlank() == true
            }
        }
        binding.foodNameInput.addTextChangedListener(textWatcher)

        binding.saveButton.setOnClickListener {
            val foodName = binding.foodNameInput.text?.toString()?.trim() ?: return@setOnClickListener
            val calories = binding.caloriesInput.text?.toString()?.toIntOrNull() ?: 0
            val sugar = binding.sugarInput.text?.toString()?.toDoubleOrNull() ?: 0.0

            viewModel.saveFood(foodName, calories, sugar, "LOW") {
                findNavController().navigate(R.id.homeFragment) {
                    popUpTo(R.id.homeFragment) { inclusive = true }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
