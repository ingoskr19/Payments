package co.com.test.payments.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Created By oscar.vergara on 5/03/2021
 */
open class BaseViewModel(application: Application): AndroidViewModel(application), CoroutineScope {

    val error   = MutableLiveData<ErrorState>()
    val loading = MutableLiveData<Boolean>()

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}