package com.mertcaralan.hw1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnackData(
    val mood: String,          // Ruh hali (Tired, Happy, etc.)
    val timeOfDay: String,     // Gün zamanı (Morning, Afternoon, etc.)
    val hungerLevel: Int,      // Açlık seviyesi (0-100)
    val recommendation: String, // Öneri metni
    val emoji: String          // Görsel emoji
) : Parcelable