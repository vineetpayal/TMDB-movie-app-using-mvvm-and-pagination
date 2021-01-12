package com.orcus.tmdb.data

import android.util.Log
import androidx.paging.PagingSource
import com.orcus.tmdb.api.TmdbApi
import retrofit2.HttpException
import java.io.IOException

private const val MOVIE_API_STARTING_PAGE_INDEX = 1
private const val TAG = "pagingSource"

class MoviePagingSource(
    private val api: TmdbApi,
    private var query: String
) : PagingSource<Int, Movie>() {

    companion object {
        private const val POPULAR = "popular"
        private const val LATEST = "latest"
        private const val TOP_RATED = "top_rated"
        private const val UPCOMING = "upcoming"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: MOVIE_API_STARTING_PAGE_INDEX

        return try {
            when (query) {
                POPULAR -> {
                    val popularMovies = api.getPopularMovies(position).results
                    LoadResult.Page(
                        data = popularMovies,
                        prevKey = if (position == MOVIE_API_STARTING_PAGE_INDEX) null else position - 1,
                        nextKey = if (popularMovies.isEmpty()) null else position + 1
                    )
                }
                LATEST -> {

                    val latestMovies = api.getTvShows(position).results
                    LoadResult.Page(
                        data = latestMovies,
                        prevKey = if (position == MOVIE_API_STARTING_PAGE_INDEX) null else position - 1,
                        nextKey = if (latestMovies.isEmpty()) null else position + 1
                    )

                }
                TOP_RATED -> {
                    val topRatedMovies = api.getTopRatedMovies(position).results
                    LoadResult.Page(
                        data = topRatedMovies,
                        prevKey = if (position == MOVIE_API_STARTING_PAGE_INDEX) null else position - 1,
                        nextKey = if (topRatedMovies.isEmpty()) null else position + 1
                    )
                }
                UPCOMING -> {
                    val upcomingMovies = api.getUpcomingMovies(position).results
                    LoadResult.Page(
                        data = upcomingMovies,
                        prevKey = if (position == MOVIE_API_STARTING_PAGE_INDEX) null else position - 1,
                        nextKey = if (upcomingMovies.isEmpty()) null else position + 1
                    )

                }
                else -> {
                    val searchResults = api.searchMovie(query, position).results
                    Log.d(TAG, "Search results size: ${searchResults.size}")
                    LoadResult.Page(
                        data = searchResults,
                        prevKey = if (position == MOVIE_API_STARTING_PAGE_INDEX) null else position - 1,
                        nextKey = if (searchResults.isEmpty()) null else position + 1
                    )
                }
            }

        } catch (e: IOException) {
            Log.d(TAG, "load: ${e.message}")
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.d(TAG, "load: ${e.message}")
            LoadResult.Error(e)
        }
    }

}