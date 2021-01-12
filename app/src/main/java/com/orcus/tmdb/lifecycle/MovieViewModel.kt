package com.orcus.tmdb.lifecycle

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.orcus.tmdb.data.Movie
import com.orcus.tmdb.data.MovieCast
import com.orcus.tmdb.data.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "movieViewModel"

class MovieViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) :
    ViewModel() {

    private var movies: LiveData<PagingData<Movie>>? = null
    var movie: MutableLiveData<Movie> = MutableLiveData()
    var searchedMovies: LiveData<PagingData<Movie>>? = null
    private var movieCast: MutableLiveData<List<MovieCast>> = MutableLiveData()

    fun getMovies(query: String): LiveData<PagingData<Movie>>? {
        if (movies == null) {
            movies = repository.getMovies(query).cachedIn(viewModelScope)
        }

        return movies
    }

    fun getMovieDetails(id: Long, appendToResponse: String): LiveData<Movie>? {
        viewModelScope.launch {
            movie.value = repository.getMovieDetails(id, appendToResponse)
        }

        return movie
    }

    fun getMovieCast(id: Long): MutableLiveData<List<MovieCast>>? {
        viewModelScope.launch {
            movieCast.value = repository.getMovieCast(id)
        }
        return movieCast
    }


    fun search(query: String): LiveData<PagingData<Movie>>? {
        searchedMovies = repository.searchMovie(query).cachedIn(viewModelScope)

        return searchedMovies

    }


    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: View model cleared ")
    }
}