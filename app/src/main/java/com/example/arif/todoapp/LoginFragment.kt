package com.example.arif.todoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.arif.todoapp.databinding.FragmentLoginBinding
import com.example.arif.todoapp.entities.UserModel
import com.example.arif.todoapp.prefdata.LoginPreference
import com.example.arif.todoapp.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var preference: LoginPreference
    private lateinit var user: UserModel
    private val loginViewModel: LoginViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preference = LoginPreference(requireContext())
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        loginViewModel.errMsgLD.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }
        binding.loginBtn.setOnClickListener {
            val email = binding.emailInputET.text.toString()
            val password = binding.passwordInputET.text.toString()
            loginViewModel.login(email, password) {
                lifecycle.coroutineScope.launch {
                    preference.setLoginStatus(true, it, requireContext())
                    findNavController().popBackStack()
                }
            }
        }
        binding.regBtn.setOnClickListener {
            val email = binding.emailInputET.text.toString()
            val password = binding.passwordInputET.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireActivity(), "Please fill up the input", Toast.LENGTH_LONG)
                    .show()
            } else {
                user = UserModel(
                    email = email, password = password
                )
                loginViewModel.register(user) {
                    lifecycle.coroutineScope.launch {
                        preference.setLoginStatus(true, it, requireContext())
                        findNavController().popBackStack()
                    }
                }
            }


        }
        return binding.root
    }
}