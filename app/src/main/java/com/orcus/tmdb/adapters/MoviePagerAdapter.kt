package com.orcus.tmdb.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.orcus.tmdb.data.Movie
import com.orcus.tmdb.databinding.ItemMovieBinding

class MoviePagerAdapter(var clickListener: OnItemClickListener) :
    PagingDataAdapter<Movie, MoviePagerAdapter.ViewHolder>(MOVIE_COMPARATOR) {

    lateinit var binding: ItemMovieBinding

    inner class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    val movie = getItem(absoluteAdapterPosition)!!
                    clickListener.onItemClick(movie)
                }
            }
        }

        fun bind(currentMovie: Movie) {
            binding.apply {
                val posterUrl = "https://image.tmdb.org/t/p/w500" + currentMovie.poster_path
                Glide.with(itemView).load(posterUrl)
                    .transition(DrawableTransitionOptions.withCrossFade()).into(moviePoster)
                movieTitle.text = currentMovie.original_title
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviePagerAdapter.ViewHolder {
        binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviePagerAdapter.ViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }


    interface OnItemClickListener {
        fun onItemClick(movie: Movie)
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem == newItem

        }
    }

}