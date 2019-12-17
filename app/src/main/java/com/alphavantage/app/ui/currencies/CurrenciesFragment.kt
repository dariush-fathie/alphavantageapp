package com.alphavantage.app.ui.currencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.alphavantage.app.R
import com.alphavantage.app.databinding.CurrenciesFragmentBinding
import com.alphavantage.app.domain.model.Result
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrenciesFragment : Fragment() {

    companion object {
        fun newInstance() = CurrenciesFragment()
    }

    private lateinit var adapter: CurrenciesAdapter
    private lateinit var binding: CurrenciesFragmentBinding

    private val viewModel: CurrenciesViewModel by viewModel()
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.currencies_fragment,
            container,
            false
        ) as CurrenciesFragmentBinding

        binding.viewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.toPrevious.observe(viewLifecycleOwner,
            Observer {
                navController?.popBackStack()
            })

        initializeList()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvCurrencies.adapter = null
    }

    private fun initializeList() {
        adapter = CurrenciesAdapter(viewModel)
        binding.rvCurrencies.adapter = adapter

        viewModel.items.observe(viewLifecycleOwner,
            Observer { result ->
                // TODO loading
                when(result.status) {
                    Result.Status.ERROR -> {
                        result.message.let { Snackbar.make(binding.root, it!!, Snackbar.LENGTH_LONG).show() }
                    }
                    Result.Status.SUCCESS -> {
                        result.data?.let { adapter.submitList(it) }
                    }
                }
            })
    }
}
