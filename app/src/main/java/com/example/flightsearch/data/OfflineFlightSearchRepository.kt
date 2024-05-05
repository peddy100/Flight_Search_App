/*
    Pedram Jarahzadeh / jarahzap@oregonstate.edu
    CS 492 / Oregon State University
*/
package com.example.flightsearch.data

import androidx.room.Query
import kotlinx.coroutines.flow.Flow

class OfflineFlightSearchRepository(private val flightSearchDao: FlightSearchDao) : FlightSearchRepository{
    override fun getAllFavoritesStream(): Flow<List<Favorite>> = flightSearchDao.getAllFavorites()

    override fun getFavoriteStream(id: Int): Flow<Favorite?> = flightSearchDao.getFavorite(id)

    override fun getDestinations(name: String): Flow<List<Airport>> = flightSearchDao.getDestinations(name)

    override suspend fun insertFavorite(favorite: Favorite) = flightSearchDao.insert(favorite)

    override suspend fun deleteFavorite(favorite: Favorite) = flightSearchDao.delete(favorite)

    override fun searchAirportsStream(query: String): Flow<List<Airport?>> = flightSearchDao.searchAirports(query)
}