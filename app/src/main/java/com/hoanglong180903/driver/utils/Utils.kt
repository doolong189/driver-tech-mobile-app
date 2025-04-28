package com.hoanglong180903.driver.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.hoanglong180903.driver.common.application.DriverApplication
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun hasInternetConnection(application: DriverApplication): Boolean {
        val connectivityManager = application.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun formatPrice(price: Double): String {
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(price)
    }

    fun convertTimestampToDate(timestampStr: String): String {
        val timestamp = timestampStr.toLongOrNull() ?: return "Invalid timestamp"
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateStr = dateFormat.format(date)
        return dateStr
    }

    fun convertTimestampToTime(timestampStr: String): String {
        val timestamp = timestampStr.toLongOrNull() ?: return "Invalid timestamp"
        val date = Date(timestamp)
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeStr = timeFormat.format(date)
        return timeStr
    }


    fun getTimeDifference(timestampStr: String): String {
        val timestamp = timestampStr.toLongOrNull() ?: return "Invalid timestamp"
        val currentTime = System.currentTimeMillis()
        val diffInMillis = currentTime - timestamp
        val diffInSeconds = diffInMillis / 1000
        val diffInMinutes = diffInSeconds / 60
        val diffInHours = diffInMinutes / 60
        val diffInDays = diffInHours / 24
        return when {
            diffInSeconds < 60 -> "Vừa xong"
            diffInMinutes < 60 -> "$diffInMinutes phút trước"
            diffInHours < 24 -> "$diffInHours giờ trước"
            diffInDays < 30 -> "$diffInDays ngày trước"
            else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
        }
    }


//    private fun drawRouteOnMap(lat1: Double, lng1: Double, lat2: Double, lng2: Double) {
//        // Sử dụng Directions API để lấy chỉ dẫn đường đi
//        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                val directions = DirectionsApi.newRequest(getGeoContext())
//                    .origin(com.google.maps.model.LatLng(lat1, lng1))
//                    .destination(com.google.maps.model.LatLng(lat2, lng2))
//                    .mode(com.google.maps.model.TravelMode.DRIVING)
//
//                val result: DirectionsResult = directions.await()
//
//                if (result.routes.isNotEmpty()) {
//                    val route: DirectionsRoute = result.routes[0]
//                    val polylineOptions = PolylineOptions()
//                        .addAll(route.overviewPolyline.decodePath())  // Giải mã polyline đường đi
//                        .width(10f)
//                        .color(Color.BLUE)
//
//                    runOnUiThread {
//                        mMap.addPolyline(polylineOptions)
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
}