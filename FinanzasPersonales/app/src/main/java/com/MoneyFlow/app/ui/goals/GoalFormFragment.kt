package com.MoneyFlow.app.ui.goals

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.MoneyFlow.app.data.model.Goal
import com.MoneyFlow.app.databinding.FragmentGoalFormBinding
import com.MoneyFlow.app.utils.showSnackbar
import java.text.SimpleDateFormat
import java.util.*

class GoalFormFragment : Fragment() {

    private var _binding: FragmentGoalFormBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GoalFormViewModel by viewModels()
    private val goalId: String by lazy { arguments?.getString("goalId") ?: "" }
    private var selectedDeadline = 0L

    private val categories = listOf("Ahorro","Viaje","Educación","Vehículo","Hogar","Emergencia","Otro")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGoalFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerCategory.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.btnPickDeadline.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                cal.set(y, m, d); selectedDeadline = cal.timeInMillis
                binding.tvDeadline.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDeadline))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        if (goalId.isNotEmpty()) { binding.toolbar.title = "Editar Meta"; viewModel.load(goalId) }

        viewModel.goal.observe(viewLifecycleOwner) { g ->
            g ?: return@observe
            binding.etTitle.setText(g.title)
            binding.etTarget.setText(g.targetAmount.toString())
            binding.etCurrent.setText(g.currentAmount.toString())
            selectedDeadline = g.deadline
            val idx = categories.indexOfFirst { it.equals(g.category, ignoreCase = true) }
            if (idx >= 0) binding.spinnerCategory.setSelection(idx)
            if (g.deadline > 0) binding.tvDeadline.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(g.deadline))
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val target = binding.etTarget.text.toString().toDoubleOrNull()
            if (title.isEmpty() || target == null) { binding.root.showSnackbar("Completa título y monto objetivo"); return@setOnClickListener }
            viewModel.save(Goal(id = goalId, title = title, targetAmount = target,
                currentAmount = binding.etCurrent.text.toString().toDoubleOrNull() ?: 0.0,
                category = binding.spinnerCategory.selectedItem.toString(), deadline = selectedDeadline))
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        viewModel.saved.observe(viewLifecycleOwner) { if (it) findNavController().navigateUp() }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
