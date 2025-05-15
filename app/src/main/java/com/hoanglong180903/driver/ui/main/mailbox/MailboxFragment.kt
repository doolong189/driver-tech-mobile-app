package com.hoanglong180903.driver.ui.main.mailbox

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.databinding.FragmentMailboxBinding

class MailboxFragment : BaseFragment<FragmentMailboxBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMailboxBinding
        get() = FragmentMailboxBinding::inflate
    override var isShowHideActionBar: Boolean = false
    private lateinit var tabLayoutMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        binding.mailboxViewPager2.isUserInputEnabled = false
        binding.mailboxViewPager2.adapter = ViewPager2Adapter(requireActivity())

        tabLayoutMediator = TabLayoutMediator(binding.mailboxTabLayout, binding.mailboxViewPager2) { tab, position ->
            val tabTextView = TextView(requireContext()).apply {
                text = when (position) {
                    0 -> getString(R.string.notification)
                    1 -> getString(R.string.message)
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

    override fun initData() {
    }

    override fun initEvents() {
    }

    override fun initObserve() {
    }

}