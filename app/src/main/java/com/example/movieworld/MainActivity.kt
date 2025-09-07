package com.example.movieworld

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), TopMainFragment.OnFilterListener, FilterMenuFragment.OnExitListener, FilterMenuFragment.OnFilterChangedListener  {
    private lateinit var btnMovies: Button
    private lateinit var btnFavourites: Button
    private lateinit var filterbar: View
    private var isMenuVisible = false

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

            supportFragmentManager.beginTransaction()
                .replace(R.id.filter_toolbar, FilterMenuFragment())
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

        //FILTER TOOL BAR
        filterbar = findViewById(R.id.filter_toolbar)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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

    //filter change
    //when a genre is selected it updates the list
    override fun onFilterChanged(selected: Set<String>) {
        val frag = supportFragmentManager.findFragmentById(R.id.content_container)
        if (frag is MovieListFragment) {
            frag.updateSelectedGenres(selected)
        }
    }



}