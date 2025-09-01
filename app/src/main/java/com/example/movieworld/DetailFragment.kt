package com.example.movieworld

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

//DetailFragment represents the "detail" screen. When user taps "Details" on a movie card, this screen opens.
class DetailFragment : Fragment() {
    private val viewModel: MovieListViewModel by activityViewModels()

    companion object {
        private const val ARG_MOVIE = "arg_movie"       //Key to pass data

        //Method to create new DetaiLFragment with a movie
        fun newInstance(movie: Movie): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_MOVIE, movie)    //save the movie
            fragment.arguments = args
            return fragment
        }
    }

    private var movie: Movie? = null    //holds the movie passed in

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = arguments?.getParcelable(ARG_MOVIE) //Retrieve movie from arguments
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backBtn: ImageButton = view.findViewById(R.id.backBtn)
        val poster: ImageView = view.findViewById(R.id.detailPoster)
        val title: TextView = view.findViewById(R.id.detailTitle)
        val meta: TextView = view.findViewById(R.id.detailMeta)
        val genreChip: TextView = view.findViewById(R.id.detailGenreChip)
        val description: TextView = view.findViewById(R.id.detailDescription)
        val favCheck: CheckBox = view.findViewById(R.id.detailFavCheck)
        val urlText: TextView = view.findViewById(R.id.detailUrl)

        val item = movie
        if (item != null) {
            //Setting image, title and metadata
            poster.setImageResource(item.imageRes)
            title.text = item.title
            val metaText = item.year.toString() + "  •  " + item.duration + " mins  •  " + item.rating
            meta.text = metaText

            //Categories ie. genre list
            genreChip.text = item.categories.joinToString(", ")

            description.text = item.description
            favCheck.isChecked = item.isFavorite

            //When user taps on heart, update local copy, and update ViewModel
            favCheck.setOnClickListener(object : View.OnClickListener {
                override fun onClick(clickedView: View?) {
                    val checked = favCheck.isChecked
                    item.isFavorite = checked
                    viewModel.updateFavoriteById(item.id, checked)
                }
            })

            //Show the URL as text if not empty
            if (item.url.isEmpty()) {
                urlText.visibility = View.GONE
            } else {
                urlText.visibility = View.VISIBLE
                urlText.text = item.url
            }
        }

        // Pop fragment when backBtn pressed.
        backBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(clickedView: View?) {
                parentFragmentManager.popBackStack()
            }
        })
    }
}
