package com.hoanglong180903.driver.ui.account.verify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.databinding.FragmentPasswordInputBinding

class PasswordInputFragment : BaseFragment<FragmentPasswordInputBinding>() {
    override var isShowHideActionBar: Boolean = false
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPasswordInputBinding
        get() = FragmentPasswordInputBinding::inflate

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initEvents() {
    }

    override fun initObserve() {
    }


}