package com.alphavantage.app.ui.forex.exchangerate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.alphavantage.app.R
import com.alphavantage.app.databinding.ExchangeRateFragmentBinding
import com.alphavantage.app.domain.widget.EventObserver
import com.alphavantage.app.ui.common.setOnSafeClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.exchange_rate_fragment.fromCurrencyLayout
import kotlinx.android.synthetic.main.exchange_rate_fragment.toCurrencyLayout

class ExchangeRateFragment : Fragment() {

    companion object {
        fun newInstance() =
            ExchangeRateFragment()
    }

    private val viewModel: ExchangeRateViewModel by viewModel()

    private var navController: NavController? = null
    private lateinit var binding: ExchangeRateFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.exchange_rate_fragment,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        fromCurrencyLayout.setOnSafeClickListener {
            viewModel.setGoToFromCurrenciesAction(R.id.action_exchangeRateFragment_to_currenciesFragment)
        }

        toCurrencyLayout.setOnSafeClickListener {
            viewModel.setGoToToCurrenciesAction(R.id.action_exchangeRateFragment_to_currenciesFragment)
        }

        viewModel.toFromCurrenciesEvent.observe(viewLifecycleOwner, EventObserver {
            navController?.navigate(it)
        })
        viewModel.toToCurrenciesEvent.observe(viewLifecycleOwner, EventObserver {
            navController?.navigate(it)
        })
    }
}
