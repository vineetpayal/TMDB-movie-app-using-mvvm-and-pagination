package com.orcus.tmdb.CategoryFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.orcus.tmdb.DetailsActivity
import com.orcus.tmdb.R
import com.orcus.tmdb.adapters.MovieLoadStateAdapter
import com.orcus.tmdb.adapters.MoviePagerAdapter
import com.orcus.tmdb.data.Movie
import com.orcus.tmdb.databinding.FragmentPopularMoviesBinding
import com.orcus.tmdb.lifecycle.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_popular_movies.*


@AndroidEntryPoint
class PopularMoviesFragment : Fragment(R.layout.fragment_popular_movies) {

    companion object {
        private const val TAG = "popularMovieFragment"
        private const val POPULAR = "popular"
        private const val LATEST = "latest"
        private const val TOP_RATED = "top_rated"
        private const val UPCOMING = "upcoming"
    }

    private var _binding: FragmentPopularMoviesBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel by viewModels<MovieViewModel>()
    lateinit var adapter: MoviePagerAdapter
    lateinit var gridLayoutManager: GridLayoutManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPopularMoviesBinding.bind(view)

        adapter = MoviePagerAdapter(object : MoviePagerAdapter.OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                val intent = Intent(requireActivity(), DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, movie.id)
                requireActivity().startActivityFromFragment(this@PopularMoviesFragment, intent, 2)
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

        movieViewModel.getMovies(POPULAR)?.observe(viewLifecycleOwner) {

            adapter.submitData(viewLifecycleOwner.lifecycle, it)
            adapter.notifyDataSetChanged()

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

        setHasOptionsMenu(true)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)


        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val action = PopularMoviesFragmentDirections.actionPopularMoviesFragmentToSearchFragment(query)
                    findNavController().navigate(action)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {


                return true
            }

        })



        return super.onCreateOptionsMenu(menu, inflater)
    }

}