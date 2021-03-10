package co.com.test.payments.common

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import co.com.test.payments.R
import co.com.test.payments.databinding.LoadingLayoutBinding
import co.com.test.payments.common.viewmodel.ErrorState

/**
 * Created By oscar.vergara on 8/03/2021
 */
abstract class BaseFragment : Fragment() {

    private var dialog: AlertDialog? = null
    private var loading: AlertDialog? = null
    private lateinit var loadingBinding: LoadingLayoutBinding

    fun setErrorObserver(error: MutableLiveData<ErrorState>) {
        if(error.hasObservers()) {
            error.removeObservers(viewLifecycleOwner)
        }
        if (!error.hasObservers()) {
            error.observe(viewLifecycleOwner, object : Observer<ErrorState> {
                override fun onChanged(e: ErrorState?) {
                    e?.let {
                        error.removeObserver(this)
                        error.postValue(null)
                        handleError(it)
                    }
                }
            })
        }
    }

    fun setLoadingObserver(load: MutableLiveData<Boolean>) {
        createLoadingDialog()
        load.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it == true) {
                showLoading()
            } else {
                loading?.dismiss()
            }
        })
    }

    fun showLoading() {
        loading?.dismiss()
        loading?.show()
    }

    private fun createLoadingDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        loadingBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.loading_layout,
            null,
            false
        )
        alertDialog.setView(loadingBinding.root)
        loading = alertDialog.create()
    }

    abstract fun handleError(errorState: ErrorState)

    fun showAlertMessage(
        title: String?,
        message: String,
        positiveButton: String?,
        negativeButton: String?
    ) {
        dialog?.let {
            dialog!!.dismiss()
        }

        val alertDialog = AlertDialog.Builder(requireContext())
        title?.let {
            alertDialog.setTitle(title)
        }

        alertDialog.setPositiveButton(
            positiveButton ?: "Continue"
        ) { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }

        negativeButton?.let {
            alertDialog.setNegativeButton(negativeButton) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
        }
        title?.let {
            alertDialog.setTitle(title)
        }

        alertDialog.setMessage(message)
        alertDialog.setCancelable(false)

        dialog = alertDialog.create()

        dialog?.show()
    }
}