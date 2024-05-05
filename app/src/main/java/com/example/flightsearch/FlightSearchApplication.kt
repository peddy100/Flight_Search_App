/*
    Pedram Jarahzadeh / jarahzap@oregonstate.edu
    CS 492 / Oregon State University
*/
package com.example.flightsearch

import android.app.Application
import com.example.flightsearch.data.AppContainer
import com.example.flightsearch.data.AppDataContainer
import com.example.flightsearch.data.AppDatabase

class FlightSearchApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this)}
    lateinit var container: AppContainer

    override fun onCreate(){
        super.onCreate()
        container = AppDataContainer(this)
    }
}