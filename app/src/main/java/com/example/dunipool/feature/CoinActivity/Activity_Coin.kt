package com.example.dunipool.feature.CoinActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.dunipool.ApiManager.model.CoinAboutItem
import com.example.dunipool.R
import com.example.dunipool.databinding.ActivityCoinBinding
import com.example.dunipool.feature.ALL
import com.example.dunipool.feature.BASE_URL_TWITTER
import com.example.dunipool.feature.HOUR
import com.example.dunipool.feature.HOURS24
import com.example.dunipool.feature.MONTH
import com.example.dunipool.feature.MONTH3
import com.example.dunipool.feature.WEEK
import com.example.dunipool.feature.YEAR
import ir.dunijet.dunipool.apiManager.ApiManager
import ir.dunijet.dunipool.apiManager.model.ChartData
import ir.dunijet.dunipool.apiManager.model.CoinsData

class Activity_Coin : AppCompatActivity() {
    val apiManager = ApiManager()
    private lateinit var binding: ActivityCoinBinding
    private lateinit var dataThisCoin: CoinsData.Data
    lateinit var DataThisCoinAbout: CoinAboutItem

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCoinBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fromIntent = intent.getBundleExtra("bundle")!!
        dataThisCoin = fromIntent.getParcelable("bundle1")!!

        if (fromIntent.getParcelable<CoinAboutItem>("bundle2") != null) {
            DataThisCoinAbout = fromIntent.getParcelable("bundle2")!!
        } else {
            DataThisCoinAbout = CoinAboutItem()
        }

        binding.layoutToolbarCoin.toolbar.title = dataThisCoin.coinInfo.fullName

        binding.swipeRefreshChart.setOnRefreshListener {

            initChar()
            initStatisticsUi()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshChart.isRefreshing = false
            } , 1000)
            binding.layoutChartCoin.radio12h.isChecked = true
        }

        initUi()

    }

    private fun initUi() {
        initChar()
        initStatisticsUi()
        initAbout()
    }

    @SuppressLint("SetTextI18n")
    private fun initAbout() {

        binding.layoutAboutCoin.txtWebsite.text = DataThisCoinAbout.CoinWebsaite
        binding.layoutAboutCoin.txtGithub.text = DataThisCoinAbout.CoinGithub
        binding.layoutAboutCoin.txtReddit.text = DataThisCoinAbout.CoinRedit
        binding.layoutAboutCoin.txtTwitter.text = "@" + DataThisCoinAbout.CoinTwitter
        binding.layoutAboutCoin.txtAboutCoin.text = DataThisCoinAbout.Desc

        binding.layoutAboutCoin.txtWebsite.setOnClickListener {
            openWebsitedataThisCoin(DataThisCoinAbout.CoinWebsaite!!)
        }

        binding.layoutAboutCoin.txtGithub.setOnClickListener {
            openWebsitedataThisCoin(DataThisCoinAbout.CoinGithub!!)
        }

        binding.layoutAboutCoin.txtReddit.setOnClickListener {
            openWebsitedataThisCoin(DataThisCoinAbout.CoinRedit!!)
        }

        binding.layoutAboutCoin.txtTwitter.setOnClickListener {
            openWebsitedataThisCoin(BASE_URL_TWITTER + DataThisCoinAbout.CoinTwitter!!)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initStatisticsUi() {
        binding.layoutStaticticsCoin.tvOpenAmount.text = "$" + dataThisCoin.dISPLAY.uSD.oPEN24HOUR
        binding.layoutStaticticsCoin.tvTodaysHighAmount.text = "$" + dataThisCoin.dISPLAY.uSD.hIGH24HOUR
        binding.layoutStaticticsCoin.tvTodayLowAmount.text = "$" + dataThisCoin.dISPLAY.uSD.lOW24HOUR
        binding.layoutStaticticsCoin.tvChangeTodayAmount.text = "$" + dataThisCoin.dISPLAY.uSD.cHANGE24HOUR
        binding.layoutStaticticsCoin.tvAlgorithm.text = dataThisCoin.coinInfo.algorithm
        binding.layoutStaticticsCoin.tvTotalVolume.text = dataThisCoin.dISPLAY.uSD.tOTALVOLUME24H
        binding.layoutStaticticsCoin.tvAvgMarketCapAmount.text = dataThisCoin.dISPLAY.uSD.mKTCAP
        binding.layoutStaticticsCoin.tvSupplyNumber.text = dataThisCoin.dISPLAY.uSD.sUPPLY
    }

    @SuppressLint("SetTextI18n")
    private fun initChar() {

        var peroid: String = HOUR
        requestShowData(peroid)

        binding.layoutChartCoin.radioMain.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_12h -> {
                    peroid = HOUR
                }

                R.id.radio_1d -> {
                    peroid = HOURS24
                }

                R.id.radio_1w -> {
                    peroid = WEEK
                }

                R.id.radio_1m -> {
                    peroid = MONTH
                }

                R.id.radio_3m -> {
                    peroid = MONTH3
                }

                R.id.radio_1y -> {
                    peroid = YEAR
                }

                R.id.radio_all -> {
                    peroid = ALL
                }
            }
            requestShowData(peroid)
        }

        binding.layoutChartCoin.txtChartPrice.text = dataThisCoin.dISPLAY.uSD.pRICE
        binding.layoutChartCoin.txtChartChange1.text = dataThisCoin.dISPLAY.uSD.cHANGE24HOUR

        val txtTaghir = dataThisCoin.rAW.uSD.cHANGEPCT24HOUR
        if (txtTaghir > 0.toDouble()) {
            binding.layoutChartCoin.txtChangeChart2.text = dataThisCoin.rAW.uSD.cHANGEPCT24HOUR.toString() + "%"
            binding.layoutChartCoin.txtChangeChart2.setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorGain))
            binding.layoutChartCoin.sparkViewMain.lineColor = (ContextCompat.getColor(binding.root.context, R.color.colorGain))
            binding.layoutChartCoin.txtChartUpDown.setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorGain))
        } else if (txtTaghir < 0.toDouble()) {
            binding.layoutChartCoin.txtChangeChart2.text = dataThisCoin.rAW.uSD.cHANGEPCT24HOUR.toString() + "%"
            binding.layoutChartCoin.txtChartUpDown.text = "▼"
            binding.layoutChartCoin.txtChangeChart2.setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorRed))
            binding.layoutChartCoin.txtChartUpDown.setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorRed))
            binding.layoutChartCoin.sparkViewMain.lineColor = (ContextCompat.getColor(binding.root.context, R.color.colorRed))
        } else {
            binding.layoutChartCoin.txtChangeChart2.text = 0.toString() + "%"
            binding.layoutChartCoin.txtChangeChart2.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            binding.layoutChartCoin.sparkViewMain.lineColor = (ContextCompat.getColor(binding.root.context, R.color.white))
        }
        binding.layoutChartCoin.sparkViewMain.setScrubListener {

            val txtTaghir = dataThisCoin.rAW.uSD.cHANGEPCT24HOUR

            // show price
            if (it == null ) {
                binding.layoutChartCoin.txtChartPrice.text = dataThisCoin.dISPLAY.uSD.pRICE
            } else {
                // show price this point
                binding.layoutChartCoin.txtChartPrice.text = "$" + (it as ChartData.Data).close.toString()
            }
        }
    }

    fun requestShowData(period: String) {
        apiManager.getChartData(dataThisCoin.coinInfo.name,
            period,
            object : ApiManager.ApiCallback<Pair<List<ChartData.Data>, ChartData.Data?>> {
                override fun onSuccess(data: Pair<List<ChartData.Data>, ChartData.Data?>) {

                    val chartAdapter = ChartAdapter(data.first, data.second?.open.toString())
                    binding.layoutChartCoin.sparkViewMain.adapter = chartAdapter
                }

                override fun onError(errorMessage: String) {
                    Toast.makeText(this@Activity_Coin, "error$errorMessage", Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private fun openWebsitedataThisCoin(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
