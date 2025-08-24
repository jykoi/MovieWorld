package com.example.movieworld

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val title: String,
    val genre: String,
    val year: Int,
    val rating: Double,
    val duration: Int,
    val description: String,
    val imageRes: Int,
    var isFavorite: Boolean = false
) : Parcelable
