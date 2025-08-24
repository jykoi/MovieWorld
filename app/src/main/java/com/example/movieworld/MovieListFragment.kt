package com.example.movieworld

// import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }
}
