package co.com.test.payments.screens.payments.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import co.com.test.payments.R
import co.com.test.payments.common.BaseFragment
import co.com.test.payments.common.Constants
import co.com.test.payments.databinding.FragmentTransactionDetailBinding
import co.com.test.payments.screens.payments.viewmodel.PaymentsViewModel
import co.com.test.payments.util.getIPHostAddress
import co.com.test.payments.common.viewmodel.ErrorState
import co.com.test.payments.databinding.FragmentPaymentDetailBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class PaymentDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentTransactionDetailBinding

    private val viewModel: PaymentsViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val uuid = PaymentDetailFragmentArgs.fromBundle(it).paymentUuid
            viewModel.findByUuid(uuid.toLong())
        }
    }

    override fun handleError(errorState: ErrorState) {
        showAlertMessage(null,errorState.message?:"",null,null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_transaction_detail, container, false)
        setListener()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        setErrorObserver(viewModel.error)
        setLoadingObserver(viewModel.loading)
        viewModel.transaction.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.ipAddress = getIPHostAddress()
                binding.transaction = it

                when(it.status?.status){
                    "PENDING" -> binding.txtStatus.setBackgroundColor(resources.getColor(R.color.colorPending,null))
                    "APPROVED" -> binding.txtStatus.setBackgroundColor(resources.getColor(R.color.colorApproved,null))
                    else -> binding.txtStatus.setBackgroundColor(resources.getColor(R.color.colorRejected,null))
                }
            }
        })
    }

    private fun setListener() {
        binding.btnNext.setOnClickListener {
            viewModel.newTransaction()
            val action = PaymentDetailFragmentDirections.actionDetailToList()
            Navigation.findNavController(it).navigate(action)
        }
    }
}