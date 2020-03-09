package com.alphavantage.app.ui.forex.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.work.*
import com.alphavantage.app.R
import com.alphavantage.app.databinding.DailyForexSeriesFragmentBinding
import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.model.forex.ForexSeriesItem
import com.alphavantage.app.domain.util.addDays
import com.alphavantage.app.domain.util.atStartOfTheDay
import com.alphavantage.app.domain.worker.DailyForexWorker
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

class DailyForexSeriesFragment : Fragment() {

    companion object {
        fun newInstance() = DailyForexSeriesFragment()
    }

    private val viewModel: DailyForexSeriesViewModel by viewModel()

    private var navController: NavController? = null
    private lateinit var binding: DailyForexSeriesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.daily_forex_series_fragment,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.dailyChart.description.isEnabled = false
        binding.dailyChart.setPinchZoom(false)
        binding.dailyChart.setDrawGridBackground(false)

        binding.dailyChart.legend.isEnabled = false

        viewModel.items.observe(viewLifecycleOwner, Observer { result ->
            when(result.status) {
                Result.Status.SUCCESS -> buildCandle(result.data!!.items)
                Result.Status.ERROR -> removeCandle()
                Result.Status.LOADING -> binding.dailyChart.resetTracking()
            }
        })
    }

    override fun onStart() {
        super.onStart()

        viewModel.getSeries()
        startDailyChanges()
    }

    private fun startDailyChanges() {
        val date = Date()
        val datePreferred = date.atStartOfTheDay().addDays(1)
        val delay = TimeUnit.MILLISECONDS.toMinutes(datePreferred.time - date.time)

        val inputData = Data.Builder()
        inputData.putString("from_currency_code", viewModel.fromCurrencyCode.value)
        inputData.putString("from_currency_name", viewModel.fromCurrencyName.value)
        inputData.putString("to_currency_code", viewModel.toCurrencyCode.value)
        inputData.putString("to_currency_name", viewModel.toCurrencyName.value)

        val updateWorker = PeriodicWorkRequestBuilder<DailyForexWorker>(
            24,
            TimeUnit.HOURS,
            PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
            TimeUnit.MILLISECONDS
        )
            .setInitialDelay(delay, TimeUnit.MINUTES)
            .addTag("daily_series_update")
            .setInputData(inputData.build())
            .build()

        WorkManager.getInstance(context!!).enqueueUniquePeriodicWork(
            "daily_series_update",
            ExistingPeriodicWorkPolicy.REPLACE,
            updateWorker
        )
    }

    override fun onStop() {
        super.onStop()

        WorkManager.getInstance(context!!).cancelUniqueWork("daily_series_update")
    }

    private fun removeCandle() {
        binding.dailyChart.invalidate()
    }

    private fun buildCandle(items: List<ForexSeriesItem>) {
        val entries = mutableListOf<CandleEntry>()
        for (i in 0 until items.count()) {
            val item = items[i]
            val entry = CandleEntry(
                i.toFloat(),
                item.high.toFloat(),
                item.low.toFloat(),
                item.open.toFloat(),
                item.close.toFloat(),
                item.date
            )
            entries.add(entry)
        }

        val set = CandleDataSet(entries, "${viewModel.fromCurrencyCode.value} to ${viewModel.toCurrencyCode.value}")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.increasingColor = ContextCompat.getColor(context!!, R.color.colorIncrease)
        set.decreasingColor = ContextCompat.getColor(context!!, R.color.colorDecrease)
        set.neutralColor = ContextCompat.getColor(context!!, R.color.colorNeutral)

        val data = CandleData(set)
        binding.dailyChart.data = data
        binding.dailyChart.invalidate()
    }
}
