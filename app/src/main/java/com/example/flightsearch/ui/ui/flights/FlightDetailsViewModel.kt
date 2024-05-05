/*
    Pedram Jarahzadeh / jarahzap@oregonstate.edu
    CS 492 / Oregon State University
*/
package com.example.flightsearch.ui.ui.flights

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FlightSearchRepository

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val flightSearchRepository: FlightSearchRepository,
) : ViewModel() {

    private val flightName: String = checkNotNull(savedStateHandle[FlightDetailsDestination.nameArg])

    val uiState: StateFlow<FlightDetailsUiState> =
        flightSearchRepository.getDestinations(flightName).map { FlightDetailsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FlightDetailsUiState()
            )

    fun insertFavorite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            val favorite = Favorite(departureCode = departureCode, destinationCode = destinationCode)
            flightSearchRepository.insertFavorite(favorite)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class FlightDetailsUiState(
    val flightList: List<Airport> = listOf()
)