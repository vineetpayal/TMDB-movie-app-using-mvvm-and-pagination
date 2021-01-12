package com.orcus.tmdb.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.orcus.tmdb.data.MovieCast
import com.orcus.tmdb.databinding.ItemMovieCastBinding

class MovieCastAdapter(var cast: List<MovieCast>, var context: Context) :
    RecyclerView.Adapter<MovieCastAdapter.CastViewHolder>() {
    inner class CastViewHolder(var binding: ItemMovieCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                Glide.with(binding.root)
                    .load("https://image.tmdb.org/t/p/original" + cast[absoluteAdapterPosition].profile_path)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(castImageView)

                castName.text = cast[absoluteAdapterPosition].name
                castCharacterName.text = cast[absoluteAdapterPosition].character
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieCastAdapter.CastViewHolder {
        val binding =
            ItemMovieCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieCastAdapter.CastViewHolder, position: Int) {
        if (position != -1) {
            holder.bind(position)
        }
    }

    override fun getItemCount(): Int {
        return cast.size
    }
}