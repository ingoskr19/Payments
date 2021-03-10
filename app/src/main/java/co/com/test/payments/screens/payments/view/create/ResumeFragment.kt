package co.com.test.payments.screens.payments.view.create

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import co.com.test.payments.R
import co.com.test.payments.common.BaseFragment
import co.com.test.payments.databinding.FragmentResumeBinding
import co.com.test.payments.common.model.GenerateOTP
import co.com.test.payments.screens.payments.view.CreatePaymentFragmentDirections
import co.com.test.payments.screens.payments.viewmodel.PaymentsViewModel
import co.com.test.payments.common.viewmodel.ErrorState
import co.com.test.payments.screens.payments.model.TransactionEntity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ResumeFragment : BaseFragment() {

    lateinit var binding: FragmentResumeBinding

    private val viewModel: PaymentsViewModel by sharedViewModel()

    override fun handleError(errorState: ErrorState) {
        showAlertMessage(null,errorState.message?:"",null,null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_resume, container, false)
        setListener()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        setErrorObserver(viewModel.error)
        setLoadingObserver(viewModel.loading)
        viewModel.buyer.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.buyer = it
            }
        })
        viewModel.payer.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.payer = it
            }
        })
        viewModel.instrument.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.instrument = it
                val credit = it.credit
                if (!TextUtils.isEmpty(credit.toString())) {
                    binding.creditSelected =
                        credit?.description + " (" + credit?.installment + ") Meses"
                }
            }
        })
        viewModel.payment.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.payment = it
            }
        })
        viewModel.interests.observe(viewLifecycleOwner, Observer {
            binding.interestContainer.visibility = View.GONE
            it?.let {
                binding.interests = it
                binding.interestContainer.visibility = View.VISIBLE
            }
        })
        viewModel.instrument.observe(viewLifecycleOwner, Observer {
            it?.let {

            }
        })
    }

    private fun setListener() {
        binding.btnNext.setOnClickListener(null)
        binding.btnNext.setOnClickListener {
            setErrorObserver(viewModel.error)
            if (viewModel.information.value?.requireOtp == true) {
                viewModel.generateOtp().observe(viewLifecycleOwner, object : Observer<GenerateOTP> {
                    override fun onChanged(generateOTP: GenerateOTP?) {
                        generateOTP?.let {
                            if (it.status?.status.equals("OK")) {
                                OtpAlertDialog(this@ResumeFragment).show(requireFragmentManager(),"OTP")
                            }
                        }
                        viewModel.generateOtp.removeObserver(this)
                    }
                })
            } else {
                consumeCreatePayment()
            }
        }

        binding.cancel.setOnClickListener {
            val action =
                CreatePaymentFragmentDirections.actionCreateToHome()
            Navigation.findNavController(it).navigate(action)
        }
    }

    fun consumeCreatePayment(){
        viewModel.createPayment().observe(viewLifecycleOwner,object: Observer<TransactionEntity>{
            override fun onChanged(t: TransactionEntity?) {
                t?.let {
                    viewModel.transaction.removeObserver(this)
                    val action = CreatePaymentFragmentDirections.actionCreationToDetail()
                    Navigation.findNavController(binding.cancel).navigate(action)
                }
            }
        })
    }
}