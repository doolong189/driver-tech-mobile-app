package com.hoanglong180903.driver.ui.main.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.data.requestmodel.GetShipperInfoRequest
import com.hoanglong180903.driver.data.responsemodel.GetShipperInfoResponse
import com.hoanglong180903.driver.databinding.FragmentUserBinding
import com.hoanglong180903.driver.model.UserInfo
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.SharedPreferences


class UserFragment : BaseFragment<FragmentUserBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUserBinding
        get() = FragmentUserBinding::inflate
    override var isShowHideActionBar: Boolean = false
    private val userViewModel by activityViewModels<UserViewModel>()
    private lateinit var preferces : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun initView() {
    }

    override fun initData() {
        preferces = SharedPreferences(requireContext())
        userViewModel.getShipperInfo(GetShipperInfoRequest(id = preferces.userId))
    }

    override fun initEvents() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, checked ->
            when {
                checked -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    override fun initObserve() {
        userViewModel.getShipperInfoResult().observe(viewLifecycleOwner , Observer {
            getShipperInfoResult(it)
        })
    }

    private fun getShipperInfoResult(event: Event<Resource<GetShipperInfoResponse>>){
        event.getContentIfNotHandled()?.let { response ->
            when (response){
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    response.data?.shipper?.let {
                        userViewModel.getConvertShipperInfo(it)
                        setViewShipperInfo(it)
                    }
                }
            }
        }
    }

    private fun setViewShipperInfo(shipperInfo : UserInfo){
        binding.tvFullName.text = shipperInfo.name
        binding.tvEmail.text = shipperInfo.email
    }

}