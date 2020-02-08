package com.alphavantage.app.ui.forex.daily

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.alphavantage.app.R

class DailyForexSeriesFragment : Fragment() {

    companion object {
        fun newInstance() = DailyForexSeriesFragment()
    }

    private lateinit var viewModel: DailyForexSeriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.daily_forex_series_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DailyForexSeriesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
