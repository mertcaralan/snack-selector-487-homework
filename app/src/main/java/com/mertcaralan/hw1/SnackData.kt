package com.mertcaralan.hw1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnackData(
    val mood: String,
    val timeOfDay: String,
    val hungerLevel: Int,
    val recommendation: String,
    val emoji: String
) : Parcelable