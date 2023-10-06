package ir.dunijet.dunipool.apiManager

import com.example.dunipool.ApiManager.ApiService
import com.example.dunipool.feature.ALL
import com.example.dunipool.feature.BASE_URL
import com.example.dunipool.feature.HISTO_DAY
import com.example.dunipool.feature.HISTO_HOUR
import com.example.dunipool.feature.HISTO_MINUTE
import com.example.dunipool.feature.HOUR
import com.example.dunipool.feature.HOURS24
import com.example.dunipool.feature.MONTH
import com.example.dunipool.feature.MONTH3
import com.example.dunipool.feature.WEEK
import com.example.dunipool.feature.YEAR
import ir.dunijet.dunipool.apiManager.model.ChartData
import ir.dunijet.dunipool.apiManager.model.CoinsData
import ir.dunijet.dunipool.apiManager.model.NewsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {
    private val apiService: ApiService

    init {

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

    }

    fun getNews(apiCallback: ApiCallback<ArrayList<Pair<String, String>>>) {
        apiService.getTopNews().enqueue(object : Callback<NewsData> {
            override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {

                val data = response.body()!!

                val dataToSend: ArrayList<Pair<String, String>> = arrayListOf()
                data.data.forEach {
                    dataToSend.add(Pair(it.title, it.url))
                }
                apiCallback.onSuccess(dataToSend)
            }

            override fun onFailure(call: Call<NewsData>, t: Throwable) {

                apiCallback.onError(t.message!!)

            }

        })
    }

    fun getCoinsList(apiCallback: ApiCallback<List<CoinsData.Data>>) {

        apiService.getToCoins().enqueue(object : Callback<CoinsData> {
            override fun onResponse(call: Call<CoinsData>, response: Response<CoinsData>) {
                val data = response.body()!!
                apiCallback.onSuccess(data.data)
            }

            override fun onFailure(call: Call<CoinsData>, t: Throwable) {
                apiCallback.onError(t.message!!)
            }

        })

    }

    fun getChartData(symbols: String, period: String, apicallback: ApiCallback<Pair<List<ChartData.Data>, ChartData.Data?>>) {

        var histoperiod = ""
        var limit = 30
        var aggregate = 1

        when(period) {
            HOUR -> {
                histoperiod = HISTO_MINUTE
                limit = 60
                aggregate = 12
            }
            HOURS24 ->{
                histoperiod = HISTO_HOUR
                limit = 24
            }
            MONTH ->{
                histoperiod = HISTO_DAY
                limit = 30
                }
            MONTH3 ->{
                histoperiod = HISTO_DAY
                limit = 90
            }
            WEEK ->{
                histoperiod = HISTO_HOUR
                aggregate = 6
            }
            YEAR ->{
                histoperiod = HISTO_DAY
                aggregate = 13
            }
            ALL ->{
                histoperiod = HISTO_DAY
                limit = 2000
                aggregate = 30
            }
        }

        apiService.getChartData(histoperiod , symbols , limit ,aggregate ).enqueue(object : Callback<ChartData> {
            override fun onResponse(call: Call<ChartData>, response: Response<ChartData>) {
                val dataFull = response.body()!!

                val data1 = dataFull.data
                val data2 = dataFull.data.maxByOrNull { it.close.toFloat() }
                val returingData = Pair(data1 , data2)
                apicallback.onSuccess(returingData)
            }

            override fun onFailure(call: Call<ChartData>, t: Throwable) {
                apicallback.onError(t.message!!)
            }

        })
    }

    interface ApiCallback<T> {

        fun onSuccess(data: T)
        fun onError(errorMessage: String)

    }

}