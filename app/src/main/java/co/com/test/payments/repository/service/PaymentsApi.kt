package co.com.test.payments.repository.service

import co.com.test.payments.common.model.*
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created By oscar.vergara on 4/03/2021
 */
interface PaymentsApi {
    @POST("gateway/query")
    suspend fun queryTransaction(@Body body: JsonObject): Response<Transaction>

    @POST("gateway/process")
    suspend fun processTransaction(@Body body: JsonObject): Response<Transaction>

    @POST("gateway/safe-process")
    suspend fun safeProcessTransaction(@Body body: JsonObject): Response<Transaction>

    @POST("gateway/information")
    suspend fun getInformation(@Body body: JsonObject): Response<Information>

    @POST("gateway/interests")
    suspend fun interests(@Body body: JsonObject): Response<Interests>

    @POST("gateway/otp/generate")
    suspend fun generateOTP(@Body body: JsonObject): Response<GenerateOTP>

    @POST("gateway/otp/validate")
    suspend fun validateOTP(@Body body: JsonObject): Response<ValidateOTP>

}