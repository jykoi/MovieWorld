package com.example.movieworld

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var btnMovies: Button
    private lateinit var btnFavourites: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.top_container, TopMainFragment())
                .commit()

            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, MovieListFragment())
                .commit()
        }

        btnMovies = findViewById(R.id.btn_movies)
        btnFavourites = findViewById(R.id.btn_favourites)

        btnMovies.setOnClickListener {
            // Show the full movie list
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, MovieListFragment())
                .commit()
        }

        btnFavourites.setOnClickListener {
            // push Favourites so Back pops back to Movies
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, FavouritesFragment())
                .addToBackStack(null)
                .commit()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}