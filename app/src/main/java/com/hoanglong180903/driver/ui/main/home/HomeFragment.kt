package com.hoanglong180903.driver.ui.main.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.BaseFragment
import com.hoanglong180903.driver.data.enity.GetStatisticalRequest
import com.hoanglong180903.driver.data.enity.GetStatisticalResponse
import com.hoanglong180903.driver.databinding.FragmentHomeBinding
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.Utils

class HomeFragment : BaseFragment() {
    private lateinit var binding : FragmentHomeBinding
    override var isVisibleActionBar: Boolean = false
    private val viewModel by activityViewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initView() {
        viewModel.getStatistical(GetStatisticalRequest(idShipper = "66e2faac041c84e872801234"))
    }

    override fun setView() {
    }

    override fun setAction() {
    }

    override fun setObserve() {
        viewModel.getStatisticalResult().observe(viewLifecycleOwner, Observer {
            getStatisticalResult(it)
        })
    }

    private fun getStatisticalResult(event : Event<Resource<GetStatisticalResponse>>){
        event.getContentIfNotHandled()?.let { response ->
            when ( response ){
                is Resource.Error -> {
                    binding.pbBar.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.pbBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.pbBar.visibility = View.GONE
                    response.data?.let {
                        binding.totalOrdersCount.text = it.totalOrdersCount.toString()
                        binding.totalReceivedAmount.text = Utils.formatPrice(it.totalReceivedAmount!!) + "đ"
                        binding.completedOrdersCount.text = it.completedOrdersCount.toString()
                        binding.canceledOrdersCount.text = it.canceledOrdersCount.toString()
                    }
                }
            }
        }
    }
}