package com.alphavantage.app.domain.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtils {

    @SuppressLint("MissingPermission")
    fun hasNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val info = connectivityManager.activeNetworkInfo
            if (info != null) {
                return (info.isConnected && (info.type == ConnectivityManager.TYPE_WIFI || info.type == ConnectivityManager.TYPE_MOBILE))
            }
        } else {
            val net = connectivityManager.activeNetwork
            if (net != null) {
                val capabilities = connectivityManager.getNetworkCapabilities(net)
                return (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
            }
        }

        return false
    }
}