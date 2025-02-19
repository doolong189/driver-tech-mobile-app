package com.hoanglong180903.driver.ui.main.order

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoanglong180903.driver.common.BaseFragment
import com.hoanglong180903.driver.data.enity.GetOrdersRequest
import com.hoanglong180903.driver.data.enity.GetOrdersResponse
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.SnackBar
import com.hoanglong180903.driver.databinding.FragmentOrderBinding
import com.hoanglong180903.driver.utils.Event

class OrderFragment : BaseFragment() {
    private lateinit var binding : FragmentOrderBinding
    private val orderViewModel by activityViewModels<OrderViewModel>()
    private var orderAdapter = OrderAdapter()
    override var isVisibleActionBar: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initView() {
        binding.orderRcView.setHasFixedSize(true)
        binding.orderRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.orderRcView.run { adapter = OrderAdapter().also { orderAdapter = it } }
        orderViewModel.getOrders(GetOrdersRequest(receiptStatus =  1))
    }

    override fun setView() {
    }

    override fun setAction() {
    }

    override fun setObserve() {
        orderViewModel.getOrderResult().observe(viewLifecycleOwner , Observer {
            getOrderResult(it)
        })
    }

    private fun getOrderResult(event : Event<Resource<GetOrdersResponse>>){
        event.getContentIfNotHandled()?.let { response ->
            when (response){
                is Resource.Error -> {
                    binding.orderPgLoading.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.orderPgLoading.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.orderPgLoading.visibility = View.GONE
                    response.data?.let { picsResponse ->
                        orderAdapter.setOrderList(picsResponse.data!!)
                        binding.orderRcView.adapter = orderAdapter
                    }
                }
            }
        }
    }
}