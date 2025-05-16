package com.hoanglong180903.driver.common.service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.hoanglong180903.driver.utils.Constants.ACTION_PAUSE_SERVICE
import com.hoanglong180903.driver.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.hoanglong180903.driver.utils.Constants.ACTION_STOP_SERVICE
import com.hoanglong180903.driver.utils.Constants.TAG
import com.hoanglong180903.driver.utils.Utils

typealias Polyline = MutableList<LatLng>
@SuppressLint("RestrictedApi")
class GpsTrackerService  : LifecycleService() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    isTracking.postValue(true)
                    Log.d(TAG, "Start service...")
                }
                ACTION_PAUSE_SERVICE -> {
                    Log.d(TAG, "Paused service")
                    isTracking.postValue(false)
                }
                ACTION_STOP_SERVICE -> {
                    Log.d(TAG, "Stopped service")
                    isTracking.postValue(false)
                }
                else -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initData(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        isTracking.observe(this,  Observer {
            requestLocationUpdates(it)
        })
    }

    private fun requestLocationUpdates(isTracking: Boolean) {
        val locationRequest = LocationRequest.create()
            .setInterval(10000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0.locations.let { locations ->
                    locations.forEach { location ->
                        Log.e("GPS-SERVICE","$location")
                        val latLng = LatLng(location.longitude, location.latitude)
                        Log.e("GPS-SERVICE1","${location.longitude} \n ${location.latitude} \n ${Utils.convertTimestampToTime(location.time.toString())}")
                        if (isTracking) {
                            addPathPoint(latLng)
                        }
                    }
                }
            }
        }, Looper.getMainLooper())

    }

    private fun addPathPoint(latLng: LatLng) {
        val updatedPathPoints = pathPoints.value ?: mutableListOf()
        if (updatedPathPoints.isEmpty()) {
            updatedPathPoints.add(latLng)
        }
        pathPoints.postValue(updatedPathPoints)
    }

    companion object{
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polyline>()
    }
}