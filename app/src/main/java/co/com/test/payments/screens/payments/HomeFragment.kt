package co.com.test.payments.screens.payments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import co.com.test.payments.R
import co.com.test.payments.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home, container, false)
        binding.goCreate.setOnClickListener(this::onClick)
        binding.goList.setOnClickListener(this::onClick)
        return binding.root
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.goList.id -> {
                val action =
                    HomeFragmentDirections.actionHomeToList()
                Navigation.findNavController(v).navigate(action)
            }
            binding.goCreate.id -> {
                val action =
                    HomeFragmentDirections.actionHomeToCreate()
                Navigation.findNavController(v).navigate(action)
            }
        }
    }
}