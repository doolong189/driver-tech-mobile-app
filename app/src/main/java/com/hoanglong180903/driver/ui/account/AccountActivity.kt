package com.hoanglong180903.driver.ui.account

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.hoanglong180903.driver.R

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        setupActionBarWithNavController(navHostFragment.navController)

        if (ContextCompat.checkSelfPermission(
                this@AccountActivity,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@AccountActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
//                showNotification()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
//            showNotification()
        }
    }
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//        showNotification()
    }

    private fun showNotification() {
        val channelId = "12345"
        val description = "Test Notification"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val  builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Hello World")
            .setContentText("Test Notification")
            .setSmallIcon(R.drawable.logo_driver_app)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources, R.drawable.bg
                )
            )
        notificationManager.notify(12345, builder.build())
    }
}