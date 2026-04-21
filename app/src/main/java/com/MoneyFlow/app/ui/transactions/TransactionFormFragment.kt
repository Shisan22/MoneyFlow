package com.MoneyFlow.app.ui.transactions

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.MoneyFlow.app.data.model.Transaction
import com.MoneyFlow.app.data.model.TransactionType
import com.MoneyFlow.app.databinding.FragmentTransactionFormBinding
import com.MoneyFlow.app.utils.showSnackbar
import java.text.SimpleDateFormat
import java.util.*

class TransactionFormFragment : Fragment() {

    private var _binding: FragmentTransactionFormBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionFormViewModel by viewModels()
    private val transactionId: String by lazy { arguments?.getString("transactionId") ?: "" }
    private var selectedDate = System.currentTimeMillis()

    private val categories = listOf("Alimentación","Transporte","Salud","Entretenimiento","Educación","Ropa","Hogar","Salario","Freelance","Otro")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransactionFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerCategory.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        updateDateDisplay()
        binding.btnPickDate.setOnClickListener {
            val cal = Calendar.getInstance().apply { timeInMillis = selectedDate }
            DatePickerDialog(requireContext(), { _, y, m, d ->
                cal.set(y, m, d); selectedDate = cal.timeInMillis; updateDateDisplay()
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        if (transactionId.isNotEmpty()) { binding.toolbar.title = "Editar Transacción"; viewModel.load(transactionId) }

        viewModel.transaction.observe(viewLifecycleOwner) { t ->
            t ?: return@observe
            binding.etTitle.setText(t.title)
            binding.etAmount.setText(t.amount.toString())
            binding.etNote.setText(t.note)
            selectedDate = t.date; updateDateDisplay()
            val idx = categories.indexOfFirst { it.equals(t.category, ignoreCase = true) }
            if (idx >= 0) binding.spinnerCategory.setSelection(idx)
            binding.rgType.check(if (t.type == TransactionType.INCOME) binding.rbIncome.id else binding.rbExpense.id)
        }

        binding.btnSave.setOnClickListener { save() }
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        viewModel.saved.observe(viewLifecycleOwner) { if (it) findNavController().navigateUp() }
    }

    private fun save() {
        val title = binding.etTitle.text.toString().trim()
        val amount = binding.etAmount.text.toString().toDoubleOrNull()
        if (title.isEmpty() || amount == null) { binding.root.showSnackbar("Completa título y monto"); return }
        viewModel.save(Transaction(
            id = transactionId, title = title, amount = amount,
            category = binding.spinnerCategory.selectedItem.toString(),
            type = if (binding.rbIncome.isChecked) TransactionType.INCOME else TransactionType.EXPENSE,
            note = binding.etNote.text.toString().trim(), date = selectedDate
        ))
    }

    private fun updateDateDisplay() {
        binding.tvDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDate))
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
