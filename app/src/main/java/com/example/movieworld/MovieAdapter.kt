package com.example.movieworld

import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup


class MovieAdapter (private var items: MutableList<Movie>)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    // Callback to the Fragment when "Details" is clicked
    interface OnMovieClickListener {
        fun onDetailsClicked(movie: Movie, position: Int)
    }
    var listener: OnMovieClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieImage: ImageView = itemView.findViewById(R.id.movieImage)
        val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
        val movieGenre: TextView = itemView.findViewById(R.id.movieGenre)
        val favBtn: CheckBox = itemView.findViewById(R.id.favBtn)
        val detailsBtn: Button = itemView.findViewById(R.id.movieDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]

        holder.movieImage.setImageResource(movie.imageRes)
        holder.movieTitle.text = movie.title
        holder.movieGenre.text = movie.genre

        holder.favBtn.isChecked = movie.isFavorite

        holder.favBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(clickedView: View?) {
                movie.isFavorite = !movie.isFavorite
                holder.favBtn.isChecked = movie.isFavorite
                notifyItemChanged(holder.adapterPosition)
            }
        })

        holder.detailsBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(clickedView: View?) {
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onDetailsClicked(movie, position)
                }
            }
        })
    }

    override fun getItemCount(): Int = items.size

    //  for updating data from ViewModel
    fun updateData(newItems: List<Movie>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}