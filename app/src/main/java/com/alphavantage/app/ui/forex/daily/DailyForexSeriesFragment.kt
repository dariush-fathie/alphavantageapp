package com.alphavantage.app.ui.forex.daily

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController

import com.alphavantage.app.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class DailyForexSeriesFragment : Fragment() {

    companion object {
        fun newInstance() = DailyForexSeriesFragment()
    }

    private val viewModel: DailyForexSeriesViewModel by viewModel()

    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.setLifecycle(lifecycle)
        return inflater.inflate(R.layout.daily_forex_series_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
