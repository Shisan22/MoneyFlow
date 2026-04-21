package com.MoneyFlow.app.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.MoneyFlow.app.R
import com.MoneyFlow.app.databinding.FragmentTransactionsBinding

class TransactionsFragment : Fragment() {

    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TransactionListAdapter(
            onItemClick = { t ->
                val bundle = Bundle().apply { putString("transactionId", t.id) }
                findNavController().navigate(R.id.transactionDetailFragment, bundle)
            },
            onDeleteClick = { t -> viewModel.delete(t.id) }
        )
        binding.rvTransactions.adapter = adapter

        viewModel.transactions.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabAdd.setOnClickListener { findNavController().navigate(R.id.transactionFormFragment) }
        binding.chipAll.setOnCheckedChangeListener { _, checked -> if (checked) viewModel.filter(null) }
        binding.chipIncome.setOnCheckedChangeListener { _, checked -> if (checked) viewModel.filter("INCOME") }
        binding.chipExpense.setOnCheckedChangeListener { _, checked -> if (checked) viewModel.filter("EXPENSE") }

        viewModel.loadTransactions()
    }

    override fun onResume() { super.onResume(); viewModel.loadTransactions() }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
