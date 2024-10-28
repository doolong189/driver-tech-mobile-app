package com.hoanglong180903.driver.presentation.ui.account.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hoanglong180903.driver.MainActivity
import com.hoanglong180903.driver.databinding.FragmentSplashBinding
import com.hoanglong180903.driver.presentation.ui.account.login.SignInFragment
import com.hoanglong180903.driver.presentation.ui.account.register.SignUpFragment

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
            (activity as MainActivity).replaceFragment(SignInFragment())
        }

        binding.splashBtRegister.setOnClickListener {
            (activity as MainActivity).replaceFragment(SignUpFragment())
        }
    }
}