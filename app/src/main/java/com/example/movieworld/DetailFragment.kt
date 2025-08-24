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

class DetailFragment : Fragment() {
    private val viewModel: MovieListViewModel by activityViewModels()


    companion object {
        private const val ARG_MOVIE = "arg_movie"

        fun newInstance(movie: Movie): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_MOVIE, movie) // Movie is @Parcelize
            fragment.arguments = args
            return fragment
        }
    }

    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = arguments?.getParcelable(ARG_MOVIE)
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

        val item = movie
        if (item != null) {
            poster.setImageResource(item.imageRes)

            title.text = item.title

            val metaText = item.year.toString() + "  •  " + item.duration + " mins  •  " + item.rating
            meta.text = metaText

            genreChip.text = item.genre
            description.text = item.description

            favCheck.isChecked = item.isFavorite

            favCheck.setOnClickListener(object : View.OnClickListener {
                override fun onClick(clickedView: View?) {
                    val checked = favCheck.isChecked
                    item.isFavorite = checked
                    viewModel.updateFavoriteByTitle(item.title, checked)
                }
            })

        }

        // Pop fragment when backBtn pressed.
        backBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(clickedView: View?) {
                parentFragmentManager.popBackStack()
            }
        })
    }
}
