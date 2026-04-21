package com.MoneyFlow.app.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.MoneyFlow.app.R
import com.MoneyFlow.app.data.model.TransactionType
import com.MoneyFlow.app.databinding.FragmentTransactionDetailBinding
import com.MoneyFlow.app.utils.formatCurrency
import java.text.SimpleDateFormat
import java.util.*

class TransactionDetailFragment : Fragment() {

    private var _binding: FragmentTransactionDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionDetailViewModel by viewModels()
    private val transactionId: String by lazy { arguments?.getString("transactionId") ?: "" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load(transactionId)

        viewModel.transaction.observe(viewLifecycleOwner) { t ->
            t ?: return@observe
            val fmt = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es"))
            val isIncome = t.type == TransactionType.INCOME
            binding.apply {
                tvTitle.text = t.title
                tvAmount.text = "${if (isIncome) "+" else "-"}${t.amount.formatCurrency()}"
                tvAmount.setTextColor(ContextCompat.getColor(requireContext(), if (isIncome) R.color.income_green else R.color.expense_red))
                tvType.text = if (isIncome) "Ingreso" else "Gasto"
                tvCategory.text = t.category
                tvDate.text = fmt.format(Date(t.date))
                tvNote.text = t.note.ifEmpty { "Sin nota" }
            }
        }

        binding.btnEdit.setOnClickListener {
            val bundle = Bundle().apply { putString("transactionId", transactionId) }
            findNavController().navigate(R.id.transactionFormFragment, bundle)
        }
        binding.btnDelete.setOnClickListener {
            viewModel.delete(transactionId)
            findNavController().navigateUp()
        }
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
