/*
    Pedram Jarahzadeh / jarahzap@oregonstate.edu
    CS 492 / Oregon State University
*/
package com.example.flightsearch.ui.ui.flights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.ui.ui.AppViewModelProvider
import com.example.flightsearch.FlightTopAppBar
import com.example.flightsearch.ui.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object FlightDetailsDestination: NavigationDestination {
    override val route = "flight_details"
    override val titleRes = R.string.flights_from
    const val iataCodeArg = "iataCode"
    const val nameArg = "flightName"
    val routeWithArgs = "$route/{$iataCodeArg}/{$nameArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailsScreen(
    iataCode: String,
    name: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FlightDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold (
        topBar = {
            FlightTopAppBar(
                title = stringResource(FlightDetailsDestination.titleRes) + iataCode,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ){ innerPadding ->
        FlightDetailsBody(
            departureIataCode = iataCode,
            departureName = name,
            flightList = uiState.flightList,
            onFavorite = { departureCode, destinationCode ->
              coroutineScope.launch {
                  viewModel.insertFavorite(departureCode, destinationCode)
              }
            },
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun FlightDetailsBody(
    departureIataCode: String,
    departureName: String,
    flightList: List<Airport>,
    onFavorite: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier){
        items(items = flightList, key = {airport -> airport.id} ) {airport ->
            FlightItem(
                departureIataCode = departureIataCode,
                departureName = departureName,
                flight = airport,
                onFavorite = {onFavorite(departureIataCode, airport.iataCode)},
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Composable
private fun FlightItem(
    departureIataCode: String,
    departureName: String,
    flight: Airport,
    onFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column {
                Text(
                    text = stringResource(R.string.depart),
                    fontWeight = FontWeight.Bold
                )
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = departureIataCode,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = departureName)
                }
                Text(
                    text = stringResource(R.string.arrive),
                    fontWeight = FontWeight.Bold
                )
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = flight.iataCode,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = flight.name)
                }
            }
            IconButton(onClick =  onFavorite ) {
                Icon(Icons.Filled.Star, contentDescription = "Favorite")
            }
        }
    }
}