package com.hoanglong180903.driver.ui.account.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.databinding.FragmentSignUpBinding

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignUpBinding
        get() = FragmentSignUpBinding::inflate

    override var isShowHideActionBar: Boolean = false

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initEvents() {
    }

    override fun initObserve() {
    }
}