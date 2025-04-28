package com.hoanglong180903.driver.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.hoanglong180903.driver.MainActivity
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.databinding.ActivityDashBoardBinding
import com.hoanglong180903.driver.ui.dashboard.home.HomeFragment
import com.hoanglong180903.driver.utils.permission.LocationPermission
import java.lang.ref.WeakReference

class DashBoardActivity : AppCompatActivity() {
    private lateinit var locationPermissionHelper: LocationPermission
    private lateinit var binding : ActivityDashBoardBinding
    private var mGoogleSignInClient : GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setView(savedInstanceState != null)
        setAction()
        permissionLocation()


        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
                R.id.navNotification -> {
                    val navController = findNavController(R.id.nav_host_fragment)
                    navController.navigate(R.id.notificationFragment)
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
        locationPermissionHelper = LocationPermission(WeakReference(this))
        if (!locationPermissionHelper.hasAccessFinePermission(this)) {
            locationPermissionHelper.requestFineLocationPermission(this)
        } else {
            Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show()
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

                    if (!locationPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                        locationPermissionHelper.launchPermissionSettings(this)
                        finish()
                    }
                }else {
                    Toast.makeText(this, "Woola!!! Location permission is granted", Toast.LENGTH_SHORT).show()
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
        ) { Toast.makeText(this@DashBoardActivity, "Signed Out", Toast.LENGTH_LONG).show() }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}