/*
    Pedram Jarahzadeh / jarahzap@oregonstate.edu
    CS 492 / Oregon State University
*/
package com.example.flightsearch.ui.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.ui.ui.AppViewModelProvider
import com.example.flightsearch.FlightTopAppBar
import com.example.flightsearch.ui.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToFlightDetails: (Airport) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var text by rememberSaveable { mutableStateOf("")}
    var isActive by rememberSaveable { mutableStateOf(false)}

    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold (
        topBar = {
            FlightTopAppBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
            Column {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .semantics { traversalIndex = -1f },
                    query = text,
                    onQueryChange = { newText ->
                        text = newText
                        viewModel.onSearchQueryChanged(newText)
                    },
                    onSearch = { isActive = false},
                    active = isActive,
                    onActiveChange = {
                        isActive = it
                    },
                    placeholder = { Text("Enter departure Airport")},
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null)},
                    trailingIcon = {
                        IconButton(onClick = {
                            text = ""
                            isActive = false
                            viewModel.onSearchQueryChanged("")
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear" )
                        }
                    }
                ) {
                    val searchResults by viewModel.searchResults.collectAsState()
                    LazyColumn(
                        contentPadding = PaddingValues(all = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchResults) {airport ->
                            ListItem(
                                headlineContent = {
                                    if (airport != null) {
                                        Text(airport.name)
                                    }
                                },
                                supportingContent = {
                                    if (airport != null) {
                                        Text(airport.iataCode)
                                    }
                                },
                                modifier = Modifier
                                    .clickable {
                                        if (airport != null) {
                                            navigateToFlightDetails(airport)
                                        }
                                    }
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
                HomeBody(
                    favoriteList = homeUiState.favoriteList,
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
        }
    }
}




@Composable
private fun HomeBody(
    favoriteList: List<Favorite>, modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        if (favoriteList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_favorite),
                textAlign = TextAlign.Center
            )
        } else {
           FavoriteList(
               favoriteList = favoriteList,
               modifier = Modifier.padding(horizontal = 8.dp)
           )
        }
    }
}

@Composable
private fun FavoriteList(
    favoriteList: List<Favorite>, modifier: Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = favoriteList, key = {it.id}) {favorite ->
            FavoriteItem(favorite = favorite,
                modifier = Modifier
                    .padding(8.dp))
        }
    }
}

@Composable private fun FavoriteItem(
    favorite: Favorite, modifier: Modifier = Modifier
) {
    Card (modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)){
        Column (
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = stringResource(R.string.depart),
                fontWeight = FontWeight.Bold
            )
            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = favorite.departureCode
                )
            }
            Text(
                text = stringResource(R.string.arrive),
                fontWeight = FontWeight.Bold
            )
            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = favorite.destinationCode
                )
            }
        }
    }
}
