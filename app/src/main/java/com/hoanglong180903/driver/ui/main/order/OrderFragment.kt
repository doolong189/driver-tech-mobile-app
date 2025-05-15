package com.hoanglong180903.driver.ui.main.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.data.requestmodel.UpdateOrderShipperRequest
import com.hoanglong180903.driver.databinding.FragmentOrderBinding

class OrderFragment : BaseFragment<FragmentOrderBinding>() {
    override var isShowHideActionBar: Boolean = false
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrderBinding
        get() = FragmentOrderBinding::inflate
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private val orderViewModel by activityViewModels<OrderViewModel>()
    private var orderAdapter = OrderAdapter()

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initEvents() {
        orderAdapter.showDetailNotification { id, position ->
            val bundle = Bundle().apply { putString("orderId", id._id) }
            findNavController().navigate(R.id.action_orderFragment_to_detailOrderFragment , bundle)
        }

        orderAdapter.onClickItemOrderAccept { id, position ->
            orderViewModel.updateOrderShipper(
                UpdateOrderShipperRequest(
                orderId = id._id,
                idShipper = "66e2faac041c84e872801234"))
        }

        orderAdapter.onClickItemOrderCancel { id, position ->
        }
    }

    override fun initObserve() {
    }
}