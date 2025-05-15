package com.hoanglong180903.driver.ui.main.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.databinding.FragmentUserInfoBinding


class UserInfoFragment : BaseFragment<FragmentUserInfoBinding>() {

    override var isShowHideActionBar: Boolean = false
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUserInfoBinding
        get() = FragmentUserInfoBinding::inflate

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initEvents() {
    }

    override fun initObserve() {
    }


}