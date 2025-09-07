package com.example.movieworld

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = MovieAdapter(mutableListOf()) //Empty list of movies first.
        recyclerView.adapter = adapter          //Attach adapter to RecyclerView

        //When user taps on Details, open DetailFragment and pass selected movie
        adapter.listener = object : MovieAdapter.OnMovieClickListener {
            override fun onDetailsClicked(movie: Movie, position: Int) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_container, DetailFragment.newInstance(movie))
                    .addToBackStack(null)
                    .commit()
            }

            //When user taps heart, tell ViewModel
            //and if user taps heart again (Unfavourite), it becomes false and is removed from list.
            override fun onFavouriteToggled(movie: Movie, isFav: Boolean, position: Int) {
                viewModel.updateFavoriteById(movie.id, isFav)
            }
        }

        //Observe changes in movie list (from shared ViewModel)
        viewModel.movies.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                val onlyFavourites = ArrayList<Movie>()     //Create new list to hold only favourite movies

                //Loop through list and if movie.isFavourite, add to favourtes list.
                for (movie in list) {
                    if (movie.isFavorite) {
                        onlyFavourites.add(movie)
                    }
                }
                adapter.updateData(onlyFavourites)      //Send filtered list to adapter, so user can see onlyFavourites
            }
        }
    }
}
