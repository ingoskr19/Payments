package co.com.test.payments.screens.payments.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.com.test.payments.common.model.*
import co.com.test.payments.screens.payments.usecases.PaymentsUseCase
import co.com.test.payments.screens.payments.model.*
import co.com.test.payments.common.viewmodel.BaseViewModel
import co.com.test.payments.common.viewmodel.ErrorState
import co.com.test.payments.common.viewmodel.Status
import kotlinx.coroutines.launch

/**
 * Created By oscar.vergara on 5/03/2021
 */

class PaymentsViewModel(application: Application, private val useCase: PaymentsUseCase) :
    BaseViewModel(application) {
    private val _tag = "PaymentsViewModel"
    val buyer = MutableLiveData<User?>()
    val payer = MutableLiveData<User?>()
    val payment = MutableLiveData<Payment>()
    val instrument = MutableLiveData<Instrument>()
    val information = MutableLiveData<Information>()
    val interests = MutableLiveData<Interests>()
    val generateOtp = MutableLiveData<GenerateOTP>()
    val validateOtp = MutableLiveData<ValidateOTP>()
    val transaction = MutableLiveData<TransactionEntity>()
    val transactionList = MutableLiveData<List<TransactionEntity>>()

    fun newTransaction() {
        buyer.postValue(User())
        payer.postValue(User())
        payment.postValue(Payment())
        instrument.postValue(Instrument())
        transaction.postValue(TransactionEntity())
    }

    fun requestInformation(): LiveData<Information> {
        launch {

            information.postValue(null)
            loading.postValue(true)
            val response = useCase.requestInformation(instrument.value, payment.value)

            loading.postValue(false)
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        information.postValue(it)
                    }
                }
                Status.ERROR -> {
                    error.postValue(ErrorState(message = response.data?.status?.message))
                }
            }
        }
        return information
    }

    fun getInterests(): LiveData<Interests> {
        launch {
            loading.postValue(true)
            val response = useCase.interests(instrument.value, payment.value)
            loading.postValue(false)
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        interests.postValue(it)
                    }
                }
                Status.ERROR -> {
                    error.postValue(ErrorState(message = response.data?.status?.message))
                }
            }
        }
        return interests
    }

    fun validateOtp(otp: String) : LiveData<ValidateOTP>{
        launch {
            instrument.value?.otp = otp
            val response = useCase.validateOTP(instrument.value, payment.value)
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        validateOtp.postValue(it)
                    }
                }
                Status.ERROR -> {
                    error.postValue(ErrorState(message = response.data?.status?.message))
                }
            }
        }
        return validateOtp
    }

    fun generateOtp(): LiveData<GenerateOTP> {
        launch {
            val response = useCase.generateOTP(instrument.value, payment.value)
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        generateOtp.postValue(it)
                    }
                }
                Status.ERROR -> {
                    error.postValue(ErrorState(message = response.data?.status?.message))
                }
            }
        }
        return generateOtp
    }


    fun createPayment() : LiveData<TransactionEntity> {
        launch {
            loading.postValue(true)
            val response = useCase.createPayment(
                instrument.value,
                payment.value,
                payer.value,
                buyer.value,
                information.value
            )
            loading.postValue(false)
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        transaction.postValue(it)
                    }
                }
                Status.ERROR -> {
                    var message: String? = response.message
                    response.data?.let {
                        message = it.status?.message
                    }
                    error.postValue(ErrorState(message = message))
                }
            }
        }

        return transaction
    }

    fun retrieveListTransactions() {
        launch {
            val response = useCase.getLocalTransactions()
            transactionList.postValue(response)
        }
    }

    fun findByUuid(uuid: Long) {
        launch {
            transaction.postValue(null)
            val resp = useCase.findByUuid(uuid)
            transaction.postValue(resp)
        }
    }

}