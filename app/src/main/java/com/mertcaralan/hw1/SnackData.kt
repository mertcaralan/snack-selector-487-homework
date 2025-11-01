package com.mertcaralan.hw1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnackData(
    val category: String,
    val intensity: Int
) : Parcelable