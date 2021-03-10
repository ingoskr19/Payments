
package co.com.test.payments.screens.payments.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import co.com.test.payments.common.model.Status

/**
 * Created By oscar.vergara on 8/03/2021
 */
@Entity(tableName = "transactions")
data class TransactionEntity (

    @Ignore
    var status: Status? = null,

    @ColumnInfo(name = "status")
    var state: String? = null,

    @ColumnInfo(name = "status_code")
    var statusCode: String? = null,

    @ColumnInfo(name = "status_message")
    var statusMessage: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null,

    @ColumnInfo(name = "initial_amount")
    var initialAmount: String? = null,

    @ColumnInfo(name = "total_amount")
    var totalAmount: String? = null,

    @ColumnInfo(name = "installment")
    var installment: Long? = 0,

    @ColumnInfo(name = "installment_quota")
    var installmentQuota: String? = null,

    @ColumnInfo(name = "interests")
    var interests: String? = null,

    @ColumnInfo(name = "payer_name")
    var payerName: String? = null,

    @ColumnInfo(name = "payer_document")
    var payerDocument: String? = null,

    @ColumnInfo(name = "payer_email")
    var payerEmail: String? = null,

    @ColumnInfo(name = "transaction_date")
    var transactionDate: String? = null,

    @ColumnInfo(name = "internal_reference")
    var internalReference: Long? = 0,

    @ColumnInfo(name = "reference")
    var reference: String? = null,

    @ColumnInfo(name = "issuer_name")
    var issuerName: String? = null,

    @ColumnInfo(name = "authorization")
    var authorization: String? = null,

    @ColumnInfo(name = "last_digits")
    var lastDigits: String? = null,

    @ColumnInfo(name = "card_number")
    var cardNumber: String? = null,

    @ColumnInfo(name = "bank")
    var bank: String? = null,

    @ColumnInfo(name = "acurrency")
    var acurrency: String? = null,

    @ColumnInfo(name = "ip_address")
    var ipAddress: String? = null,

    @ColumnInfo(name = "credit")
    var credit: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0;
}