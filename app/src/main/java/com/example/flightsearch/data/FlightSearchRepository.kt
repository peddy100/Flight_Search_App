/*
    Pedram Jarahzadeh / jarahzap@oregonstate.edu
    CS 492 / Oregon State University
*/
package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface FlightSearchRepository {
    suspend fun insertFavorite(favorite: Favorite)

    suspend fun deleteFavorite(favorite: Favorite)

    fun getAllFavoritesStream(): Flow<List<Favorite>>

    fun getFavoriteStream(id: Int): Flow<Favorite?>

    fun getDestinations(name: String): Flow<List<Airport>>

    fun searchAirportsStream(query: String): Flow<List<Airport?>>
}