package com.MoneyFlow.app.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.MoneyFlow.app.R
import com.MoneyFlow.app.databinding.FragmentGoalDetailBinding
import com.MoneyFlow.app.utils.formatCurrency
import java.text.SimpleDateFormat
import java.util.*

class GoalDetailFragment : Fragment() {

    private var _binding: FragmentGoalDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GoalDetailViewModel by viewModels()
    private val goalId: String by lazy { arguments?.getString("goalId") ?: "" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGoalDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load(goalId)

        viewModel.goal.observe(viewLifecycleOwner) { g ->
            g ?: return@observe
            val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.apply {
                tvTitle.text = g.title
                tvCategory.text = g.category
                tvTarget.text = g.targetAmount.formatCurrency()
                tvCurrent.text = g.currentAmount.formatCurrency()
                tvPercent.text = "${g.progress}%"
                progressBar.progress = g.progress
                tvDeadline.text = if (g.deadline > 0) fmt.format(Date(g.deadline)) else "Sin fecha"
            }
        }

        binding.btnEdit.setOnClickListener {
            val bundle = Bundle().apply { putString("goalId", goalId) }
            findNavController().navigate(R.id.goalFormFragment, bundle)
        }
        binding.btnDelete.setOnClickListener { viewModel.delete(goalId); findNavController().navigateUp() }
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
