package co.com.test.payments.common.viewmodel

/**
 * Created By oscar.vergara on 8/03/2021
 */
data class ErrorState(
    val code: Int? = null,
    val message: String? = null,
    val module: Int? = null
)