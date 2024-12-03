package com.example.beatwell.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.beatwell.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun getProfileIcon(context: Context, isLocalUser: Boolean): Drawable {
    val drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_bot)
            ?: throw IllegalStateException("Could not get user profile image")

    if (isLocalUser) {
        DrawableCompat.setTint(drawable.mutate(), Color.BLUE)
    } else {
        DrawableCompat.setTint(drawable.mutate(), Color.RED)
    }

    return drawable
}

fun dateFormater(date: String): String {
    val parsedDate = ZonedDateTime.parse(date)
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
    return parsedDate.format(outputFormatter)
}