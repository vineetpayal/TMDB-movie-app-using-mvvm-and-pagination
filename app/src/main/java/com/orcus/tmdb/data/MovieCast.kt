package com.orcus.tmdb.data


data class MovieCast(
    val profile_path: String,
    val name: String,
    val character: String,
    val id: Long,
    val credit_id: String
)