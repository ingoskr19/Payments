package co.com.test.payments.screens.payments.view.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import co.com.test.payments.R
import co.com.test.payments.databinding.OtpDialogBinding
import co.com.test.payments.screens.payments.viewmodel.PaymentsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created By oscar.vergara on 7/03/2021
 */
class OtpAlertDialog(val fragment: Fragment) : DialogFragment() {

    private lateinit var binding: OtpDialogBinding
    private val viewModel: PaymentsViewModel by fragment.sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.otp_dialog,container,false)
        setListener()
        isCancelable = false
        return binding.root
    }

    private fun setListener() {
        binding.btnValidate.setOnClickListener {
            viewModel.validateOtp(binding.code.text.toString()).observe(viewLifecycleOwner, Observer {
                it?.let {
                    if (it.validated == true){
                        consumeCreate(it.signature)
                    }
                    this.dismiss()
                }
            })
        }

        binding.cancel.setOnClickListener {
            this.dismiss()
        }
    }

    private fun consumeCreate(otp: String?) {
        viewModel.instrument.value?.otp = otp
        this.dismiss()
        (fragment as ResumeFragment).consumeCreatePayment()
    }

}