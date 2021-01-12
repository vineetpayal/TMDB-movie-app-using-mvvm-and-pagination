package com.orcus.tmdb

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.orcus.tmdb.CategoryFragments.LatestMoviesFragmentDirections
import com.orcus.tmdb.CategoryFragments.PopularMoviesFragmentDirections
import com.orcus.tmdb.CategoryFragments.TopRatedMoviesFragmentDirections
import com.orcus.tmdb.CategoryFragments.UpcomingMoviesFragmentDirections
import com.orcus.tmdb.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "mainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    private var currentFragmentId: Int? = R.id.popularMoviesFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment).navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.popularMoviesFragment,
                R.id.upcomingMoviesFragment,
                R.id.topRatedMoviesFragment

            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        //For navigating between category fragments

        binding.apply {


            categoryChips.setOnCheckedChangeListener { group, checkedId ->
                currentFragmentId = navController.currentDestination?.id

                when (checkedId) {
                    R.id.popular_movies_chip -> {
                        var action: NavDirections? = null
                        when (currentFragmentId) {
                            R.id.popularMoviesFragment -> {

                            }
                            R.id.topRatedMoviesFragment -> {
                                action =
                                    TopRatedMoviesFragmentDirections.actionTopRatedMoviesFragmentToPopularMoviesFragment()
                            }
                            R.id.latestMoviesFragment -> {
                                action =
                                    LatestMoviesFragmentDirections.actionLatestMoviesFragmentToPopularMoviesFragment()
                            }
                            R.id.upcomingMoviesFragment -> {
                                action =
                                    UpcomingMoviesFragmentDirections.actionUpcomingMoviesFragmentToPopularMoviesFragment()
                            }
                        }
                        if (action != null) {
                            navController.navigate(action)
                        }
                    }

                    R.id.top_rated_movies_chip -> {
                        var action: NavDirections? = null
                        when (currentFragmentId) {
                            R.id.popularMoviesFragment -> {
                                action =
                                    PopularMoviesFragmentDirections.actionPopularMoviesFragmentToTopRatedMoviesFragment()
                            }
                            R.id.topRatedMoviesFragment -> {

                            }
                            R.id.latestMoviesFragment -> {
                                action =
                                    LatestMoviesFragmentDirections.actionLatestMoviesFragmentToTopRatedMoviesFragment()
                            }
                            R.id.upcomingMoviesFragment -> {
                                action =
                                    UpcomingMoviesFragmentDirections.actionUpcomingMoviesFragmentToTopRatedMoviesFragment()
                            }
                        }

                        if (action != null) {
                            navController.navigate(action)
                        }

                    }

                    R.id.upcoming_movies_chip -> {
                        var action: NavDirections? = null
                        when (currentFragmentId) {
                            R.id.popularMoviesFragment -> {
                                action =
                                    PopularMoviesFragmentDirections.actionPopularMoviesFragmentToUpcomingMoviesFragment()
                            }
                            R.id.topRatedMoviesFragment -> {
                                action =
                                    TopRatedMoviesFragmentDirections.actionTopRatedMoviesFragmentToUpcomingMoviesFragment()
                            }
                            R.id.latestMoviesFragment -> {
                                action =
                                    LatestMoviesFragmentDirections.actionLatestMoviesFragmentToUpcomingMoviesFragment()
                            }
                            R.id.upcomingMoviesFragment -> {

                            }
                        }

                        if (action != null) {
                            navController.navigate(action)
                        }

                    }
                }
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}