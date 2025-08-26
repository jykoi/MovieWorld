package com.example.movieworld

// import androidx.fragment.app.viewModels
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MovieListFragment : Fragment() {

    companion object {
        private const val ARG_SHOW_FAVS = "show_favs"

        fun newInstance(showFavouritesOnly: Boolean): MovieListFragment {
            val fragment = MovieListFragment()
            val args = Bundle()
            args.putBoolean(ARG_SHOW_FAVS, showFavouritesOnly) // put flag in bundle
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: MovieListViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter

    private var showFavouritesOnly: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFavouritesOnly = arguments?.getBoolean(ARG_SHOW_FAVS, false) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Find RV from XML; Sets LayoutManager; Creates adapter w empty list for now; Attach adapter;
        // Observe ViewModel and submits data to adapter.
        recyclerView = view.findViewById(R.id.movieRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = MovieAdapter(mutableListOf())
        recyclerView.adapter = adapter

        adapter.listener = object : MovieAdapter.OnMovieClickListener {
            override fun onDetailsClicked(movie: Movie, position: Int) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_container, DetailFragment.newInstance(movie))
                    .addToBackStack(null)
                    .commit()
            }
        }


        viewModel.movies.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                val toShow = if (showFavouritesOnly) {
                    val onlyFavs = ArrayList<Movie>()
                    for (movie in list) {
                        if (movie.isFavorite) {
                            onlyFavs.add(movie)
                        }
                    }
                    onlyFavs
                } else {
                    list
                }
                adapter.updateData(toShow)
            }
        }

        //SEARCH FEATURE
        val btnSearch = view.findViewById<Button>(R.id.btnSearch)
        val searchField = view.findViewById<TextView>(R.id.searchField)

        //not necessary but added for to maintain the integrity of the layout design
        btnSearch.setOnClickListener {
            //gets user input
            //removes whitespaces
            val query = searchField.text.toString().trim()

            //get full list of movies
            //if list is null/empty it exits function
            val movieList = viewModel.movies.value ?: return@setOnClickListener

            //filtered implies the list of movies found with search query
            val filtered = if (query.isEmpty()) {
                //show entire list if search bar is empty
                movieList
            } else {
                //filter logic
                movieList.filter { movie ->
                    movie.title.contains(query, ignoreCase = true)
                }
            }

            //update movie list to recycler view
            adapter.updateData(filtered.toMutableList())
        }

        // Observe text changes
        searchField.addTextChangedListener(object : TextWatcher {
            //called before the text is changed
            //determines the position of the carat and the char sequence
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            //called after the text is changed
            //also determines the position of the carat and the char sequence
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()

                // get full list of movies
                val movieList = viewModel.movies.value ?: return

                // filter logic (title OR description)
                val filtered = if (query.isEmpty()) {
                    movieList
                } else {
                    movieList.filter { movie ->
                        movie.title.contains(query, ignoreCase = true) ||
                                movie.description.contains(query, ignoreCase = true)
                    }
                }

                // update movie list in recycler view
                adapter.updateData(filtered.toMutableList())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }
}
