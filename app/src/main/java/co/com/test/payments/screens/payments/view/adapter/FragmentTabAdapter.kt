package co.com.test.payments.screens.payments.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.com.test.payments.screens.payments.view.create.InstallmentInfoFragment
import co.com.test.payments.screens.payments.view.create.AmountInfoFragment
import co.com.test.payments.screens.payments.view.create.PaymentInfoFragment
import co.com.test.payments.screens.payments.view.create.ResumeFragment
import co.com.test.payments.screens.payments.view.listener.TabLayoutListener


/**
 * Created By oscar.vergara on 5/03/2021
 */
class FragmentTabAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val listener: TabLayoutListener
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> PaymentInfoFragment(listener)
            2 -> InstallmentInfoFragment(listener)
            3 -> ResumeFragment()
            else -> AmountInfoFragment(listener)
        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}