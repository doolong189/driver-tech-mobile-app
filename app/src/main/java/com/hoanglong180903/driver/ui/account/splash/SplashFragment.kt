package com.hoanglong180903.driver.ui.account.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.databinding.FragmentSplashBinding

class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override var isShowHideActionBar: Boolean = false

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initEvents() {
        binding.splashBtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
        }

        binding.splashBtRegister.setOnClickListener {
            findNavController().navigate(R.id.action_splashFragment_to_signUpFragment)
        }    }

    override fun initObserve() {
    }

}