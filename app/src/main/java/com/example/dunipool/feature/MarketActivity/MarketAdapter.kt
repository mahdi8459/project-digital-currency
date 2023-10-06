package com.example.dunipool.feature.MarketActivity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dunipool.feature.BASE_URL_IMAGE
import com.example.dunipool.R
import com.example.dunipool.databinding.ItemRecyclerMarketBinding
import ir.dunijet.dunipool.apiManager.model.CoinsData

class MarketAdapter(
    private val data: ArrayList<CoinsData.Data>,
    private val recyclerCallBack: RecyclerCallBack
) :
    RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {
    lateinit var binding: ItemRecyclerMarketBinding

    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindViews(dataCoin: CoinsData.Data) {

            binding.txtCoinName.text = dataCoin.coinInfo.fullName
            binding.txtPrice.text =dataCoin.dISPLAY.uSD.pRICE

            //------------------------

            val txtTaghir = dataCoin.dISPLAY.uSD.cHANGEPCT24HOUR
            if (txtTaghir > 0.toString()) {
                binding.txtTaghir.text = dataCoin.dISPLAY.uSD.cHANGEPCT24HOUR  + "%"
                binding.txtTaghir.setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorGain))
            } else if (txtTaghir < 0.toString()){
                binding.txtTaghir.text = dataCoin.dISPLAY.uSD.cHANGEPCT24HOUR + "%"
                binding.txtTaghir.setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorRed))
            } else {
                binding.txtTaghir.text = 0.toString() + "%"
                binding.txtTaghir.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            }

            binding.txtMarketCap.text = dataCoin.dISPLAY.uSD.mKTCAP


            Glide
                .with(itemView)
                .load(BASE_URL_IMAGE + dataCoin.coinInfo.imageUrl)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.imgItem)

            itemView.setOnClickListener {
                recyclerCallBack.onCoinItemClicked(dataCoin)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val infalte = LayoutInflater.from(parent.context)
        binding = ItemRecyclerMarketBinding.inflate(infalte, parent, false)

        return MarketViewHolder(binding.root)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bindViews(data[position])
    }

    interface RecyclerCallBack {

        fun onCoinItemClicked(dataCoin: CoinsData.Data)
    }

}