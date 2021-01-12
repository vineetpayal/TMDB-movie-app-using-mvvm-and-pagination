package com.orcus.tmdb.data

data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val genres: List<GenreList>,
    val id: Long,
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double,
    val status: String,
    val videos: VideoResult
) {
    data class GenreList(
        val id: Int,
        val name: String
    )

    data class VideoResult(
        val results: List<Result>
    ) {
        data class Result(
            val id: String,
            val key: String,
            val site: String,
            val type: String
        )
    }
}