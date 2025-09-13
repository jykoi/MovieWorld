package com.example.movieworld

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

//FavouritesFragment shows only movies that user marked as favourites.
class FavouritesFragment : Fragment() {
    private val viewModel: MovieListViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_favourites_list, container, false)
    }

    //Called after view is created. Set up.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.favouritesRecyclerView)

        // Check orientation
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape → grid view with 3 columns
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        } else {
            // Portrait → vertical list (linear)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        adapter = MovieAdapter(mutableListOf()) //Empty list of movies first.
        recyclerView.adapter = adapter          //Attach adapter to RecyclerView

        adapter.listener = object : MovieAdapter.OnMovieClickListener {
            override fun onDetailsClicked(movie: Movie, position: Int) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_container, DetailFragment.newInstance(movie))
                    .addToBackStack(null)
                    .commit()
            }

            override fun onMovieClicked(movie: Movie, position: Int) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_container, DetailFragment.newInstance(movie))
                    .addToBackStack(null)
                    .commit()
            }

            override fun onFavouriteToggled(movie: Movie, isFav: Boolean, position: Int) {
                viewModel.updateFavoriteById(movie.id, isFav)
                applyFiltersAndSearch() // Update favourites list immediately
            }
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            applyFiltersAndSearch()
        }

        viewModel.selectedGenresLive.observe(viewLifecycleOwner) {
            applyFiltersAndSearch()
        }
    }

    fun applyFiltersAndSearch() {
        if (!this::adapter.isInitialized) return

        var allMovies = viewModel.movies.value ?: return
        allMovies = allMovies.filter { it.isFavorite }

        val filtered = if (viewModel.selectedGenres.isEmpty()) {
            allMovies
        } else {
            allMovies.filter { movie ->
                viewModel.selectedGenres.all { sel ->
                    movie.categories.any { it.equals(sel, ignoreCase = true) }
                }
            }
        }

        adapter.updateData(filtered.toMutableList())
    }
}
