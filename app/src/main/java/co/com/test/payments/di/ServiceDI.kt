package co.com.test.payments.di

import android.app.Application
import androidx.room.Room
import co.com.test.payments.BuildConfig
import co.com.test.payments.repository.database.PaymentsDao
import co.com.test.payments.repository.database.PaymentsDatabase
import co.com.test.payments.repository.service.PaymentsApi
import co.com.test.payments.repository.service.PaymentsApiImpl
import co.com.test.payments.util.ErrorUtils
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created By oscar.vergara on 7/03/2021
 */
val networkServiceModule = module {

    single {
        val logging = HttpLoggingInterceptor ()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient.build())
            .baseUrl(BuildConfig.BASE_URL).build()
    }
    single{ get<Retrofit>().create(PaymentsApi::class.java) }

    single {
        PaymentsApiImpl(get())
    }

    single {
        ErrorUtils(get())
    }
}

val databaseModule = module {

    fun provideDatabase(application: Application): PaymentsDatabase {
        return PaymentsDatabase(application)
    }

    fun provideDao(database: PaymentsDatabase): PaymentsDao {
        return database.transactionsDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
}