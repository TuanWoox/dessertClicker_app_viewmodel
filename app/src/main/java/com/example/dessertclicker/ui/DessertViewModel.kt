// In DessertViewModel.kt
package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.R
import com.example.dessertclicker.data.Dessert
import com.example.dessertclicker.data.DessertUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {

    private val _dessertUiState = MutableStateFlow(DessertUiState())
    val dessertUiState: StateFlow<DessertUiState> = _dessertUiState.asStateFlow()

    // List of all desserts, in order of when they start being produced
    private val dessert1List = listOf(
        Dessert(R.drawable.cupcake, 5, 0),
        Dessert(R.drawable.donut, 10, 5),
        Dessert(R.drawable.eclair, 15, 10),
        Dessert(R.drawable.froyo, 30, 20),
        Dessert(R.drawable.gingerbread, 50, 30)
    )

    private val dessert2List = listOf(
        Dessert(R.drawable.cupcake, 5, 0),
        Dessert(R.drawable.donut, 10, 5),
        Dessert(R.drawable.icecreamsandwich, 15, 10),
        Dessert(R.drawable.jellybean, 30, 20),
        Dessert(R.drawable.kitkat, 50, 30)
    )

    private fun determineDessert1Index(dessertsSold: Int): Int {
        var dessertIndex = 0
        for (index in dessert1List.indices) {
            if (dessertsSold >= dessert1List[index].startProductionAmount) {
                dessertIndex = index
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more
                // desserts, you'll start producing more expensive desserts as determined by
                // startProductionAmount. We know to break as soon as we see a dessert who's
                // "startProductionAmount" is greater than the amount sold.
                break
            }
        }
        return dessertIndex
    }

    private fun determineDessert2Index(dessertsSold: Int): Int {
        var dessertIndex = 0
        for (index in dessert2List.indices) {
            if (dessertsSold >= dessert2List[index].startProductionAmount) {
                dessertIndex = index
            } else {
                break
            }
        }
        return dessertIndex
    }

    private fun updateDessert1Image(dessertsSold: Int) {
        val dessertIndex = determineDessert1Index(dessertsSold)
        val currentDessert = dessert1List[dessertIndex]

        _dessertUiState.update { currentState ->
            currentState.copy(
                dessert1ImageId = currentDessert.imageId
            )
        }
    }

    private fun updateDessert2Image(dessertsSold: Int) {
        val dessertIndex = determineDessert2Index(dessertsSold)
        val currentDessert = dessert2List[dessertIndex]

        _dessertUiState.update { currentState ->
            currentState.copy(
                dessert2ImageId = currentDessert.imageId
            )
        }
    }

    fun onDessert1Clicked() {
        _dessertUiState.update { currentState ->
            val dessert1Sold = currentState.dessert1Sold + 1
            val dessertIndex = determineDessert1Index(dessert1Sold)
            val currentPrice = dessert1List[dessertIndex].price
            val dessert1Revenue = dessert1Sold * currentPrice
            val totalDessertsSold = dessert1Sold + currentState.dessert2Sold
            val totalRevenue = dessert1Revenue + currentState.dessert2Revenue

            currentState.copy(
                dessert1Sold = dessert1Sold,
                dessert1Revenue = dessert1Revenue,
                totalDessertsSold = totalDessertsSold,
                totalRevenue = totalRevenue,
                dessert1ImageId = dessert1List[dessertIndex].imageId,
                dessert1Price = currentPrice  // Add this line
            )
        }
    }

    fun onDessert2Clicked() {
        _dessertUiState.update { currentState ->
            val dessert2Sold = currentState.dessert2Sold + 1
            val dessertIndex = determineDessert2Index(dessert2Sold)
            val currentPrice = dessert2List[dessertIndex].price
            val dessert2Revenue = dessert2Sold * currentPrice
            val totalDessertsSold = currentState.dessert1Sold + dessert2Sold
            val totalRevenue = currentState.dessert1Revenue + dessert2Revenue

            currentState.copy(
                dessert2Sold = dessert2Sold,
                dessert2Revenue = dessert2Revenue,
                totalDessertsSold = totalDessertsSold,
                totalRevenue = totalRevenue,
                dessert2ImageId = dessert2List[dessertIndex].imageId,
                dessert2Price = currentPrice  // Add this line
            )
        }
    }
}