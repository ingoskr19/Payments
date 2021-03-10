package co.com.test.payments.repository.service

import co.com.test.payments.common.model.*
import com.google.gson.JsonObject
import retrofit2.Response

/**
 * Created By oscar.vergara on 5/03/2021
 */
class PaymentsApiImpl(private val api : PaymentsApi) {

    suspend fun queryTransaction(body: JsonObject) : Response<Transaction> {
        return api.queryTransaction(body)
    }

    suspend fun getInformation(body: JsonObject) : Response<Information> {
        return api.getInformation(body)
    }

    suspend fun getInterests(body: JsonObject) : Response<Interests> {
        return api.interests(body)
    }

    suspend fun generateOTP(body: JsonObject) : Response<GenerateOTP> {
        return api.generateOTP(body)
    }

    suspend fun validateOTP(body: JsonObject) : Response<ValidateOTP> {
        return api.validateOTP(body)
    }

    suspend fun createPayment(body: JsonObject) : Response<Transaction> {
        return api.processTransaction(body)
    }

    suspend fun createSafePayment(body: JsonObject) : Response<Transaction> {
        return api.safeProcessTransaction(body)
    }
}