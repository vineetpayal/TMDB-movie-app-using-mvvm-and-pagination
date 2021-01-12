package com.orcus.tmdb.api

import com.orcus.tmdb.BuildConfig
import com.orcus.tmdb.data.Movie
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = BuildConfig.TMDB_API_KEY
        const val ACCESS_TOKEN = BuildConfig.TMDB_ACCESS_TOKEN
    }

    @Headers("Authorization: Bearer $ACCESS_TOKEN")
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page")
        page: Int,
    ): MovieResponse

    @Headers("Authorization: Bearer $ACCESS_TOKEN")
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(

        @Query("page")
        page: Int
    ): MovieResponse

    @Headers("Authorization: Bearer $ACCESS_TOKEN")
    @GET("tv/popular")
    suspend fun getTvShows(

        @Query("page")
        page: Int
    ): MovieResponse


    @Headers("Authorization: Bearer $ACCESS_TOKEN")
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page")
        page: Int
    ): MovieResponse


    @Headers("Authorization: Bearer $ACCESS_TOKEN")
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id")
        movieId: Long,
        @Query("append_to_response")
        appendToResponse: String
    ): Movie

    @Headers("Authorization: Bearer $ACCESS_TOKEN")
    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id")
        movieId: Long,
    ): MovieCastResponse


    @Headers("Authorization: Bearer $ACCESS_TOKEN")
    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query")
        query: String,
        @Query("page")
        page: Int
    ): MovieResponse

}