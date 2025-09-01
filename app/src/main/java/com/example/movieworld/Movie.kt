package com.example.movieworld

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//Simple data class for one catalog item (a movie).
//@Parcelize lets us send this movie from one screen to another (Ie from list to detail).
@Parcelize
data class Movie(
    val id: Int,          //Unique number for each item/movie so we can update Favourites safely
    val title: String,
    val categories: List<String>,   //List of tags used for filtering through genres
    val year: Int,
    val rating: Double,
    val duration: Int,
    val description: String,
    val imageRes: Int,
    val url: String,  //external link (opens in a browser from detail screen)
    var isFavorite: Boolean = false
) : Parcelable
