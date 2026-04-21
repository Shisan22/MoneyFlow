package com.MoneyFlow.app.ui.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.MoneyFlow.app.R
import com.MoneyFlow.app.data.model.Transaction
import com.MoneyFlow.app.data.model.TransactionType
import com.MoneyFlow.app.databinding.ItemTransactionBinding
import com.MoneyFlow.app.utils.formatCurrency
import java.text.SimpleDateFormat
import java.util.*

class TransactionListAdapter(
    private val onItemClick: (Transaction) -> Unit,
    private val onDeleteClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionListAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(a: Transaction, b: Transaction) = a.id == b.id
            override fun areContentsTheSame(a: Transaction, b: Transaction) = a == b
        }
    }

    inner class VH(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val ctx = holder.binding.root.context
        val fmt = SimpleDateFormat("dd MMM yyyy", Locale("es"))
        val isIncome = item.type == TransactionType.INCOME

        holder.binding.apply {
            tvTitle.text = item.title
            tvCategory.text = item.category
            tvDate.text = fmt.format(Date(item.date))
            tvAmount.text = "${if (isIncome) "+" else "-"}${item.amount.formatCurrency()}"
            tvAmount.setTextColor(
                ContextCompat.getColor(ctx, if (isIncome) R.color.income_green else R.color.expense_red)
            )
            root.setOnClickListener { onItemClick(item) }
            root.setOnLongClickListener { onDeleteClick(item); true }
        }
    }
}
