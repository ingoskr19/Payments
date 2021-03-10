package co.com.test.payments.screens.payments.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import co.com.test.payments.R
import co.com.test.payments.common.Constants
import co.com.test.payments.screens.payments.model.TransactionEntity
import co.com.test.payments.screens.payments.view.adapter.PaymentsAdapter
import co.com.test.payments.screens.payments.view.listener.PaymentListener
import co.com.test.payments.screens.payments.viewmodel.PaymentsViewModel
import kotlinx.android.synthetic.main.fragment_list_payments.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ListPaymentsFragment : Fragment() {

    private val adapter: PaymentsAdapter by inject()
    private val viewModel: PaymentsViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return layoutInflater.inflate(R.layout.fragment_list_payments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.retrieveListTransactions()
        setAdapter()
        setObserver()
    }

    private fun setObserver() {
        viewModel.transactionList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.updateList(it)
                paymentsList.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = this@ListPaymentsFragment.adapter
                }
            }
        })
    }

    private fun setAdapter() {
        paymentsList.adapter = adapter
    }
}