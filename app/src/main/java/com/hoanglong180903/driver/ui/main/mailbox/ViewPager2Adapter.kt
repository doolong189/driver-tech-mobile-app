package com.hoanglong180903.driver.ui.main.mailbox

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hoanglong180903.driver.ui.main.order.tabLayout.CancelOrderFragment
import com.hoanglong180903.driver.ui.main.order.tabLayout.CompletedOrderFragment
import com.hoanglong180903.driver.ui.main.order.tabLayout.OnGoingOrderFragment

class ViewPager2Adapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NotificationFragment()
            1 -> MessageFragment()
            else -> NotificationFragment()
        }
    }
}