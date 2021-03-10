package co.com.test.payments.screens.payments.view.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import co.com.test.payments.R
import co.com.test.payments.common.BaseFragment
import co.com.test.payments.databinding.FragmentInstallmentInfoBinding
import co.com.test.payments.common.model.Information
import co.com.test.payments.screens.payments.view.adapter.ExpandableListAdapter
import co.com.test.payments.screens.payments.view.listener.TabLayoutListener
import co.com.test.payments.screens.payments.viewmodel.PaymentsViewModel
import co.com.test.payments.common.viewmodel.ErrorState
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class InstallmentInfoFragment(private val tabLayoutListener: TabLayoutListener): BaseFragment() {

    private lateinit var binding: FragmentInstallmentInfoBinding
    private val viewModel: PaymentsViewModel by sharedViewModel()
    private val groupList: MutableList<String> = mutableListOf()
    private val childrenList : HashMap<String,List<String>> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_installment_info,container,false)
        return binding.root
    }

    override fun handleError(errorState: ErrorState) {
        showAlertMessage(null,errorState.message?:"",null,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        binding.btnNext.setOnClickListener(this::nextClick)
    }

    private fun setObservers() {
        setLoadingObserver(viewModel.loading)
        setErrorObserver(viewModel.error)
        viewModel.information.observe(viewLifecycleOwner, Observer {res ->
            res?.let { information ->
                 if ( information.credits?.isNotEmpty() == true){
                     binding.installmentContainer.visibility = View.VISIBLE
                     createLists(information)
                     setAdapter(information)
                 }
                if ( information.displayInterest == true ) binding.interestContainer.visibility = View.VISIBLE
            }
        })
    }

    private fun setAdapter(information: Information) {
        context?.let {
            binding.expandableListCredits.setAdapter(
                ExpandableListAdapter(
                    it,
                    groupList,
                    childrenList,
                    this
                )
            )
        }

        expandAll()

        val myListItemClicked =
            OnChildClickListener { parent, view, groupPosition, childrenPosition, _ ->
                getInterest(information,groupPosition,childrenPosition)
                parent.setSelectedChild(groupPosition,childrenPosition,true)
                false
            }
        binding.expandableListCredits.setOnChildClickListener(myListItemClicked)
    }

    private fun expandAll() {
        val count: Int = binding.expandableListCredits.expandableListAdapter.groupCount
        for (i in 0 until count) {
            binding.expandableListCredits.expandGroup(i)
        }
    }

    private fun createLists(information: Information) {
        information.credits?.let { list ->
            groupList.clear()
            childrenList.clear()
            var position = 0
            list.map {
                groupList.add(position, it.description?:"")
                childrenList[it.description?:""] = it.installments?: listOf()
                position++
            }
        }
    }

    private fun nextClick(v: View) {
        tabLayoutListener.selectTab(3)
    }

    private fun getInterest(information: Information, groupPosition: Int, childrenPosition: Int){
        information.credits?.get(groupPosition)?.let { credit ->
            credit.installment = childrenList[ groupList[groupPosition]]?.get(childrenPosition)
            viewModel.instrument.value?.credit = credit
            viewModel.instrument.postValue(viewModel.instrument.value)
        }

        if(information.displayInterest == true){
            setErrorObserver(viewModel.error)
            viewModel.getInterests().observe(viewLifecycleOwner, Observer {
                it?.let {
                    val currency = viewModel.payment.value?.amount?.currency?:""

                    var value = "$currency $${it.values?.original.toString()}"
                    binding.txtInitial.text         = value

                    value = "$currency $${it.values?.installment.toString()}"
                    binding.txtInstallment.text     = value

                    value = "$currency $${it.values?.interest.toString()}"
                    binding.txtInterestValue.text   = value

                    value = "$currency $${it.values?.total.toString()}"
                    binding.txtTotal.text           = value

                    binding.interestContainer.visibility = View.VISIBLE
                }
            })
        } else {
            binding.interestContainer.visibility = View.GONE
        }
    }
}