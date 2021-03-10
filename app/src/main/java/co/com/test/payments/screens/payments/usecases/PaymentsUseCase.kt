package co.com.test.payments.screens.payments.usecases

import co.com.test.payments.common.model.*
import co.com.test.payments.screens.payments.model.*
import co.com.test.payments.util.createAuth
import co.com.test.payments.repository.service.PaymentsApiImpl
import co.com.test.payments.util.ErrorUtils
import co.com.test.payments.util.getIPHostAddress
import co.com.test.payments.util.random
import co.com.test.payments.common.viewmodel.StatusData
import co.com.test.payments.repository.database.PaymentsDao
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Response
import java.lang.Exception

/**
 * Created By oscar.vergara on 5/03/2021
 */
class PaymentsUseCase(
    private val api: PaymentsApiImpl,
    private val dao: PaymentsDao,
    private val errorUtils: ErrorUtils
) {

    private val _tag = "PaymentsUseCase"
    private val locale = "es_CO"
    private val userAgent = "Mozilla/5.0"

    suspend fun requestInformation(
        instrument: Instrument?,
        payment: Payment?
    ): StatusData<Information?> {
        val query = JsonObject()
        val auth = createAuth()

        payment?.reference = random()

        query.add("instrument", JsonParser.parseString(Gson().toJson(instrument)))
        query.add("payment", JsonParser.parseString(Gson().toJson(payment)))
        query.add("auth", auth)

        val response = api.getInformation(query)

        return if (response.isSuccessful) {
            StatusData.success(response.body())
        } else {
            StatusData.error(
                Information(
                    status = errorUtils.parseError(
                        response
                    )?.status
                ),
                response.message()
            )
        }
    }

    suspend fun interests(instrument: Instrument?, payment: Payment?): StatusData<Interests?> {
        val query = JsonObject()
        val auth = createAuth()

        payment?.reference = random()

        query.add("instrument", JsonParser.parseString(Gson().toJson(instrument)))
        query.add("payment", JsonParser.parseString(Gson().toJson(payment)))
        query.add("auth", auth)

        val response = api.getInterests(query)

        return if (response.isSuccessful) {
            StatusData.success(response.body())
        } else {
            StatusData.error(
                Interests(
                    status = errorUtils.parseError(
                        response
                    )?.status
                ),
                response.message()
            )
        }
    }

    suspend fun validateOTP(instrument: Instrument?, payment: Payment?): StatusData<ValidateOTP?> {
        val query = JsonObject()
        val auth = createAuth()

        payment?.reference = random()

        query.add("instrument", JsonParser.parseString(Gson().toJson(instrument)))
        query.add("payment", JsonParser.parseString(Gson().toJson(payment)))
        query.add("auth", auth)

        val response = api.validateOTP(query)

        return if (response.isSuccessful) {
            StatusData.success(response.body())
        } else {
            StatusData.error(
                ValidateOTP(
                    status = errorUtils.parseError(
                        response
                    )?.status
                ),
                response.message()
            )
        }
    }

    suspend fun generateOTP(instrument: Instrument?, payment: Payment?): StatusData<GenerateOTP?> {
        val query = JsonObject()
        val auth = createAuth()

        payment?.reference = random()

        query.add("instrument", JsonParser.parseString(Gson().toJson(instrument)))
        query.add("payment", JsonParser.parseString(Gson().toJson(payment)))
        query.add("auth", auth)

        val response = api.generateOTP(query)

        return if (response.isSuccessful) {
            StatusData.success(response.body())
        } else {
            StatusData.error(
                GenerateOTP(
                    status = errorUtils.parseError(
                        response
                    )?.status
                ),
                response.message()
            )
        }
    }

    suspend fun createPayment(
        instrument: Instrument?,
        payment: Payment?,
        payer: User?,
        buyer: User?,
        information: Information?
    ): StatusData<TransactionEntity?> {

        val requestJson = JsonObject()

        payment?.reference = random()
        payment?.description = "Payment ${payment?.reference}"
        if(information?.requireOtp == false){
            instrument?.otp = "123456"
        }

        requestJson.add("auth", createAuth())
        requestJson.add("payer", JsonParser.parseString(Gson().toJson(payer)))
        requestJson.add("buyer", JsonParser.parseString(Gson().toJson(buyer)))
        requestJson.add("instrument", JsonParser.parseString(Gson().toJson(instrument)))
        requestJson.add("payment", JsonParser.parseString(Gson().toJson(payment)))
        requestJson.addProperty("ip", getIPHostAddress())
        requestJson.addProperty("local", locale)
        requestJson.addProperty("userAgent", userAgent)

        try {

            val response = consumeProcessTransaction((information?.requireOtp == true), requestJson)

            val localTransaction = saveTransactionLocal(
                response.body(), payer, instrument?.credit?.description ?: ""
            )

            return StatusData.success(localTransaction)

        } catch (e: Exception) {
            return StatusData.error(data = null, message = "Error: ${e.message}")
        }
    }

    private suspend fun saveTransactionLocal(
        transaction: Transaction?,
        payer: User?,
        credit: String
    ): TransactionEntity {
        val localTransaction = TransactionEntity()

        localTransaction.state = transaction?.status?.status
        localTransaction.statusCode = transaction?.status?.reason
        localTransaction.statusMessage = transaction?.status?.message
        localTransaction.totalAmount = "${transaction?.amount?.currency} $${transaction?.additional?.totalAmount}"
        localTransaction.initialAmount = "${transaction?.amount?.currency} $${transaction?.amount?.total}"
        localTransaction.acurrency = transaction?.amount?.currency
        localTransaction.date = transaction?.date
        localTransaction.installment = transaction?.additional?.credit?.installments?.toLong()
        localTransaction.installmentQuota = "${transaction?.amount?.currency} $${transaction?.additional?.installmentAmount}"
        localTransaction.interests = "${transaction?.amount?.currency} $${transaction?.additional?.interestAmount}"
        localTransaction.payerName = payer?.name
        localTransaction.payerEmail = payer?.email
        localTransaction.payerDocument = "${payer?.documentType} ${payer?.document}"
        localTransaction.transactionDate = transaction?.transactionDate
        localTransaction.internalReference = transaction?.internalReference
        localTransaction.reference = transaction?.reference
        localTransaction.issuerName = transaction?.issuerName
        localTransaction.authorization = transaction?.authorization
        localTransaction.lastDigits = transaction?.lastDigits
        localTransaction.ipAddress = getIPHostAddress()
        localTransaction.credit = credit

        dao.insertAll(localTransaction)

        return localTransaction
    }

    private suspend fun consumeProcessTransaction(
        safe: Boolean,
        requestJson: JsonObject
    ): Response<Transaction> {
        return if (safe) {
            api.createPayment(requestJson)
        } else {
            api.createSafePayment(requestJson)
        }
    }

    suspend fun getLocalTransactions(): List<TransactionEntity> {
        return dao.getAll()
    }

    suspend fun findByUuid(uuid: Long): TransactionEntity {
        return dao.getById(uuid)
    }
}