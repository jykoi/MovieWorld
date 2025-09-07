package com.example.movieworld

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.viewModels


class MainActivity : AppCompatActivity(),
    TopMainFragment.OnFilterListener,
    FilterMenuFragment.OnExitListener,
    FilterMenuFragment.OnFilterChangedListener {

    private lateinit var btnMovies: Button
    private lateinit var btnFavourites: Button
    private lateinit var filterbar: View
    private var isMenuVisible = false

    // Shared ViewModel for fragments
    private val movieViewModel: MovieListViewModel by viewModels()

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
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, MovieListFragment())
                .commit()
        }

        btnFavourites.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, FavouritesFragment())
                .addToBackStack(null)
                .commit()
        }

        filterbar = findViewById(R.id.filter_toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Filter button clicked -> show/hide toolbar
    override fun onFilter() {
        if (!isMenuVisible) {
            filterbar.animate()
                .translationX(0f)
                .setDuration(250)
                .start()
        }
        isMenuVisible = !isMenuVisible
    }

    // Exit button clicked -> hide toolbar
    override fun onExit() {
        if (isMenuVisible) {
            filterbar.animate()
                .translationX(-filterbar.width.toFloat())
                .setDuration(250)
                .start()
        }
        isMenuVisible = !isMenuVisible
    }

    // Filter change -> update ViewModel and refresh fragments
    override fun onFilterChanged(selected: Set<String>) {
        movieViewModel.updateSelectedGenres(selected)

        val fragments = supportFragmentManager.fragments
        for (frag in fragments) {
            when (frag) {
                is MovieListFragment -> frag.applyFiltersAndSearch()
                is FavouritesFragment -> frag.applyFiltersAndSearch()
            }
        }
    }
}
