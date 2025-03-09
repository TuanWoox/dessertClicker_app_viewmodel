// Now let's update the MainActivity to use the new UI
package com.example.dessertclicker

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dessertclicker.data.DessertUiState
import com.example.dessertclicker.ui.theme.DessertClickerTheme
import com.example.dessertclicker.ui.DessertViewModel

// tag for logging
private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate Called")

        setContent {
            DessertClickerTheme {
                DessertClickerApp()
            }
        }
    }

    // Lifecycle methods remain the same
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }
}

/**
 * Share desserts sold information using ACTION_SEND intent
 */
private fun shareSoldDessertsInformation(intentContext: Context, dessertsSold: Int, revenue: Int) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            intentContext.getString(R.string.share_text, dessertsSold, revenue)
        )
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)

    try {
        startActivity(intentContext, shareIntent, null)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            intentContext,
            intentContext.getString(R.string.sharing_not_available),
            Toast.LENGTH_LONG
        ).show()
    }
}

@Composable
private fun DessertClickerApp(
    viewModel: DessertViewModel = viewModel()
) {
    val uiState by viewModel.dessertUiState.collectAsState()
    DessertClickerApp(
        uiState = uiState,
        onDessert1Clicked = viewModel::onDessert1Clicked,
        onDessert2Clicked = viewModel::onDessert2Clicked
    )
}

@Composable
private fun DessertClickerApp(
    uiState: DessertUiState,
    onDessert1Clicked: () -> Unit,
    onDessert2Clicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold (
        topBar = {
            val intentContext = LocalContext.current
            AppBar(
                onShareButtonClicked = {
                    shareSoldDessertsInformation(
                        intentContext = intentContext,
                        dessertsSold = uiState.totalDessertsSold,
                        revenue = uiState.totalRevenue
                    )
                }
            )
        }
    ) { contentPadding ->
        DessertClickerScreen(
            dessert1Revenue = uiState.dessert1Revenue,
            dessert2Revenue = uiState.dessert2Revenue,
            totalRevenue = uiState.totalRevenue,
            dessert1Sold = uiState.dessert1Sold,
            dessert2Sold = uiState.dessert2Sold,
            totalDessertsSold = uiState.totalDessertsSold,
            dessert1ImageId = uiState.dessert1ImageId,
            dessert2ImageId = uiState.dessert2ImageId,
            onDessert1Clicked = onDessert1Clicked,
            onDessert2Clicked = onDessert2Clicked,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
private fun AppBar(
    onShareButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // AppBar remains unchanged
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
        )
        IconButton (
            onClick = onShareButtonClicked,
            modifier = Modifier.padding(end = 16.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.share),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun DessertClickerScreen(
    dessert1Revenue: Int,
    dessert2Revenue: Int,
    totalRevenue: Int,
    dessert1Sold: Int,
    dessert2Sold: Int,
    totalDessertsSold: Int,
    @DrawableRes dessert1ImageId: Int,
    @DrawableRes dessert2ImageId: Int,
    onDessert1Clicked: () -> Unit,
    onDessert2Clicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.bakery_back),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column {
            // Display dessert images in a row
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    // First dessert
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(dessert1ImageId),
                            contentDescription = "Dessert 1",
                            modifier = Modifier
                                .width(120.dp)
                                .height(120.dp)
                                .clickable { onDessert1Clicked() },
                            contentScale = ContentScale.Crop,
                        )
                        Text(
                            text = "$5",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    // Second dessert
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(dessert2ImageId),
                            contentDescription = "Dessert 2",
                            modifier = Modifier
                                .width(120.dp)
                                .height(120.dp)
                                .clickable { onDessert2Clicked() },
                            contentScale = ContentScale.Crop,
                        )
                        Text(
                            text = "$8",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
            TransactionInfo(
                dessert1Revenue = dessert1Revenue,
                dessert2Revenue = dessert2Revenue,
                totalRevenue = totalRevenue,
                dessert1Sold = dessert1Sold,
                dessert2Sold = dessert2Sold,
                totalDessertsSold = totalDessertsSold
            )
        }
    }
}

@Composable
private fun TransactionInfo(
    dessert1Revenue: Int,
    dessert2Revenue: Int,
    totalRevenue: Int,
    dessert1Sold: Int,
    dessert2Sold: Int,
    totalDessertsSold: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White),
    ) {
        // Show individual dessert sales
        DessertsSoldInfo(
            title = "Dessert 1 Sold",
            count = dessert1Sold
        )
        DessertsSoldInfo(
            title = "Dessert 2 Sold",
            count = dessert2Sold
        )
        DessertsSoldInfo(
            title = stringResource(R.string.dessert_sold),
            count = totalDessertsSold
        )

        // Show individual dessert revenues
        RevenueInfo(
            title = "Dessert 1 Revenue",
            revenue = dessert1Revenue
        )
        RevenueInfo(
            title = "Dessert 2 Revenue",
            revenue = dessert2Revenue
        )
        RevenueInfo(
            title = stringResource(R.string.total_revenue),
            revenue = totalRevenue
        )
    }
}

@Composable
private fun RevenueInfo(
    title: String,
    revenue: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "$${revenue}",
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun DessertsSoldInfo(
    title: String,
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
fun MyDessertClickerAppPreview() {
    DessertClickerTheme {
        DessertClickerApp(
            uiState = DessertUiState(),
            onDessert1Clicked = {},
            onDessert2Clicked = {}
        )
    }
}