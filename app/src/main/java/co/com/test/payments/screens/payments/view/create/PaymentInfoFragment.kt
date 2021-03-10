package co.com.test.payments.screens.payments.view.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import co.com.test.payments.R
import co.com.test.payments.common.BaseFragment
import co.com.test.payments.databinding.FragmentPaymentInfoBinding
import co.com.test.payments.screens.payments.viewmodel.PaymentsViewModel
import co.com.test.payments.common.model.Instrument
import co.com.test.payments.common.model.User
import co.com.test.payments.screens.payments.view.listener.TabLayoutListener
import co.com.test.payments.common.viewmodel.ErrorState
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PaymentInfoFragment(private val tabLayoutListener: TabLayoutListener) : BaseFragment() {

    private var cardValidated: Boolean = false
    private lateinit var binding: FragmentPaymentInfoBinding

    private val viewModel: PaymentsViewModel by sharedViewModel()

    private var instrument = Instrument()
    private lateinit var documentTypes: Array<String>

    private var flag = true
    private var payer = User()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_payment_info,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setSpinnersAdapter()
        setSpinnersListener()
        setObservers()
    }

    private fun setListeners() {
        binding.btnNext.setOnClickListener(this::nextClick)
        binding.btnValidate.setOnClickListener(this::validateCard)
        binding.checkedSameBuyerPerson.setOnClickListener(this::samePersonClick)
    }

    private fun setInputEnable(b: Boolean) {
        binding.editTextDocument.isEnabled = b
        binding.editTextName.isEnabled = b
        binding.editTextSurName.isEnabled = b
        binding.editTextEmail.isEnabled = b
        binding.editTextMobileNumber.isEnabled = b
        binding.spinnerDocumentType.isEnabled = b
    }

    private fun setObservers() {
        setErrorObserver(viewModel.error)
        setLoadingObserver(viewModel.loading)
        viewModel.instrument.observe(viewLifecycleOwner, Observer {
            instrument = it
            binding.editTextCard.setText(instrument.card?.number)
        })

        viewModel.payer.observe(viewLifecycleOwner, Observer {
            it?.let {
                payer = it
                binding.editTextDocument.setText(payer.document)
                binding.editTextName.setText(payer.name)
                binding.editTextSurName.setText(payer.surname)
                binding.editTextEmail.setText(payer.email)
                binding.editTextMobileNumber.setText(payer.mobile)
                binding.spinnerDocumentType.setSelection(
                    documentTypes.asList().indexOf(payer.documentType), true
                )
            }
        })

    }

    override fun handleError(errorState: ErrorState) {
        showAlertMessage(null,errorState.message?:"",null,null)
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
                payer.documentType = binding.spinnerDocumentType.selectedItem as String
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
        binding.spinnerDocumentType.onItemSelectedListener = listener
    }

    private fun setSpinnersAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.document_types_list,
            android.R.layout.simple_spinner_item
        )
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerDocumentType.adapter = adapter
            }
        documentTypes = resources.getStringArray(R.array.document_types_list)
    }

    private fun nextClick(v: View) {
        if (viewModel.information.value != null ) {
            payer.documentType = binding.spinnerDocumentType.selectedItem as String
            payer.name = binding.editTextName.text.toString()
            payer.surname = binding.editTextSurName.text.toString()
            payer.email = binding.editTextEmail.text.toString()
            payer.mobile = binding.editTextMobileNumber.text.toString()

            if (binding.editTextCardExpirationDate.text?.length == 4){
                val expirationDate = binding.editTextCardExpirationDate.text.toString()
                instrument.card?.expirationMonth = expirationDate.substring(0,2)
                instrument.card?.expirationYear = expirationDate.substring(2,4)
                instrument.card?.cvv = binding.editTextCardCvv.text.toString()
            }

            if (binding.checkedSameBuyerPerson.isChecked) {
                viewModel.payer.postValue(viewModel.buyer.value)
            } else {
                viewModel.payer.postValue(payer)
            }
            tabLayoutListener.selectTab(2)
        }

    }

    private fun validateCard(v: View) {
        binding.cvvContainer.visibility = View.GONE
        binding.payerContainer.visibility = View.GONE
        instrument.card?.number = binding.editTextCard.text.toString()
        viewModel.instrument.postValue(instrument)
        setErrorObserver(viewModel.error)
        viewModel.requestInformation().observe(viewLifecycleOwner, Observer {
            it?.let {information ->
                cardValidated = true
                if (information.requireCvv2 == true) {
                    binding.cvvContainer.visibility = View.VISIBLE
                    binding.editTextCardCvv.requestFocus()
                    binding.payerContainer.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun samePersonClick(v: View) {
        if (flag) {
            flag = false
            if (!binding.checkedSameBuyerPerson.isChecked) {
                viewModel.payer.postValue(viewModel.buyer.value)
                setInputEnable(false)
            } else {
                viewModel.payer.postValue(User())
                setInputEnable(true)
            }
            binding.checkedSameBuyerPerson.isChecked = !binding.checkedSameBuyerPerson.isChecked
            flag = true
        }
    }
}