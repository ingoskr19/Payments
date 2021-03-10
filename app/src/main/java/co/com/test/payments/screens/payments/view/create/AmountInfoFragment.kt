package co.com.test.payments.screens.payments.view.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import co.com.test.payments.R
import co.com.test.payments.databinding.FragmentAmountInfoBinding
import co.com.test.payments.common.model.Payment
import co.com.test.payments.common.model.User
import co.com.test.payments.screens.payments.view.listener.TabLayoutListener
import co.com.test.payments.screens.payments.viewmodel.PaymentsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AmountInfoFragment(private val tabLayoutListener: TabLayoutListener) : Fragment() {

    private lateinit var binding: FragmentAmountInfoBinding
    private val viewModel: PaymentsViewModel by sharedViewModel()
    private val buyer = User()
    private val payment = Payment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setSpinnersAdapter()
        setSpinnersListener()
    }

    private fun setListeners() {
        binding.btnNext.setOnClickListener {
            buyer.documentType = binding.spinnerDocumentType.selectedItem as String
            buyer.document = binding.editTextDocument.text.toString()
            buyer.name = binding.editTextName.text.toString()
            buyer.surname = binding.editTextSurName.text.toString()
            buyer.email = binding.editTextEmail.text.toString()
            buyer.mobile = binding.editTextMobileNumber.text.toString()

            viewModel.buyer.postValue(buyer)

            payment.amount.total = binding.editTextAmount.text.toString().toDouble()
            payment.amount.currency = binding.spinnerCurrency.selectedItem as String

            viewModel.payment.postValue(payment)

            tabLayoutListener.selectTab(1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_amount_info, container, false)
        return binding.root
    }

    private fun setSpinnersListener() {
        val listener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //No implementation needed
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent?.id == binding.spinnerCurrency.id) {
                    payment.amount.currency = binding.spinnerCurrency.selectedItem as String
                }

                if (parent?.id == binding.spinnerDocumentType.id) {
                    buyer.documentType = binding.spinnerDocumentType.selectedItem as String
                }
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //No impletation needed
            }
        }
        binding.spinnerCurrency.onItemSelectedListener = listener
        binding.spinnerDocumentType.onItemSelectedListener = listener
    }

    private fun setSpinnersAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currency_list,
            android.R.layout.simple_spinner_item
        )
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCurrency.adapter = adapter
            }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.document_types_list,
            android.R.layout.simple_spinner_item
        )
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerDocumentType.adapter = adapter
            }
    }
}