/*
    Pedram Jarahzadeh / jarahzap@oregonstate.edu
    CS 492 / Oregon State University
*/
package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightSearchDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)

    @Query(
        """
        SELECT * FROM favorite
        """
    )
    fun getAllFavorites(): Flow<List<Favorite>>

    @Query(
        """
        SELECT * FROM favorite WHERE id = :id
        """
    )
    fun getFavorite(id: Int): Flow<Favorite>

    @Query(
        """
        SELECT * FROM airport
        WHERE name != :name
        ORDER BY passengers DESC
        """
    )
    fun getDestinations(name: String): Flow<List<Airport>>

    @Query(
        """
        SELECT * FROM airport
        WHERE name LIKE :query or iata_code LIKE :query
        ORDER BY passengers DESC    
        """
    )
    fun searchAirports(query: String): Flow<List<Airport>>
}