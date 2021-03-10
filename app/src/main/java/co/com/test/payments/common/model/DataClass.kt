package co.com.test.payments.common.model

import com.google.gson.annotations.SerializedName

/**
 * Created By oscar.vergara on 4/03/2021
 */
data class Transaction (
    @SerializedName("status") val status: Status? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("transactionDate") val transactionDate: String? = null,
    @SerializedName("internalReference") val internalReference: Long? = 0,
    @SerializedName("reference") val reference: String? = null,
    @SerializedName("paymentMethod") val paymentMethod: String? = null,
    @SerializedName("franchise") val franchise: String? = null,
    @SerializedName("franchiseName") val franchiseName: String? = null,
    @SerializedName("issuerName") val issuerName: String? = null,
    @SerializedName("amount") val amount: Amount? = null,
    @SerializedName("conversion") val conversion: Conversion? = null,
    @SerializedName("authorization") val authorization: String? = null,
    @SerializedName("receipt") val receipt: Long? = 0,
    @SerializedName("type") val type: String? = null,
    @SerializedName("refunded") val refunded: Boolean? = false,
    @SerializedName("lastDigits") val lastDigits: String? = null,
    @SerializedName("provider") val provider: String? = null,
    @SerializedName("discount") val discount: Any? = null,
    @SerializedName("processorFields") val processorFields: ProcessorFields? = null,
    @SerializedName("additional") val additional: Additional? = null,
    var ipAddress: String? = null
)

data class ProcessorFields(
    val id: String? = null,
    val b24: String? = null
)

data class Additional (
    val merchantCode: String? = null,
    val terminalNumber: String? = null,
    var credit: CreditResponse? = null,
    val totalAmount: Double? = 0.0,
    val interestAmount: Double? = 0.0,
    val installmentAmount: Double? = 0.0,
    val iceAmount: Double? = 0.0
)

data class Credit (
    val code: String? = null,
    val type: String? = null,
    val groupCode: String? = null,
    val installments: List<String>? = null,
    var installment: String? = null,
    val description: String? = null
)

data class CreditResponse (
    val code: String? = null,
    val type: String? = null,
    val groupCode: String? = null,
    var description: String? = null,
    val installments: String? = null
)

data class Amount (
    val taxes: List<Tax>? = null,
    val details: List<Detail>? = null,
    var currency: String? = null,
    var total: Double? = 0.0
)

data class Detail (
    val kind: String? = null,
    val amount: String? = null
)

data class Tax (
    val kind: String? = null,
    val amount: Double? = 0.0,
    val base: Long? = 0,
)

data class Conversion (
    val from: From? = null,
    val to: From? = null,
    val factor: Double? = 0.0
)

data class From (
    val currency: String? = null,
    val total: Double? = 0.0
)

data class Status (
    val status: String? = null,
    val reason: String? = null,
    val message: String? = null,
    val date: String? = null
)

data class ResponseError (
    val status: Status,
)

data class Information (
    val status: Status? = null,
    val provider: String? = null,
    val cardTypes: List<String>? = null,
    val displayInterest: Boolean? = false,
    val requireOtp: Boolean? = false,
    val requireCvv2: Boolean? = false,
    val threeDS: String? = null,
    val credits: List<Credit>? = null
)

data class Interests (
    val status: Status? = null,
    val provider: String? = null,
    val values: InterestValues? = null,
    val conversion: Any? = null
)

data class InterestValues (
    val original: Double? = 0.0,
    val installment: Double? = 0.0,
    val interest: Double? = 0.0,
    val total: Double? = 0.0
)

data class GenerateOTP (
    val status: Status? = null,
    val provider: String? = null
)

data class ValidateOTP (
    val status: Status? = null,
    val provider: String? = null,
    val validated: Boolean? = false,
    val signature: String? = null
)

data class RequestTransaction (
    var ipAddress: String? = null,
    val userAgent: String? = null,
    var locale: String? = null,
    var auth: Auth? = null,
    var payment: Payment? = null,
    val additional: Additional? = null,
    var instrument: Instrument? = null,
    var payer: User? = null,
    var buyer: User? = null
)

data class Auth (
    val login: String? = null,
    val tranKey: String? = null,
    val nonce: String? = null,
    val seed: String? = null
)

data class User (
    var document: String? = null,
    var documentType: String? = null,
    var name: String? = null,
    var surname: String? = null,
    var email: String? = null,
    var mobile: String? = null
)

data class Instrument (
    var otp: String? = null,
    val card: Card? = Card(),
    var credit: Credit? = null,
    val token: TokenClass? = null
)

data class Card (
    var number: String? = null,
    var expirationMonth: String? = null,
    var expirationYear: String? = null,
    var cvv: String? = null
)

data class Payment (
    var reference: String? = null,
    var description: String? = null,
    val amount: Amount = Amount()
)

data class Token (
    val status: Status? = null,
    val provider: String? = null,
    val instrument: Instrument? = null
)

data class TokenClass (
    val token: String? = null,
    val subtoken: String? = null,
    val franchise: String? = null,
    val franchiseName: String? = null,
    val issuerName: String? = null,
    val lastDigits: String? = null,
    val varidUntil: String? = null
)

data class QueryTransactions (
    val status: Status? = null,
    val transactions: List<Transaction>? = null
)
