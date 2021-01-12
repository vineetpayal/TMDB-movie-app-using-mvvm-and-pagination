package com.orcus.tmdb.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.orcus.tmdb.databinding.ItemMovieHeaderFooterBinding

class MovieLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<MovieLoadStateAdapter.LoadStateViewHolder>() {
    inner class LoadStateViewHolder(private val binding: ItemMovieHeaderFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressCircular.isVisible = loadState is LoadState.Loading
                textViewError.isVisible = loadState is LoadState.Error
                btnRetry.isVisible = loadState is LoadState.Error
            }
        }

    }

    override fun onBindViewHolder(
        holder: MovieLoadStateAdapter.LoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): MovieLoadStateAdapter.LoadStateViewHolder {
        val binding = ItemMovieHeaderFooterBinding.inflate(LayoutInflater.from(parent.context))
        return LoadStateViewHolder(binding)
    }
}