package co.com.test.payments.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import co.com.test.payments.screens.payments.model.TransactionEntity

/**
 * Created By oscar.vergara on 08/03/2021
 */
@Dao
interface PaymentsDao {
    @Insert
    suspend fun insertAll(vararg dogs: TransactionEntity): List<Long>

    @Query("SELECT * FROM transactions")
    suspend fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM transactions where uuid = :id")
    suspend fun getById(id: Long): TransactionEntity

    @Query("DELETE FROM transactions where uuid = :id")
    suspend fun deleteById(id: Int)
}