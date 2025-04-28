package com.hoanglong180903.driver.ui.dashboard.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.data.enity.GetShipperInfoRequest
import com.hoanglong180903.driver.data.enity.GetShipperInfoResponse
import com.hoanglong180903.driver.databinding.FragmentUserBinding
import com.hoanglong180903.driver.ui.dashboard.order.OrderViewModel
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource


class UserFragment : BaseFragment() {
    private lateinit var binding : FragmentUserBinding
    override var isVisibleActionBar: Boolean = false

    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun initView() {
        userViewModel.getShipperInfo(GetShipperInfoRequest(id = "123"))
    }

    override fun setView() {

    }

    override fun setAction() {
    }

    override fun setObserve() {
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
                    }
                }
            }
        }
    }

}