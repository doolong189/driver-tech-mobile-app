package com.hoanglong180903.driver.ui.account.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private lateinit var binding:  FragmentSplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        action()
    }

    private fun action(){
        binding.splashBtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
        }

        binding.splashBtRegister.setOnClickListener {
            findNavController().navigate(R.id.action_splashFragment_to_signUpFragment)
        }
    }
}