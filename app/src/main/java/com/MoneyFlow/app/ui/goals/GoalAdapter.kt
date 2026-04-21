package com.MoneyFlow.app.ui.goals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.MoneyFlow.app.data.model.Goal
import com.MoneyFlow.app.databinding.ItemGoalBinding
import com.MoneyFlow.app.utils.formatCurrency
import java.text.SimpleDateFormat
import java.util.*

class GoalAdapter(
    private val onItemClick: (Goal) -> Unit,
    private val onDeleteClick: (Goal) -> Unit
) : ListAdapter<Goal, GoalAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Goal>() {
            override fun areItemsTheSame(a: Goal, b: Goal) = a.id == b.id
            override fun areContentsTheSame(a: Goal, b: Goal) = a == b
        }
    }

    inner class VH(val binding: ItemGoalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemGoalBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.binding.apply {
            tvGoalTitle.text = item.title
            tvGoalCategory.text = item.category
            tvGoalProgress.text = "${item.currentAmount.formatCurrency()} / ${item.targetAmount.formatCurrency()}"
            progressBar.progress = item.progress
            tvPercent.text = "${item.progress}%"
            tvDeadline.text = if (item.deadline > 0) "Meta: ${fmt.format(Date(item.deadline))}" else ""
            root.setOnClickListener { onItemClick(item) }
            root.setOnLongClickListener { onDeleteClick(item); true }
        }
    }
}
