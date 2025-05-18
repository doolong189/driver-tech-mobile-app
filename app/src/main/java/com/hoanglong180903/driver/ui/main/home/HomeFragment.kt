package com.hoanglong180903.driver.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.data.requestmodel.GetNewOrderRequest
import com.hoanglong180903.driver.data.responsemodel.GetNewOrderResponse
import com.hoanglong180903.driver.data.requestmodel.GetStatisticalRequest
import com.hoanglong180903.driver.data.responsemodel.GetStatisticalResponse
import com.hoanglong180903.driver.databinding.FragmentHomeBinding
import com.hoanglong180903.driver.ui.main.MainActivity
import com.hoanglong180903.driver.ui.main.user.UserViewModel
import com.hoanglong180903.driver.ui.map.MapActivity
import com.hoanglong180903.driver.ui.map.NavigationMapActivity
import com.hoanglong180903.driver.utils.Constants
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.SocketIOManager
import com.hoanglong180903.driver.utils.Utils


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel by activityViewModels<HomeViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()
    private var homeAdapter = HomeAdapter()
    private var socketIO = SocketIOManager()
    override var isShowHideActionBar: Boolean = false


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override fun initView() {
        binding.orderList.setHasFixedSize(true)
        binding.orderList.layoutManager = LinearLayoutManager(requireContext())
        binding.orderList.run { adapter = HomeAdapter().also { homeAdapter = it } }
        (activity as MainActivity).isShowHideBottomNav(true)
    }

    override fun initData() {
        viewModel.getStatistical(GetStatisticalRequest(idShipper = userViewModel.getShipperInfo?._id))
        viewModel.getNewOrder(GetNewOrderRequest(receiptStatus =  0))

    }

    override fun initEvents() {
        binding.switchOnOff.setOnCheckedChangeListener { _, checked ->
            when {
                checked -> {
                    socketIO.connect()
                    socketIO.join(userViewModel.getShipperInfo?._id.toString())
                    socketIO.userJoinedTheChat()
                    val message = socketIO.message()
                    binding.switchOnOff.text = "Sẵn sàng"
                }
                else -> {
                    socketIO.disconnect(userViewModel.getShipperInfo?._id.toString())
                    binding.switchOnOff.text = ""
                }
            }
        }


        homeAdapter.directionMap { id, position ->
            val fromLocation = ArrayList<Double>()
            fromLocation.add(id.fromLocation?.get(1) ?: 0.0)
            fromLocation.add(id.fromLocation?.get(0) ?: 0.0)

            val toLocation = ArrayList<Double>()
            toLocation.add(id.toLocation?.get(1) ?: 0.0)
            toLocation.add(id.toLocation?.get(0) ?: 0.0)
            val mIntent = Intent(requireActivity(), NavigationMapActivity::class.java)
            mIntent.putExtra(FROM_LOCATION, fromLocation)
            mIntent.putExtra(TO_LOCATION, toLocation)
            Log.e(Constants.TAG,"${fromLocation} - ${toLocation}")
            startActivity(mIntent)
        }

        homeAdapter.detailOrder { id, position ->
            val bundle = Bundle().apply { putString(ORDER_ID, id._id) }
            findNavController().navigate(R.id.action_homeFragment_to_detailOrderFragment , bundle)
            (activity as MainActivity).isShowHideBottomNav(false)
        }
    }

    override fun initObserve() {
        viewModel.getStatisticalResult().observe(viewLifecycleOwner, Observer {
            getStatisticalResult(it)
        })
        viewModel.getNewOrderResult().observe(viewLifecycleOwner, Observer{
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

    private fun getOrderResult(event : Event<Resource<GetNewOrderResponse>>){
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

    companion object{
        const val ORDER_ID = "orderId"
        const val FROM_LOCATION = "fromLocation"
        const val TO_LOCATION = "toLocation"
    }
}