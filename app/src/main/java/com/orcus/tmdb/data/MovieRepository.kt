package com.orcus.tmdb.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.orcus.tmdb.api.TmdbApi
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "repository"

@Singleton
class MovieRepository @Inject constructor(private val api: TmdbApi) {

    fun getMovies(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(api, query)
            }
        ).liveData

    fun searchMovie(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false,

                ),
            pagingSourceFactory = {
                MoviePagingSource(api, query)
            }
        ).liveData

    suspend fun getMovieDetails(id: Long, appendToResponse: String): Movie {
        return api.getMovieDetails(id, appendToResponse)
    }

    suspend fun getMovieCast(id: Long): List<MovieCast> {
        val casts =  api.getMovieCast(id).cast
        Log.d(TAG, "getMovieCast: ${casts[0].name}")
        return casts
    }
}