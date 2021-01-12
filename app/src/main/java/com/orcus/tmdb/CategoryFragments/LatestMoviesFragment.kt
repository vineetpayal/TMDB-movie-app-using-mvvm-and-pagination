package com.orcus.tmdb.CategoryFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.orcus.tmdb.DetailsActivity
import com.orcus.tmdb.R
import com.orcus.tmdb.adapters.MovieLoadStateAdapter
import com.orcus.tmdb.adapters.MoviePagerAdapter
import com.orcus.tmdb.data.Movie
import com.orcus.tmdb.databinding.FragmentLatestMoviesBinding
import com.orcus.tmdb.lifecycle.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LatestMoviesFragment : Fragment(R.layout.fragment_latest_movies) {

    companion object {
        private const val TAG = "latestMoviesFragment"
        private const val POPULAR = "popular"
        private const val LATEST = "latest"
        private const val TOP_RATED = "top_rated"
        private const val UPCOMING = "upcoming"
    }

    private var _binding: FragmentLatestMoviesBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: MoviePagerAdapter
    private val movieViewModel by viewModels<MovieViewModel>()
    lateinit var gridLayoutManager: GridLayoutManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLatestMoviesBinding.bind(view)

        adapter = MoviePagerAdapter(object : MoviePagerAdapter.OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                val intent = Intent(requireActivity(), DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, movie.id)
                requireActivity().startActivityFromFragment(this@LatestMoviesFragment, intent, 3)
            }

        })

        binding.apply {
            recyclerView.setHasFixedSize(true)
            gridLayoutManager = GridLayoutManager(context, 2)
            recyclerView.layoutManager = gridLayoutManager
            //recyclerView.isNestedScrollingEnabled = false
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter { adapter.retry() },
                footer = MovieLoadStateAdapter { adapter.retry() },
            )

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == adapter.itemCount) 2 else 1

                }

            }

        }

        movieViewModel.getMovies(LATEST)?.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
            adapter.notifyDataSetChanged()
            Log.d(TAG, "onViewCreated: Observing latest movies ${adapter.itemCount} ")

        }

        binding.apply {
            adapter.addLoadStateListener { loadStates ->
                progressCircular.isVisible = loadStates.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadStates.source.refresh is LoadState.NotLoading
                textViewError.isVisible = loadStates.source.refresh is LoadState.Error
                btnRetry.isVisible = loadStates.source.refresh is LoadState.Error


                btnRetry.setOnClickListener {
                    adapter.retry()
                }
            }

        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}