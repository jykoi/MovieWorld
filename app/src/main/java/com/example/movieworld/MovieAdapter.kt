package com.example.movieworld

import android.view.View
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.text.TextUtils
import androidx.core.content.ContextCompat

//MovieAdapter builds each movie card in the list.
class MovieAdapter (private var items: MutableList<Movie>)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    //Interface that lets Fragment (details + favourites) know when user taps "Details"/heart
    interface OnMovieClickListener {
        fun onDetailsClicked(movie: Movie, position: Int)
        fun onMovieClicked(movie: Movie, position: Int)

        fun onFavouriteToggled(movie: Movie, isFav: Boolean, position: Int)
    }
    var listener: OnMovieClickListener? = null  //Holds whoever is listening, null when it starts

    //ViewHolder remembers where all parts of a movie card are
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieImage: ImageView = itemView.findViewById(R.id.movieImage)
        val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
        val categoryContainer: LinearLayout = itemView.findViewById(R.id.categoryContainer)
        val favCheckBox: CheckBox = itemView.findViewById(R.id.favCheckBox)
        val detailsBtn: ImageButton = itemView.findViewById(R.id.movieDetails)
        val cardView: CardView = itemView.findViewById(R.id.movieCard)

    }

    //Called when RecyclerView needs a new card (when scrolling)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return ViewHolder(itemView)
    }

    //Called to fill a card with data (runs for each movie in the list)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]

        holder.movieImage.setImageResource(movie.imageRes)
        holder.movieTitle.text = movie.title

        renderCategories(holder.categoryContainer, movie.categories)    //Genre chips

        //Prevent infinite loop when setting checkbox (tap heart)
        holder.favCheckBox.setOnCheckedChangeListener(null)
        holder.favCheckBox.isChecked = movie.isFavorite

        //When user taps heart, tell the fragment
        holder.favCheckBox.setOnClickListener(object : View.OnClickListener {
            override fun onClick(clickedView: View?) {
                val newState = !movie.isFavorite
                val currentPosition = holder.bindingAdapterPosition
                if (currentPosition != RecyclerView.NO_POSITION && listener != null) {
                    listener!!.onFavouriteToggled(movie, newState, currentPosition)
                }
            }
        })

        //When user taps the movie card, open the detail screen
        holder.cardView.setOnClickListener {
            if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION && listener != null) {
                listener!!.onMovieClicked(movie, holder.bindingAdapterPosition)
            }
        }

        //When user taps the details icon, open the detail screen
        holder.detailsBtn.setOnClickListener {
            if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION && listener != null) {
                listener!!.onDetailsClicked(movie, holder.bindingAdapterPosition)
            }
        }


    }


    override fun getItemCount(): Int = items.size

    //For updating the list (when search or filter changes)
    fun updateData(newItems: List<Movie>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    //Clears the container and adds one little “chip” TextView per category.
    private fun renderCategories(container: LinearLayout, categories: List<String>) {
        container.removeAllViews()          // Remove any old chip views first

        //For each category, create a small TextView styled like a chip
        for (category in categories) {
            val chip = TextView(container.context)
            chip.text = category
            chip.textSize = 12f
            chip.setTextColor(android.graphics.Color.BLACK)
            chip.background = ContextCompat.getDrawable(container.context, R.drawable.chip)

            //keep chip text on one line so it doesn't become vertical
            chip.isSingleLine = true
            chip.ellipsize = TextUtils.TruncateAt.END   //Adds ... if too long
            chip.maxLines = 1

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 8, 8)  //margin spacing right + bottom
            chip.layoutParams = params

            container.addView(chip)
        }
    }
}