package com.hoanglong180903.driver.ui.main.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.data.enity.GetOrdersRequest
import com.hoanglong180903.driver.data.enity.GetOrdersResponse
import com.hoanglong180903.driver.data.enity.UpdateOrderShipperRequest
import com.hoanglong180903.driver.utils.Resource
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
        orderViewModel.getOrders(GetOrdersRequest(receiptStatus =  0))
    }

    override fun setView() {
        binding.orderPgLoading.visibility = View.VISIBLE
    }

    override fun setAction() {
        orderAdapter.showDetailNotification { id, position ->
            val bundle = Bundle().apply { putString("orderId", id._id) }
            findNavController().navigate(R.id.action_orderFragment_to_detailOrderFragment , bundle)
        }

        orderAdapter.onClickItemOrderAccept { id, position ->
            orderViewModel.updateOrderShipper(UpdateOrderShipperRequest(
                orderId = id._id,
                idShipper = "66e2faac041c84e872801234"
            ))
        }

        orderAdapter.onClickItemOrderCancel { id, position ->

        }
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
                    }
                }
            }
        }
    }
}