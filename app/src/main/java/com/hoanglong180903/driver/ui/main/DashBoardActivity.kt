package com.hoanglong180903.driver.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.databinding.ActivityDashBoardBinding
import com.hoanglong180903.driver.ui.main.home.HomeFragment

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDashBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setView(savedInstanceState != null)
        setAction()
    }

    private fun setView(isRestore: Boolean){
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        setupActionBarWithNavController(navHostFragment.navController)

        if (isRestore) {
            ((supportFragmentManager.fragments[0] as NavHostFragment).childFragmentManager.fragments[0] as? HomeFragment)?.let {
                binding.mainBottomNav.selectedItemId = R.id.navHome
            }
        }else{
            binding.mainBottomNav.selectedItemId != R.id.navHome
        }
    }

    private fun setAction(){
        binding.mainBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navHome -> {
                    val navController = findNavController(R.id.nav_host_fragment)
                    navController.navigate(R.id.homeFragment)
                }
                R.id.navOrder ->{
                    val navController = findNavController(R.id.nav_host_fragment)
                    navController.navigate(R.id.orderFragment)
                }
                R.id.navBill -> {
                    val navController = findNavController(R.id.nav_host_fragment)
                    navController.navigate(R.id.billFragment)
                }
                R.id.navUser -> {
                }
                else -> {}
            }
            false
        }

    }
}