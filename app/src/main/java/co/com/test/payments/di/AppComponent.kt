package co.com.test.payments.di

import co.com.test.payments.screens.payments.paymentsModule

/**
 * Created By oscar.vergara on 7/03/2021
 */
val appComponent = listOf(
    networkServiceModule,
    paymentsModule,
    databaseModule
)