
package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.DessertUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {

    private val _dessertUiState = MutableStateFlow(DessertUiState())
    val dessertUiState: StateFlow<DessertUiState> = _dessertUiState.asStateFlow()

    // Set fixed prices for each dessert
    private val dessert1Price = 5  // $5 per dessert1
    private val dessert2Price = 8  // $8 per dessert2

    fun onDessert1Clicked() {
        _dessertUiState.update { currentState ->
            val dessert1Sold = currentState.dessert1Sold + 1
            val dessert1Revenue = dessert1Sold * dessert1Price
            val totalDessertsSold = dessert1Sold + currentState.dessert2Sold
            val totalRevenue = dessert1Revenue + currentState.dessert2Revenue

            currentState.copy(
                dessert1Sold = dessert1Sold,
                dessert1Revenue = dessert1Revenue,
                totalDessertsSold = totalDessertsSold,
                totalRevenue = totalRevenue
            )
        }
    }

    fun onDessert2Clicked() {
        _dessertUiState.update { currentState ->
            val dessert2Sold = currentState.dessert2Sold + 1
            val dessert2Revenue = dessert2Sold * dessert2Price
            val totalDessertsSold = currentState.dessert1Sold + dessert2Sold
            val totalRevenue = currentState.dessert1Revenue + dessert2Revenue

            currentState.copy(
                dessert2Sold = dessert2Sold,
                dessert2Revenue = dessert2Revenue,
                totalDessertsSold = totalDessertsSold,
                totalRevenue = totalRevenue
            )
        }
    }
}