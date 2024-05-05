/*
    Pedram Jarahzadeh / jarahzap@oregonstate.edu
    CS 492 / Oregon State University
*/
package com.example.flightsearch.ui.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FlightSearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(flightSearchRepository: FlightSearchRepository) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        flightSearchRepository.getAllFavoritesStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    private val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults = searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            flowOf(emptyList<Airport>())
        } else {
            flightSearchRepository.searchAirportsStream(query)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(query: String){
        searchQuery.value = "%$query%"
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(val favoriteList: List<Favorite> = listOf())