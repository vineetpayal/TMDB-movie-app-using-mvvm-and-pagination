package com.orcus.tmdb.api

import com.orcus.tmdb.data.Movie

data class MovieResponse(
    val results: List<Movie>
)
