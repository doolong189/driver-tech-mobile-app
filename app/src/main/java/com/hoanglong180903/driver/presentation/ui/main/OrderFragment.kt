package com.hoanglong180903.driver.presentation.ui.main

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.Utils.Resource
import com.hoanglong180903.driver.Utils.SnackBar
import com.hoanglong180903.driver.databinding.FragmentOrderBinding
import com.hoanglong180903.driver.presentation.ui.main.viewmodel.OrderViewModel

class OrderFragment : Fragment() {
    private lateinit var binding : FragmentOrderBinding
    private lateinit var orderViewModel: OrderViewModel
    lateinit var orderAdapter : OrderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentOrderBinding.inflate(layoutInflater, container, false)
        (context as Activity?)!!.title = "Yêu Cầu Đơn Hàng"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        binding.orderRcView.setHasFixedSize(true)
        binding.orderRcView.layoutManager = LinearLayoutManager(requireContext())
        orderAdapter = OrderAdapter(requireContext())
        setupViewModel()
    }

    private fun setupViewModel() {
        orderViewModel = ViewModelProvider(
            this,
            OrderViewModel.OrdersViewModelFactory(requireActivity().application)
        )[OrderViewModel::class.java]
        getPictures()
    }

    private fun getPictures() {
        orderViewModel.ordersData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        orderAdapter.setOrderList(picsResponse)
                        binding.orderRcView.adapter = orderAdapter
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        SnackBar.showSnackBar(requireView(),message)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.orderPgLoading.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.orderPgLoading.visibility = View.VISIBLE
    }
}