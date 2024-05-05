/*
    Pedram Jarahzadeh / jarahzap@oregonstate.edu
    CS 492 / Oregon State University
*/
package com.example.flightsearch.ui.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flightsearch.ui.ui.flights.FlightDetailsDestination
import com.example.flightsearch.ui.ui.flights.FlightDetailsScreen
import com.example.flightsearch.ui.ui.home.HomeDestination
import com.example.flightsearch.ui.ui.home.HomeScreen

@Composable
fun FlightNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToFlightDetails = {airport ->
                    navController.navigate("${FlightDetailsDestination.route}/${airport.iataCode}/${airport.name}")
                }
            )
        }
        composable(
            route = FlightDetailsDestination.routeWithArgs,
            arguments = listOf(
                navArgument(FlightDetailsDestination.iataCodeArg) {
                    type = NavType.StringType
                },
                navArgument(FlightDetailsDestination.nameArg) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val iataCode = backStackEntry.arguments?.getString(FlightDetailsDestination.iataCodeArg) ?: ""
            val name = backStackEntry.arguments?.getString(FlightDetailsDestination.nameArg) ?: ""
            FlightDetailsScreen(
                iataCode = iataCode,
                name = name,
                navigateBack = { navController.navigateUp()}
            )
        }
    }
}