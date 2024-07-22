package com.example.minimalistinfohub.stock

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.minimalistinfohub.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StockViewModel : ViewModel() {
    private val _stock = MutableStateFlow<StockData?>(null)
    val stock: StateFlow<StockData?> = _stock

    private val _selectedStockSymbol = MutableStateFlow<String?>(null)
    val symbol: StateFlow<String?> = _selectedStockSymbol

    private val _highestStock = MutableStateFlow<StockData?>(null)
    val highestStock: StateFlow<StockData?> = _highestStock

    private val _stockPrices = MutableStateFlow<List<StockPriceDAO>>(listOf())
    val stockPrices: StateFlow<List<StockPriceDAO>> = _stockPrices

    private val stocks = Stocks.getStocks()

    init {
        fetchStock()
       // getHighestStock()
        getStockPrices()
    }

    fun fetchStock() {
        viewModelScope.launch {
            try {

                Log.d("StockViewModel", "Fetching stock data...")
                val result = RetrofitClient.stockApiService().getStock()
                Log.d("StockViewModel", "Received result: $result")
                _stock.value = result
            } catch (e: Exception) {
                Log.e("StockViewModel", "Unexpected error ${e.message}", e)
            }
        }
    }

    fun fetchSpecificStock(stockName: String) {
        viewModelScope.launch {
            _selectedStockSymbol.value = stockName
            _stock.value = fetchStockBySymbol(stockName)
        }
    }

    private suspend fun fetchStockBySymbol(symbol: String): StockData? {
        var res: StockData? = null
        try {
            Log.d("StockViewModel", "Fetching stock data...")
            val result = RetrofitClient.stockApiService().getSpecificStock(symbol)
            Log.d("StockViewModel", "Received result: $result")
            res = result
        } catch (e: Exception) {
            Log.e("StockViewModel", "Unexpected error ${e.message}", e)
        }

        return res
    }

    private fun getHighestStock() {
        viewModelScope.launch {
            var highestStock: StockData? = null
            var highestValue = 0.0
            for (stock in stocks) {
                val tempStock = fetchStockBySymbol(stock.value)
                val tempValue = tempStock?.metric?.fiftyTwoWeeksHigh?.toDouble() ?: 0.0
                highestValue = if (tempValue > highestValue) tempValue else highestValue
                highestStock = if (tempValue == highestValue) tempStock else highestStock
            }

            _highestStock.value = highestStock
        }
    }

    private fun getStockPrices() {
        val stockPrices : MutableList<StockPriceDAO> = mutableListOf()
        viewModelScope.launch {
            for (stock in stocks) {
                val result = RetrofitClient.stockApiService().getStockPrice(stock.value)
                if(result.currentPrice != null && result.percentChange != null) {
                    stockPrices.add(StockPriceDAO(stock.value, result.currentPrice, result.percentChange))
                } else {
                    Log.e("StockViewModel", "Stock price not fetched")
                }
            }
            _stockPrices.value = stockPrices
        }
    }
}