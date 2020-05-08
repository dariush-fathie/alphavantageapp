package com.alphavantage.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alphavantage.app.R
import com.alphavantage.app.ui.exchangerate.ExchangeRateFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ExchangeRateFragment.newInstance())
                .commitNow()
        }
    }

}
