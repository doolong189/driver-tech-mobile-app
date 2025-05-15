package com.hoanglong180903.driver.ui.main.order.tabLayout

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
import com.hoanglong180903.driver.databinding.FragmentOngoingOrderBinding
import com.hoanglong180903.driver.ui.main.order.OrderAdapter
import com.hoanglong180903.driver.ui.main.order.OrderViewModel
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.PopupUtils
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.SharedPreferences


class OnGoingOrderFragment : BaseFragment<FragmentOngoingOrderBinding>() {
    override var isShowHideActionBar: Boolean = false
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOngoingOrderBinding
        get() = FragmentOngoingOrderBinding::inflate
    private val viewModel by activityViewModels<OrderViewModel>()
    private var orderAdapter = OrderAdapter()
    private lateinit var preferences : SharedPreferences

    override fun initView() {
        binding.ongoingOrderRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.ongoingOrderRcView.run { adapter = OrderAdapter().also { orderAdapter = it } }
    }

    override fun initData() {
        preferences = SharedPreferences(requireContext())
        viewModel.getOrdersShipID(GetOrderShipIDRequest(idShipper = preferences.userId , receiptStatus = 1))
    }

    override fun initEvents() {
    }

    override fun initObserve() {
        viewModel.getOnGoingOrderResult().observe(viewLifecycleOwner, Observer {
            getOrderShipID(it)
        })
    }

    private fun getOrderShipID(event : Event<Resource<GetOrderShipIDResponse>>){
        event.getContentIfNotHandled()?.let { response ->
            when (response) {
                is Resource.Success -> {
                    binding.ongoingOrderPbBar.visibility = View.GONE
                    response.data?.let {
                        orderAdapter.setOrderList(it.data!!)
                    }
                }
                is Resource.Loading -> {
                    binding.ongoingOrderPbBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.ongoingOrderPbBar.visibility = View.GONE
                    response.message?.let { PopupUtils.showToast(requireContext(), it) }
                }
            }
        }
    }
}