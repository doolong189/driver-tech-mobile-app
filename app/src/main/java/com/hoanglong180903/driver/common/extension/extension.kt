package com.hoanglong180903.driver.common.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getTimeDifference(timestampStr: String): String {
    val timestamp = timestampStr.toLongOrNull() ?: return "Ngày/giờ không đúng"
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

fun formatPrice(price: Double): String {
    val decimalFormat = DecimalFormat("#,###,###")
    return decimalFormat.format(price)
}

fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
    convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
    if (sourceDrawable == null) {
        return null
    }
    return if (sourceDrawable is BitmapDrawable) {
        sourceDrawable.bitmap
    } else {
        val constantState = sourceDrawable.constantState ?: return null
        val drawable = constantState.newDrawable().mutate()
        val bitmap: Bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    }
}