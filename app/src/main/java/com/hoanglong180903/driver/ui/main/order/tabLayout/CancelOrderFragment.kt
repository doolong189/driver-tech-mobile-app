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
import com.hoanglong180903.driver.data.enity.GetOrderShipIDRequest
import com.hoanglong180903.driver.data.enity.GetOrderShipIDResponse
import com.hoanglong180903.driver.databinding.FragmentCancelOrderBinding
import com.hoanglong180903.driver.ui.main.order.OrderAdapter
import com.hoanglong180903.driver.ui.main.order.OrderViewModel
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.SharedPreferences


class CancelOrderFragment : BaseFragment() {

    override var isVisibleActionBar: Boolean = false
    private lateinit var binding : FragmentCancelOrderBinding
    private val viewModel by activityViewModels<OrderViewModel>()
    private var orderAdapter = OrderAdapter()
    private lateinit var preferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCancelOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun initView() {
        preferences = SharedPreferences(requireContext())
        binding.cancelOrderRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.cancelOrderRcView.run { adapter = OrderAdapter().also { orderAdapter = it } }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getOrdersShipID(GetOrderShipIDRequest(idShipper = preferences.userId , receiptStatus = 3))
    }
    override fun setView() {
    }

    override fun setAction() {
//        orderAdapter.onClickItemOrder { id, position ->
//            val bundle = Bundle().apply { putString("orderId", id._id) }
//            findNavController().navigate(R.id.action_orderFragment_to_detailOrderFragment,bundle)
//        }
    }

    override fun setObserve() {
        viewModel.getCancelOrderResult().observe(viewLifecycleOwner, Observer {
            getOrderShipID(it)
        })
    }
    private fun getOrderShipID(event : Event<Resource<GetOrderShipIDResponse>>){
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
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}