package com.hoanglong180903.driver.ui.main.order

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.data.enity.UpdateOrderShipperRequest
import com.hoanglong180903.driver.databinding.FragmentOrderBinding

class OrderFragment : BaseFragment() {
    private lateinit var binding : FragmentOrderBinding
    private val orderViewModel by activityViewModels<OrderViewModel>()
    private var orderAdapter = OrderAdapter()
    override var isVisibleActionBar: Boolean = false
    private lateinit var tabLayoutMediator: TabLayoutMediator
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
    }

    override fun setView() {
        binding.orderViewPager2.isUserInputEnabled = false
        binding.orderViewPager2.adapter = ViewPager2Adapter(requireActivity())

        tabLayoutMediator = TabLayoutMediator(binding.orderTabLayout, binding.orderViewPager2) { tab, position ->
            // Tạo một TextView tùy chỉnh cho mỗi tab
            val tabTextView = TextView(requireContext()).apply {
                text = when (position) {
                    0 -> getString(R.string.on_going_order)
                    1 -> getString(R.string.completed_order)
                    2 -> getString(R.string.cancel_order)
                    else -> ""
                }
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                gravity = Gravity.CENTER
            }
            tab.customView = tabTextView
        }
        tabLayoutMediator.attach()
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

    }

}