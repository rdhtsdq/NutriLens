package com.example.nutrilens.ui.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrilens.R
import com.example.nutrilens.data.entity.FoodLogEntity
import com.example.nutrilens.databinding.ItemFoodBinding

class FoodAdapter(
    private val onDelete: (FoodLogEntity) -> Unit
) : ListAdapter<FoodLogEntity, FoodAdapter.FoodViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FoodViewHolder(binding, onDelete)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FoodViewHolder(
        private val binding: ItemFoodBinding,
        private val onDelete: (FoodLogEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentFood: FoodLogEntity? = null

        fun bind(food: FoodLogEntity) {
            currentFood = food
            binding.foodName.text = food.foodName
            binding.foodDetails.text = "${food.calories} kcal \u00B7 ${food.sugarContent}g sugar"

            val (bg, textColor) = when (food.riskLevel) {
                "LOW" -> R.drawable.bg_risk_low to R.color.risk_low_text
                "MEDIUM" -> R.drawable.bg_risk_medium to R.color.risk_medium_text
                else -> R.drawable.bg_risk_high to R.color.risk_high_text
            }
            binding.riskBadge.setBackgroundResource(bg)
            binding.riskBadge.setTextColor(
                ContextCompat.getColor(binding.root.context, textColor)
            )
            binding.riskBadge.text = food.riskLevel

            binding.deleteButton.setOnClickListener {
                onDelete(food)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<FoodLogEntity>() {
        override fun areItemsTheSame(oldItem: FoodLogEntity, newItem: FoodLogEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FoodLogEntity, newItem: FoodLogEntity): Boolean =
            oldItem == newItem
    }
}
