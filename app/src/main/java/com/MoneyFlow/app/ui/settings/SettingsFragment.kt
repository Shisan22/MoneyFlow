package com.MoneyFlow.app.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.MoneyFlow.app.data.repository.AuthRepository
import com.MoneyFlow.app.databinding.FragmentSettingsBinding
import com.MoneyFlow.app.ui.auth.AuthActivity
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val authRepo = AuthRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        lifecycleScope.launch {
            val user = authRepo.getUserData(uid)
            binding.tvUserName.text = user?.name ?: "Usuario"
            binding.tvUserEmail.text = user?.email ?: ""
        }

        binding.switchNotifications.isChecked = true
        binding.btnLogout.setOnClickListener {
            authRepo.logout()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
