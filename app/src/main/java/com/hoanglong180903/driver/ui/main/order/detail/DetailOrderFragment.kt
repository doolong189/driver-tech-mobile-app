package com.hoanglong180903.driver.ui.main.order.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.BaseFragment
import com.hoanglong180903.driver.api.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.api.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.databinding.FragmentDetailOrderBinding
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource


class DetailOrderFragment : BaseFragment() {
    private lateinit var binding : FragmentDetailOrderBinding
    override var isVisibleActionBar: Boolean = false
    private val viewModel by viewModels<DetailOrderViewModel>()
    private var detailOrderAdapter = DetailOrderAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentDetailOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        binding.rcv.run { adapter = DetailOrderAdapter().also { detailOrderAdapter = it } }

        val orderId = arguments?.getString("orderId") ?: ""
        viewModel.getDetailOrder(GetDetailOrderRequest(id = orderId))
    }

    override fun setView() {
    }

    override fun setAction() {
    }

    override fun setObserve() {
        viewModel.getDetailOrderResult().observe(viewLifecycleOwner, Observer {
            getDetailOrderResult(it)
        })
    }

    private fun getDetailOrderResult(event : Event<Resource<GetDetailOrderResponse>>){
        event.getContentIfNotHandled()?.let { response ->
            when (response){
                is Resource.Success -> {
                    binding.pgBar.visibility = View.GONE
                    response.data?.let {
                        detailOrderAdapter.submitList(it.data?.products!!)
                        binding.orderId.text = "${requireContext().getString(R.string.order_id)} "+it.data?._id
                        if (it.data.receiptStatus == 0) {
                            binding.receiptStatus.setTextColor(binding.root.resources.getColor(R.color.text))
                            binding.receiptStatus.text = "Trạng thái: $processStatus"
                        }else if(it.data.receiptStatus == 1){
                            binding.receiptStatus.setTextColor(binding.root.resources.getColor(R.color.completed))
                            binding.receiptStatus.text = "Trạng thái: $completedStatus"
                        }else{
                            binding.receiptStatus.setTextColor(binding.root.resources.getColor(R.color.cancel))
                            binding.receiptStatus.text = "Trạng thái: $cancelStatus"
                        }
                        //store
                        Glide.with(binding.root.context)
                            .load(it.data.products[0].product.idUser.image)
                            .into(binding.imageStore)
                        binding.tvStore.text = it.data.products[0].product.idUser.name
                        binding.tvAddressStore.text = it.data.products[0].product.idUser.address
                        //client
                        Glide.with(binding.root.context)
                            .load(it.data.idClient.image)
                            .into(binding.imageClient)
                        binding.tvClient.text = it.data.idClient.name
                        binding.tvAddressClient.text = it.data.idClient.address
                        detailOrderAdapter.submitList(it.data.products)

                        //direction
                        binding.directionStore.setOnClickListener {
//                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse((("google.navigation:q=${21.1358758},${105.9104621}" + "&waypoints=" + 21.029061) + "," + 105.797435).toString() + "&mode=1"))
//                            intent.setPackage("com.google.android.apps.maps")
//                            if (intent.resolveActivity(requireActivity().packageManager) != null) {
//                                startActivity(intent)
//                            }
                            val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=${21.026227},${105.793893}&destination=${21.027120},${105.804512}")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            intent.setPackage("com.google.android.apps.maps")
                            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                                startActivity(intent)
                            }
                        }
                    }
                }
                is Resource.Loading -> {
                    binding.pgBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.pgBar.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        val processStatus = "Đang xử lý"
        val completedStatus = "Đã giao"
        val cancelStatus = "Đã hủy"
    }

}