package com.example.minimalistinfohub.stock

object Stocks {
    val stockList = mapOf(
        "Apple" to "AAPL",
        "Microsoft" to "MSFT",
        "Amazon" to "AMZN",
        "Nvidia" to "NVDA",
        "Tesla" to "TSLA",
        "NIKE" to "NKE"
    )

    fun getStocks() : Map<String, String> {
        return stockList
    }
}