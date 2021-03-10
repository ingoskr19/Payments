package co.com.test.payments.screens.payments.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import co.com.test.payments.R
import co.com.test.payments.databinding.FragmentCreatePaymentBinding
import co.com.test.payments.screens.payments.view.adapter.FragmentTabAdapter
import co.com.test.payments.screens.payments.view.listener.TabLayoutListener
import com.google.android.material.tabs.TabLayoutMediator


class CreatePaymentFragment : Fragment(), TabLayoutListener {

    private lateinit var binding: FragmentCreatePaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_payment, container, false
        )
        init()
        return binding.root
    }

    private fun init() {
        binding.viewpager.adapter = FragmentTabAdapter(requireFragmentManager(),lifecycle,this)

        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewpager, true,
                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    when (position) {
                        0 -> tab.text = "Amount\nInformation"
                        1 -> tab.text = "Payment\nInformation"
                        2 -> tab.text = "Installment\nInformation"
                        3 -> tab.text = "Resume"
                    }
                }
            )

        tabLayoutMediator.attach()

        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.getTabAt(position)?.select()
            }
        })
    }

    override fun selectTab(position: Int) {
        binding.tabLayout.getTabAt(position)?.select()
    }
}