package com.example.movieworld

import android.content.res.Configuration
import androidx.fragment.app.activityViewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout

//Fragment that shows full list of movies + search.
class MovieListFragment : Fragment() {

    companion object {
        private const val ARG_SHOW_FAVS = "show_favs"   //Key to pass data into this fragment

        //Method to create a new instance of this fragment
        fun newInstance(showFavouritesOnly: Boolean): MovieListFragment {
            val fragment = MovieListFragment()
            val args = Bundle()
            args.putBoolean(ARG_SHOW_FAVS, showFavouritesOnly) // put flag in bundle
            fragment.arguments = args
            return fragment
        }
    }

    //Get the shared ViewModel (same one used by all fragments)
    private val viewModel: MovieListViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView     //scrollable list
    private lateinit var adapter: MovieAdapter          //adapter that fills in each movie card

    //onCreate when fragment is created, check if favourites should be shown
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.showFavouritesOnly = arguments?.getBoolean(ARG_SHOW_FAVS, false) ?: false   //read the flag
    }

    //Create actual screen
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    //Set screen up.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Find RecyclerView from layout
        recyclerView = view.findViewById(R.id.movieRecyclerView)

        // Check orientation
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape → grid with 3 columns
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        } else {
            // Portrait → vertical list (linear)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        // Set up adapter
        adapter = MovieAdapter(mutableListOf())   // Start with empty list
        recyclerView.adapter = adapter            // Attach adapter -> RecyclerView


        //Set up when user taps on Details/heart for a movie
        adapter.listener = object : MovieAdapter.OnMovieClickListener {
            override fun onDetailsClicked(movie: Movie, position: Int) {



                //Open DetailFragment and pass the selected movie
                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_container, DetailFragment.newInstance(movie))
                    //.replace(R.id.bottom_container, Fragment())
                    .addToBackStack(null)
                    .commit()
            }

            override fun onMovieClicked(movie: Movie, position: Int) {



                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_container, DetailFragment.newInstance(movie))
                    .addToBackStack(null)
                    .commit()

            }

            //When the heart/checkbox is tapped in the list, update shared state BY ID
            override fun onFavouriteToggled(movie: Movie, isFav: Boolean, position: Int) {
                viewModel.updateFavoriteById(movie.id, isFav)
            }


        }

        //Observe changes in movie list (when heart is tapped)
        viewModel.movies.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                // Save raw list, then re-apply search + filters
                applyFiltersAndSearch()
            }
        }

        //SEARCH FEATURE
        val btnSearch = view.findViewById<Button>(R.id.btnSearch)
        val searchField = view.findViewById<TextView>(R.id.searchField)
        searchField.setText(viewModel.currentQuery)     //restores previous text entered when switching fragments


        //not necessary but added for to maintain the integrity of the layout design
        btnSearch.setOnClickListener {
            //gets user input
            //removes whitespaces
            viewModel.currentQuery = searchField.text.toString().trim()

            //updates list according to user input
            applyFiltersAndSearch()
        }

        // Observe text changes
        searchField.addTextChangedListener(object : TextWatcher {
            //called before the text is changed
            //determines the position of the carat and the char sequence
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            //applies the search logic when search bar text is changed
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.currentQuery = s.toString().trim()
                applyFiltersAndSearch()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    //SEARCH + FILTER CODE
    fun applyFiltersAndSearch() {
        var allMovies = viewModel.movies.value ?: return

        // favourites saved list
        if (viewModel.showFavouritesOnly) {
            allMovies = allMovies.filter { it.isFavorite }
        }

        // filter between genre (categories)
        var filtered = if (viewModel.selectedGenres.isEmpty()) {
            allMovies
        } else {
            allMovies.filter { movie ->
                // keep movie if it matches all genres selected
                viewModel.selectedGenres.all { sel -> movie.categories.any { it.equals(sel, ignoreCase = true) } }
            }
        }

        // search query (title or description)
        if (viewModel.currentQuery.isNotEmpty()) {
            filtered = filtered.filter { movie ->
                movie.title.contains(viewModel.currentQuery, ignoreCase = true) ||
                        movie.description.contains(viewModel.currentQuery, ignoreCase = true)
            }
        }

        //update list
        adapter.updateData(filtered.toMutableList())
    }


    fun updateSelectedGenres(genres: Set<String>) {
        viewModel.selectedGenres.clear()
        viewModel.selectedGenres.addAll(genres)
        applyFiltersAndSearch()
    }


}

