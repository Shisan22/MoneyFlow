package com.MoneyFlow.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.MoneyFlow.app.R
import com.MoneyFlow.app.data.model.Transaction
import com.MoneyFlow.app.data.model.TransactionType
import com.MoneyFlow.app.databinding.ItemTransactionBinding
import com.MoneyFlow.app.utils.formatCurrency
import java.text.SimpleDateFormat
import java.util.*

class RecentTransactionAdapter(
    private val items: List<Transaction>,
    private val onClick: (Transaction) -> Unit
) : RecyclerView.Adapter<RecentTransactionAdapter.VH>() {

    inner class VH(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val ctx = holder.binding.root.context
        val fmt = SimpleDateFormat("dd MMM", Locale("es"))
        val isIncome = item.type == TransactionType.INCOME

        holder.binding.apply {
            tvTitle.text = item.title
            tvCategory.text = item.category
            tvDate.text = fmt.format(Date(item.date))
            tvAmount.text = "${if (isIncome) "+" else "-"}${item.amount.formatCurrency()}"
            tvAmount.setTextColor(
                ContextCompat.getColor(ctx, if (isIncome) R.color.income_green else R.color.expense_red)
            )
            ivCategory.setImageResource(getCategoryIcon(item.category))
            root.setOnClickListener { onClick(item) }
        }
    }

    private fun getCategoryIcon(category: String) = when (category.lowercase()) {
        "alimentación", "comida" -> R.drawable.ic_food
        "transporte" -> R.drawable.ic_transport
        "salud" -> R.drawable.ic_health
        "entretenimiento" -> R.drawable.ic_entertainment
        "salario", "ingreso", "freelance" -> R.drawable.ic_income
        else -> R.drawable.ic_other
    }
}
