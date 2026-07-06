package com.example.nutrilens.ui.analytics

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.nutrilens.R
import com.example.nutrilens.databinding.FragmentAnalyticsBinding
import kotlinx.coroutines.launch

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnalyticsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weeklyStats.collect { stats ->
                    binding.totalCalories.text = stats.totalCalories.toString()
                    binding.averageCalories.text = stats.averageDailyCalories.toString()
                    binding.totalFoods.text = stats.totalFoods.toString()
                    binding.lowRiskCount.text = stats.lowRiskCount.toString()
                    binding.mediumRiskCount.text = stats.mediumRiskCount.toString()
                    binding.highRiskCount.text = stats.highRiskCount.toString()
                    populateDailyBreakdown(stats.dailyBreakdown)
                }
            }
        }
    }

    private fun populateDailyBreakdown(dailyStats: List<DailyStats>) {
        val container = binding.dailyBreakdownContainer
        container.removeAllViews()

        if (dailyStats.isEmpty()) {
            val emptyText = TextView(requireContext()).apply {
                text = "No foods logged this week"
                gravity = Gravity.CENTER_HORIZONTAL
                setTextSize(16f)
                setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                setPadding(0, 24, 0, 24)
            }
            container.addView(emptyText)
            return
        }

        for (day in dailyStats) {
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(0, 8, 0, 8)
            }

            val dayLabel = TextView(requireContext()).apply {
                text = day.dayLabel
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                setTextSize(16f)
                setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            }

            val barContainer = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    32,
                    2f
                )
                gravity = Gravity.CENTER
            }

            val maxCalories = dailyStats.maxOfOrNull { it.totalCalories } ?: 1
            val barWidth = (day.totalCalories.toFloat() / maxCalories * 200).toInt().coerceAtLeast(4)

            val bar = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    barWidth,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        if (day.totalCalories > 400) R.color.risk_high_text
                        else if (day.totalCalories > 200) R.color.risk_medium_text
                        else R.color.risk_low_text
                    )
                )
            }

            val calorieText = TextView(requireContext()).apply {
                text = "${day.totalCalories}"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginStart = 8 }
                setTextSize(14f)
                setTypeface(null, android.graphics.Typeface.BOLD)
            }

            barContainer.addView(bar)
            row.addView(dayLabel)
            row.addView(barContainer)
            row.addView(calorieText)
            container.addView(row)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
