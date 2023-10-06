package com.example.dunipool.feature.CoinActivity

import com.robinhood.spark.SparkAdapter
import ir.dunijet.dunipool.apiManager.model.ChartData

class ChartAdapter (private val historicaData :List<ChartData.Data> , private val baseLine :String?) :
    SparkAdapter() {

    override fun getCount(): Int {
        return historicaData.size
    }

    override fun getItem(index: Int): ChartData.Data {
        return historicaData[index]
    }

    override fun getY(index: Int): Float {
        return historicaData[index].close.toFloat()
    }

    override fun hasBaseLine(): Boolean {
        return true
    }

    override fun getBaseLine(): Float {
        return baseLine?.toFloat() ?:super.getBaseLine()
    }
}