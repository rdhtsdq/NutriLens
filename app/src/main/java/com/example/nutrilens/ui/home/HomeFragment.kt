package com.example.nutrilens.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.nutrilens.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.greetingText.text = viewModel.greeting

        adapter = FoodAdapter { food ->
            viewModel.deleteFood(food)
        }
        binding.foodRecyclerView.adapter = adapter
        binding.foodRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.todayFoods.collect { foods ->
                        adapter.submitList(foods)
                        val hasFoods = foods.isNotEmpty()
                        binding.emptyState.visibility = if (hasFoods) View.GONE else View.VISIBLE
                        binding.foodRecyclerView.visibility = if (hasFoods) View.VISIBLE else View.GONE
                    }
                }
                launch {
                    viewModel.totalCalories.collect { total ->
                        binding.calorieCount.text = total.toString()
                        val progress = (total.toFloat() / 2000f).coerceIn(0f, 1f)
                        binding.calorieProgress.progress = (progress * 100).toInt()
                        binding.caloriePercent.text = "${(progress * 100).toInt()}%"
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
