package co.com.test.payments.screens.payments

import co.com.test.payments.screens.payments.usecases.PaymentsUseCase
import co.com.test.payments.screens.payments.view.CreatePaymentFragment
import co.com.test.payments.screens.payments.view.ListPaymentsFragment
import co.com.test.payments.screens.payments.view.adapter.PaymentsAdapter
import co.com.test.payments.screens.payments.viewmodel.PaymentsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created By oscar.vergara on 7/03/2021
 */
val paymentsModule = module {
    viewModel {
        PaymentsViewModel( get(), get() )
    }
    single {
        CreatePaymentFragment()
    }
    single {
        ListPaymentsFragment()
    }
    single {
        PaymentsAdapter()
    }
    single {
        PaymentsUseCase(get(),get(),get())
    }
}