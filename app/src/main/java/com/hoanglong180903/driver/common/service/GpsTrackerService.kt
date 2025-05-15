package com.hoanglong180903.driver.common.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.hoanglong180903.driver.ui.map.NavigationMapboxActivity
import com.hoanglong180903.driver.utils.SharedPreferences

typealias Polyline = MutableList<LatLng>
typealias PolylineList = MutableList<Polyline>
class GpsTrackerService  : LifecycleService() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initData()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initData(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        sharedPreferences = SharedPreferences(this)
        isTracking.observe(this,  Observer {
            fetchLocation(it)
        })
    }

    private fun requestLocationUpdates() {
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
                        Log.e("zzzzz","$location")
                    }
                }
            }
        }, Looper.getMainLooper())

    }

    private fun fetchLocation(isTracking: Boolean){
        if (isTracking) {
            val latLng = LatLng(sharedPreferences.getUserLoc()!![1], sharedPreferences.getUserLoc()!![0])
            addPathPoint(latLng)
        }
    }

    private fun addPathPoint(latLng: LatLng) {
        val pos = latLng
        pathPoints.value?.apply {
            last().add(pos)
            pathPoints.postValue(this)
        }
    }

    companion object{
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<PolylineList>()
    }
}