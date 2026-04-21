package com.MoneyFlow.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.MoneyFlow.app.databinding.FragmentRegisterBinding
import com.MoneyFlow.app.ui.main.MainActivity
import com.MoneyFlow.app.utils.showSnackbar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirm = binding.etConfirmPassword.text.toString().trim()
            when {
                name.isEmpty() || email.isEmpty() || password.isEmpty() ->
                    binding.root.showSnackbar("Completa todos los campos")
                password != confirm ->
                    binding.root.showSnackbar("Las contraseñas no coinciden")
                password.length < 6 ->
                    binding.root.showSnackbar("La contraseña debe tener al menos 6 caracteres")
                else -> viewModel.register(name, email, password)
            }
        }

        binding.tvLogin.setOnClickListener { findNavController().navigateUp() }

        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is AuthState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                is AuthState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.root.showSnackbar(state.message)
                }
                else -> binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
