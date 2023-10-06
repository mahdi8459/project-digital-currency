package com.example.dunipool.feature.MarketActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dunipool.ApiManager.model.AboutData
import com.example.dunipool.ApiManager.model.CoinAboutItem
import com.example.dunipool.databinding.ActivityMarketBinding
import com.example.dunipool.feature.CoinActivity.Activity_Coin
import com.google.gson.Gson
import ir.dunijet.dunipool.apiManager.ApiManager
import ir.dunijet.dunipool.apiManager.model.CoinsData

class Activity_Market : AppCompatActivity(), MarketAdapter.RecyclerCallBack {

    lateinit var binding: ActivityMarketBinding
    val apimanager = ApiManager()
    lateinit var dataNews: ArrayList<Pair<String, String>>
    lateinit var AboutDataMap: MutableMap<String, CoinAboutItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMarketBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.layoutListMarket.btnShowMore.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.livecoinwatch.com/"))
            startActivity(intent)
        }

        binding.swipeRefreshMain.setOnRefreshListener {

            initUi()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshMain.isRefreshing = false
            }, 1000)
        }

        getAboutDataFromAssets()
        initUi()

    }

    override fun onResume() {
        super.onResume()
        initUi()

    }

    private fun initUi() {
        getNewsFromApi()
        getTopCoinsFromApi()
    }

    private fun getTopCoinsFromApi() {

        apimanager.getCoinsList(object : ApiManager.ApiCallback<List<CoinsData.Data>> {
            override fun onSuccess(data: List<CoinsData.Data>) {
                showDataInRecycler(data)
            }

            override fun onError(errorMessage: String) {

                Toast.makeText(this@Activity_Market, "error$errorMessage", Toast.LENGTH_LONG).show()
                Log.e("test2" , errorMessage)
            }
        })
    }

    private fun showDataInRecycler(data: List<CoinsData.Data>) {

        val marketadapter = MarketAdapter(ArrayList(data), this)
        binding.layoutListMarket.recyclerViewMain.adapter = marketadapter
        binding.layoutListMarket.recyclerViewMain.layoutManager = LinearLayoutManager(this)
    }

    private fun getNewsFromApi() {

        apimanager.getNews(object : ApiManager.ApiCallback<ArrayList<Pair<String, String>>> {
            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                dataNews = data
                refreshNews()
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@Activity_Market, "error $errorMessage", Toast.LENGTH_SHORT)
                    .show()
            }

        })

    }

    private fun refreshNews() {
        val randomAccess = (0..49).random()
        binding.layoutNewsMarket.txtNews.text = dataNews[randomAccess].first
        binding.layoutNewsMarket.imgNews.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataNews[randomAccess].second))
            startActivity(intent)
        }
        binding.layoutNewsMarket.txtNews.setOnClickListener {
            refreshNews()
        }
    }

    override fun onCoinItemClicked(dataCoin: CoinsData.Data) {

        val intent = Intent(this, Activity_Coin::class.java)

        val bundle = Bundle()
        bundle.putParcelable("bundle1", dataCoin)
        bundle.putParcelable("bundle2", AboutDataMap[dataCoin.coinInfo.name])
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    private fun getAboutDataFromAssets() {

        val fileString = applicationContext.assets.open("currencyinfo.json").bufferedReader()
            .use { it.readText() }

        AboutDataMap = mutableMapOf()
        val gson = Gson()
        val dataAboutAll = gson.fromJson(fileString, AboutData::class.java)
        dataAboutAll.forEach {
            AboutDataMap[it.currencyName] = CoinAboutItem(
                it.info.web,
                it.info.github,
                it.info.twt,
                it.info.reddit,
                it.info.desc
            )
        }
    }
}