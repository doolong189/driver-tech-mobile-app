package com.hoanglong180903.driver.ui.dashboard.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.hoanglong180903.driver.domain.enity.GetOrdersRequest
import com.hoanglong180903.driver.domain.enity.GetOrdersResponse
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.domain.enity.GetStatisticalRequest
import com.hoanglong180903.driver.domain.enity.GetStatisticalResponse
import com.hoanglong180903.driver.databinding.FragmentHomeBinding
import com.hoanglong180903.driver.ui.dashboard.order.OrderViewModel
import com.hoanglong180903.driver.ui.map.NavigationMapboxActivity
import com.hoanglong180903.driver.utils.Contacts
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.Utils
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : BaseFragment() {
    private lateinit var binding : FragmentHomeBinding
    override var isVisibleActionBar: Boolean = false
    private val viewModel by activityViewModels<HomeViewModel>()
    private val orderViewModel by activityViewModels<OrderViewModel>()
    private var socket: Socket? = null
    private var homeAdapter = HomeAdapter()
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
        binding.orderList.setHasFixedSize(true)
        binding.orderList.layoutManager = LinearLayoutManager(requireContext())
        binding.orderList.run { adapter = HomeAdapter().also { homeAdapter = it } }
        try {
            socket = IO.socket(Contacts.SOCKET_URL)
            socket?.connect()
            Log.e("zzzz","socket connect")
            socket?.emit("join", "66e2faac041c84e872801234")
        } catch (e : Exception) {
            e.printStackTrace()
        }

        socket?.on("userjoinedthechat") { args ->
            activity?.runOnUiThread {
                val data = args[0] as String
                Log.e("zzzz","$data")
            }
        }

        socket!!.on("message") { args ->
            activity?.runOnUiThread {
                val data = args[0] as JSONObject
                try {
                    val nickname = data.getString("senderNickname")
                    Log.e("zzzz","$nickname")
                    orderViewModel.getOrders(GetOrdersRequest(receiptStatus =  0))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

    }

    override fun setView() {
    }

    override fun setAction() {
        homeAdapter.directionMap { id, position ->
            startActivity(Intent(requireActivity(), NavigationMapboxActivity::class.java))
        }
    }

    override fun setObserve() {
        viewModel.getStatisticalResult().observe(viewLifecycleOwner, Observer {
            getStatisticalResult(it)
        })
        orderViewModel.getOrderResult().observe(viewLifecycleOwner, Observer{
            getOrderResult(it)
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

    private fun getOrderResult(event : Event<Resource<GetOrdersResponse>>){
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
                    response.data?.let { picsResponse ->
                        homeAdapter.submitList(picsResponse.data!!)
                    }
                }
            }
        }
    }
}