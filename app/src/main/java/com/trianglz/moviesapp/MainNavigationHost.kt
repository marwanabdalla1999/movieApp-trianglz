package com.trianglz.moviesapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.trianglz.movies.moviesDetailsScreen.movieDetailsRoute
import com.trianglz.movies.moviesListingScreen.moviesListingRoute
import com.trianglz.ui.navigations.MovieDetails
import com.trianglz.ui.navigations.MoviesListing


@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController, startDestination = MoviesListing, modifier = modifier
    ) {

        moviesListingRoute(navigateToMovieDetails = {
            navController.navigate(MovieDetails(it))
        })

        movieDetailsRoute{
            navController.popBackStack()
        }

    }
}



