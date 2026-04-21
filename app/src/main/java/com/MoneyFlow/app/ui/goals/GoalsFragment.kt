package com.MoneyFlow.app.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.MoneyFlow.app.R
import com.MoneyFlow.app.databinding.FragmentGoalsBinding

class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GoalsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GoalAdapter(
            onItemClick = { goal ->
                val bundle = Bundle().apply { putString("goalId", goal.id) }
                findNavController().navigate(R.id.goalDetailFragment, bundle)
            },
            onDeleteClick = { goal -> viewModel.delete(goal.id) }
        )
        binding.rvGoals.adapter = adapter

        viewModel.goals.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabAdd.setOnClickListener { findNavController().navigate(R.id.goalFormFragment) }
        viewModel.loadGoals()
    }

    override fun onResume() { super.onResume(); viewModel.loadGoals() }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
