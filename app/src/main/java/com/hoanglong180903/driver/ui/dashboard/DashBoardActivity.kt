package com.hoanglong180903.driver.ui.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.databinding.ActivityDashBoardBinding
import com.hoanglong180903.driver.ui.dashboard.home.HomeFragment
import com.hoanglong180903.driver.utils.LocationPermissionHelper
import java.lang.ref.WeakReference

class DashBoardActivity : AppCompatActivity() {
    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private lateinit var binding : ActivityDashBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setView(savedInstanceState != null)
        setAction()
        permissionLocation()
    }

    private fun setView(isRestore: Boolean){
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        setupActionBarWithNavController(navHostFragment.navController)

        if (isRestore) {
            ((supportFragmentManager.fragments[0] as NavHostFragment).childFragmentManager.fragments[0] as? HomeFragment)?.let {
                binding.mainBottomNav.selectedItemId = R.id.navHome
            }
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
                    val navController = findNavController(R.id.nav_host_fragment)
                    navController.navigate(R.id.userFragment)
                }
                else -> {}
            }
            true
        }

    }

    private fun permissionLocation(){
        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        if (!locationPermissionHelper.hasAccessFinePermission(this)) {
            locationPermissionHelper.requestFineLocationPermission(this)
        } else {
            Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show()
            // PERMISSION is Already granted ((navigate to Next Screen...))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            locationPermissionHelper.BASIC_PERMISSION_REQUESTCODE -> {
                if (!locationPermissionHelper.hasAccessFinePermission(this)) {
                    Toast.makeText(
                        this,
                        "Location permission is needed to run this application",
                        Toast.LENGTH_LONG
                    ).show();

                    if (!locationPermissionHelper.shouldShowRequestPermissionRationale(this)) {  // checking if don't show Again box checked and denied
                        locationPermissionHelper.launchPermissionSettings(this)   // redirect user to Setting screen
                        finish()
                    }
                }else {
                    Toast.makeText(this, "Woola!!! Location permission is granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}