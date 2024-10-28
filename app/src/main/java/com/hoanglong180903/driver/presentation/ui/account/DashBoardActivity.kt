package com.hoanglong180903.driver.presentation.ui.account

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.databinding.ActivityDashBoardBinding
import com.hoanglong180903.driver.presentation.ui.main.BillFragment
import com.hoanglong180903.driver.presentation.ui.main.HomeFragment
import com.hoanglong180903.driver.presentation.ui.main.OrderFragment
import com.hoanglong180903.driver.presentation.ui.main.UserFragment

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDashBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setUpNavigation()
    }

    private fun setUpNavigation(){
        setSupportActionBar(binding.dashboardToolBar)
        binding.dashboardBottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navHome -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.navOrder -> {
                    replaceFragment(OrderFragment())
                    true
                }
                R.id.navBill -> {
                    replaceFragment(BillFragment())
                    true
                }
                R.id.navUser -> {
                    replaceFragment(UserFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment)
        transaction.commit()
    }
}