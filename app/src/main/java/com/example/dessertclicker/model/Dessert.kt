package com.example.dessertclicker.data

import androidx.annotation.DrawableRes

/**
 * Data class that represents a dessert
 */
data class Dessert(
    @DrawableRes val imageId: Int,
    val price: Int,
    val startProductionAmount: Int
)