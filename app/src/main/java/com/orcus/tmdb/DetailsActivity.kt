package com.orcus.tmdb

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.size
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.chip.Chip
import com.orcus.tmdb.adapters.MovieCastAdapter
import com.orcus.tmdb.data.Movie
import com.orcus.tmdb.data.MovieCast
import com.orcus.tmdb.databinding.ActivityDetailsBinding
import com.orcus.tmdb.databinding.ItemMovieGenreBinding
import com.orcus.tmdb.lifecycle.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "detailsActivity"

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val movieViewModel by viewModels<MovieViewModel>()

    companion object {
        const val EXTRA_MOVIE_ID = "ExtraMovieId"
    }

    lateinit var binding: ActivityDetailsBinding
    lateinit var movie: Movie
    lateinit var movieCastAdapter: MovieCastAdapter
    var cast: ArrayList<MovieCast> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        try {
            fetchMovieDetails()
        } catch (e: Exception) {
        }

    }

    private fun fetchMovieDetails() {
        val movieId: Long = intent.getLongExtra(EXTRA_MOVIE_ID, 343611)

        movieViewModel.getMovieDetails(movieId, "videos")?.observe(this) {

            val movie = it

            binding.apply {


                Glide.with(this@DetailsActivity)
                    .load("https://image.tmdb.org/t/p/w500" + movie.poster_path)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(moviePoster)

                Glide.with(this@DetailsActivity)
                    .load("https://image.tmdb.org/t/p/original" + movie.backdrop_path)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(movieBackdropPoster)

                movieTitle.text = movie.original_title
                movieOverView.text = movie.overview
                if (movie.status.equals("Released")) {
                    movieReleaseDate.text = "Released on ${movie.release_date}"
                }
                val ratings = formatRatings(movie.vote_average)
                movieRatingProgress.progress = ratings
                movieRatingText.text = ratings.toString()

                btnMovieTrailer.setOnClickListener {
                    try {
                        val videos = movie.videos.results
                        for (i in videos.indices) {
                            if (videos[i].type.equals("Trailer")) {
                                val trailerUrl = "https://www.youtube.com/watch?v=${videos[i].key}"
                                Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl)).apply {
                                    setPackage("com.google.android.youtube")
                                    startActivity(this)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@DetailsActivity, e.message, Toast.LENGTH_SHORT).show()
                    }

                }

                val genreList = movie.genres

                if (movie.genres.isNotEmpty()) {
                    //Clears existing genre chips if device is rotated to prevent the duplication of the chips
                    if (chipGroup.size > 0) {
                        chipGroup.removeAllViews()
                    }
                    for (i in genreList.indices) {
                        var binding = ItemMovieGenreBinding.inflate(layoutInflater)
                        val genrechip = binding.root as Chip
                        genrechip.text = genreList[i].name
                        chipGroup.addView(genrechip)
                    }
                }
            }
        }

        movieCastAdapter = MovieCastAdapter(cast, this@DetailsActivity)
        binding.movieCastRecyclerView.adapter = movieCastAdapter
        binding.movieCastRecyclerView.setHasFixedSize(true)
        movieCastAdapter.notifyDataSetChanged()


        //Fetching the movie cast
        movieViewModel.getMovieCast(movieId)?.observe(this) {

            cast.clear()
            cast.addAll(it)

            movieCastAdapter.notifyDataSetChanged()

        }

        binding.progressBar.isVisible = true

    }

    //Formating the ratings to show in a circular progress view
    private fun formatRatings(rating: Double): Int {
        return (rating * 10).toInt()
    }
}