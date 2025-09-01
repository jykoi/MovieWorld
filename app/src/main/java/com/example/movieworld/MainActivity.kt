package com.example.movieworld

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

//MainActivity hosts all fragments and handles navigation between them,
class MainActivity : AppCompatActivity(), TopMainFragment.OnFilterListener, FilterMenuFragment.OnExitListener  {
    private lateinit var btnMovies: Button
    private lateinit var btnFavourites: Button
    private lateinit var filterbar: View
    private var isMenuVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Only add initial fragments if this is the first time launching app,
        //to avoid readding after screen rotation/ config change.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.top_container, TopMainFragment())
                .commit()

            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, MovieListFragment())
                .commit()

            supportFragmentManager.beginTransaction()
                .replace(R.id.filter_toolbar, FilterMenuFragment())
                .commit()
        }

        //Bottom navigation bar:
        btnMovies = findViewById(R.id.btn_movies)
        btnFavourites = findViewById(R.id.btn_favourites)

        btnMovies.setOnClickListener {
            //Show the full movie list (MovieListFragment)
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, MovieListFragment())
                .commit()
        }

        btnFavourites.setOnClickListener {
            //Show FavouritesFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, FavouritesFragment())
                .addToBackStack(null)
                .commit()
        }

        //FILTER TOOL BAR
        filterbar = findViewById(R.id.filter_toolbar)

    }

    //when filter button is clicked tool bar appears
    override fun onFilter() {
        if (!isMenuVisible) {
            filterbar.animate()
                .translationX(0f)
                .setDuration(250)
                .start()
        }
        isMenuVisible = !isMenuVisible
    }

    //when exit button is clicked tool bar disappears
    override fun onExit() {
        if (isMenuVisible) {
            // Hide toolbar
            filterbar.animate()
                .translationX(-filterbar.width.toFloat())
                .setDuration(250)
                .start()
        }
        isMenuVisible = !isMenuVisible
    }

}