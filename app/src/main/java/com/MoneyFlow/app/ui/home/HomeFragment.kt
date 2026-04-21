package com.MoneyFlow.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.MoneyFlow.app.R
import com.MoneyFlow.app.databinding.FragmentHomeBinding
import com.MoneyFlow.app.utils.formatCurrency
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("es", "CO"))
        binding.tvMonth.text = dateFormat.format(Date()).replaceFirstChar { it.uppercase() }

        viewModel.summary.observe(viewLifecycleOwner) { summary ->
            binding.tvBalance.text = summary.balance.formatCurrency()
            binding.tvIncome.text = summary.totalIncome.formatCurrency()
            binding.tvExpense.text = summary.totalExpense.formatCurrency()
        }

        viewModel.recentTransactions.observe(viewLifecycleOwner) { list ->
            val adapter = RecentTransactionAdapter(list) { transaction ->
                val bundle = Bundle().apply { putString("transactionId", transaction.id) }
                findNavController().navigate(R.id.transactionDetailFragment, bundle)
            }
            binding.rvRecent.adapter = adapter
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.transactionFormFragment)
        }

        viewModel.loadData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
