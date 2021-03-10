package co.com.test.payments.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.com.test.payments.screens.payments.model.TransactionEntity

/**
 * Created By oscar.vergara on 8/03/2021
 */
@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
abstract class PaymentsDatabase: RoomDatabase() {

    abstract fun transactionsDao(): PaymentsDao

    companion object {
        @Volatile private var instance: PaymentsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            PaymentsDatabase::class.java,
            "payments.database"
        ).build()
    }
}