package com.example.dunipool.ApiManager.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoinAboutItem (
    var CoinWebsaite :String? = "no data" ,
    var CoinGithub :String? = "no data" ,
    var CoinTwitter :String? = "no data" ,
    var CoinRedit :String? = "no data",
    var Desc :String? = "no data"
) :Parcelable