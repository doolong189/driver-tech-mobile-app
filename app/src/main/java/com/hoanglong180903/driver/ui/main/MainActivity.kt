package com.hoanglong180903.driver.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.base.BaseActivity
import com.hoanglong180903.driver.databinding.ActivityMainBinding
import com.hoanglong180903.driver.ui.main.home.HomeFragment
import com.hoanglong180903.driver.common.permission.LocationPermission
import com.hoanglong180903.driver.utils.PopupUtils
import java.lang.ref.WeakReference


class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate
    private lateinit var locationPermissionHelper: LocationPermission
    private var mGoogleSignInClient : GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //
    }

    override fun initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        setupActionBarWithNavController(navHostFragment.navController)
        ((supportFragmentManager.fragments[0] as NavHostFragment).childFragmentManager.fragments[0] as? HomeFragment)?.let {
            binding.mainBottomNav.selectedItemId = R.id.navHome
        }
        permissionLocation()
    }

    override fun initData() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if(acct != null) {
            val personName = acct.displayName
            val personEmail = acct.email
            val personId = acct.id
            Log.e("zzzz", "$personName - $personEmail - $personId")
        }
    }

    override fun initEvents() {
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
                R.id.navMailBox -> {
                    val navController = findNavController(R.id.nav_host_fragment)
                    navController.navigate(R.id.mailboxFragment)
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

    override fun initObserve() {
    }

    private fun permissionLocation(){
        locationPermissionHelper = LocationPermission(WeakReference(this))
        if (!locationPermissionHelper.hasAccessFinePermission(this)) {
            locationPermissionHelper.requestFineLocationPermission(this)
        } else {
            PopupUtils.showToast(this, "Location Permission Granted")
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
                    PopupUtils.showToast(this, "Location permission is needed to run this application")
                    if (!locationPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                        locationPermissionHelper.launchPermissionSettings(this)
                        finish()
                    }
                }else {
                    PopupUtils.showToast(this, "Woola!!! Location permission is granted")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        signOut()
    }

    private fun signOut(){
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(this
        ) {
            PopupUtils.showToast(this@MainActivity, "Signed Out")
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun isShowHideBottomNav(isVisible : Boolean){
        if (isVisible){
            binding.mainBottomNav.visibility = View.VISIBLE
        }else{
            binding.mainBottomNav.visibility = View.GONE
        }
    }
}