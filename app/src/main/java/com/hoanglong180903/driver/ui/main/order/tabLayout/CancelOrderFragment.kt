package com.hoanglong180903.driver.ui.main.order.tabLayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.data.requestmodel.GetOrderShipIDRequest
import com.hoanglong180903.driver.data.responsemodel.GetOrderShipIDResponse
import com.hoanglong180903.driver.databinding.FragmentCancelOrderBinding
import com.hoanglong180903.driver.ui.main.order.OrderAdapter
import com.hoanglong180903.driver.ui.main.order.OrderViewModel
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.PopupUtils
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.SharedPreferences


class CancelOrderFragment : BaseFragment<FragmentCancelOrderBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCancelOrderBinding
        get() = FragmentCancelOrderBinding::inflate
    override var isShowHideActionBar: Boolean = false
    private val viewModel by activityViewModels<OrderViewModel>()
    private var orderAdapter = OrderAdapter()
    private lateinit var preferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun initView() {
        binding.cancelOrderRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.cancelOrderRcView.run { adapter = OrderAdapter().also { orderAdapter = it } }
    }

    override fun initData() {
        preferences = SharedPreferences(requireContext())
        viewModel.getOrdersShipID(GetOrderShipIDRequest(id = preferences.userId , receiptStatus = 3))
    }

    override fun initEvents() {
    }

    override fun initObserve() {
        viewModel.getCancelOrderResult().observe(viewLifecycleOwner, Observer {
            getOrderShipIDResult(it)
        })
    }
    private fun getOrderShipIDResult(event : Event<Resource<GetOrderShipIDResponse>>){
        event.getContentIfNotHandled()?.let { response ->
            when (response) {
                is Resource.Success -> {
                    binding.cancelOrderPbBar.visibility = View.GONE
                    response.data?.let {
                        orderAdapter.setOrderList(it.data!!)
                    }
                }
                is Resource.Loading -> {
                    binding.cancelOrderPbBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.cancelOrderPbBar.visibility = View.GONE
                    response.message?.let { PopupUtils.showToast(requireContext(), it) }
                }
            }
        }
    }
}