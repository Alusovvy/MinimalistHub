package com.example.minimalistinfohub.stock

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.minimalistinfohub.utils.BackButton
import com.example.minimalistinfohub.utils.RefreshState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment


@SuppressLint("SuspiciousIndentation")
@Composable
fun StockScreen() {
    val stockViewModel: StockViewModel = viewModel()
    val stockState by stockViewModel.stock.collectAsState(initial = null)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

            if (stockState != null) {
                StockDetails(stockState!!.metric, stockState!!.symbol)
            } else {
                Text(
                    text = "Failed to load stock data",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                item {
                    StockPicker(stockViewModel)
                    RefreshState {
                        if (stockViewModel.symbol.value != null) {
                            stockViewModel.fetchSpecificStock(stockViewModel.symbol.value!!)
                        } else {
                            stockViewModel.fetchStock()
                        }
                    }
                }

                item {
                    StockPriceList(stockViewModel = stockViewModel)
                }

            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockPicker(stockViewModel: StockViewModel) {
    val stockList = Stocks.getStocks()

    var expanded by remember { mutableStateOf(false) }
    var selectedStock by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = stockViewModel.symbol.value ?: "Select a stock",
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                stockList.forEach { (name, symbol) ->
                    DropdownMenuItem(
                        onClick = {
                            selectedStock = "$name ($symbol)"
                            expanded = false
                            stockViewModel.fetchSpecificStock(symbol)
                        }
                    ) {
                        Text(text = "$name ($symbol)")
                    }
                }
            }
        }

        selectedStock?.let {
            Text(text = "Selected Stock: $it", modifier = Modifier.padding(top = 16.dp))
        }
    }
}


@Composable
fun StockDetails(stockDetails: StockMetric, symbol: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Stock Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            StockDetailRow(label = "Stock Symbol:", value = symbol)
            StockDetailRow(label = "52 Weeks High:", value = stockDetails.fiftyTwoWeeksHigh?.toString() ?: "N/A")
            StockDetailRow(label = "52 Weeks High Date:", value = stockDetails.fiftyTwoWeeksHighDate ?: "N/A")
            StockDetailRow(label = "52 Weeks Low:", value = stockDetails.fiftyTwoWeeksLow?.toString() ?: "N/A")
            StockDetailRow(label = "52 Weeks Low Date:", value = stockDetails.fiftyTwoWeeksLowDate ?: "N/A")
            StockDetailRow(label = "10 Day Avg Trading Volume:", value = stockDetails.tenDayAverageTradingVolume?.toString() ?: "N/A")
            StockDetailRow(label = "26 Week Price Return Daily:", value = stockDetails.twentySixWeekPriceReturnDaily?.toString() ?: "N/A")
        }
    }
}

@Composable
fun StockDetailRow(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun StockPriceList(stockViewModel: StockViewModel) {
    val stocks by stockViewModel.stockPrices.collectAsState(initial = emptyList())

    if (stocks.isNotEmpty()) {
        stocks.let { list ->
            for (stock in list) {
                StockPriceSummary(stockViewModel, stockPrice = stock)
            }
        }
    } else {
        Text("Loading stocks")
    }
}

@Composable
fun StockPriceSummary(stockViewModel: StockViewModel, stockPrice: StockPriceDAO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(true,
                onClick =
                {stockViewModel.fetchSpecificStock(stockPrice.symbol)}),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Symbol: ",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stockPrice.symbol,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Current Price: ",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$${stockPrice.currentPrice}",
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Daily Change: ",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${stockPrice.percentChange.slice(IntRange(0, 3))}%",
                    style = MaterialTheme.typography.titleSmall,
                    color = if (stockPrice.percentChange.startsWith("-")) Color.Red else Color.Green
                )
            }
        }
    }
}


@Composable
fun StockHighlight(stockViewModel: StockViewModel) {
    // UI for the title
    Text(
        text = "Highest stock from watched stocks in last 52 weeks",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )

    // Observe the highestStock state
    val highestStock by stockViewModel.highestStock.collectAsState() // If it's a StateFlow
    // If highestStock is LiveData, use:
    // val highestStock by stockViewModel.highestStock.observeAsState()

    // Handle the nullable StockData
    highestStock?.let { stock ->
        StockDetails(stock.metric, symbol = stock.symbol)
    }
}