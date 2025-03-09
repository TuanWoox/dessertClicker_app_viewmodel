
package com.example.dessertclicker.data

import androidx.annotation.DrawableRes
import com.example.dessertclicker.R

data class DessertUiState(
    val dessert1Sold: Int = 0,
    val dessert2Sold: Int = 0,
    val dessert1Revenue: Int = 0,
    val dessert2Revenue: Int = 0,
    val totalRevenue: Int = 0,
    val totalDessertsSold: Int = 0,
    @DrawableRes val dessert1ImageId: Int = R.drawable.cupcake,
    @DrawableRes val dessert2ImageId: Int = R.drawable.donut
)