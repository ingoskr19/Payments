package co.com.test.payments.util

import co.com.test.payments.common.model.ResponseError
import co.com.test.payments.common.model.Status
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException


/**
 * Created By oscar.vergara on 7/03/2021
 */
class ErrorUtils(private val retrofit: Retrofit) {

    fun parseError(response: Response<*>): ResponseError? {
        val converter: Converter<ResponseBody, ResponseError> = retrofit
            .responseBodyConverter(ResponseError::class.java, arrayOfNulls<Annotation>(0))
        val error: ResponseError?
        error = try {
            converter.convert(response.errorBody())
        } catch (e: IOException) {
            return ResponseError(
                status = Status(
                    e.message
                )
            )
        }
        return error
    }
}