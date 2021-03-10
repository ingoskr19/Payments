package co.com.test.payments.screens.payments.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import co.com.test.payments.R
import co.com.test.payments.common.Constants
import co.com.test.payments.databinding.ItemPaymentBinding
import co.com.test.payments.screens.payments.model.TransactionEntity
import co.com.test.payments.screens.payments.view.ListPaymentsFragmentDirections
import co.com.test.payments.screens.payments.view.listener.PaymentListener
import kotlinx.android.synthetic.main.item_payment.view.*

/**
 * Created By oscar.vergara on 5/03/2021
 */
class PaymentsAdapter :
    RecyclerView.Adapter<PaymentsAdapter.PaymentViewHolder>(), PaymentListener {

    val payments = ArrayList<TransactionEntity>()
    lateinit var binding: ItemPaymentBinding

    fun updateList(list: List<TransactionEntity>) {
        payments.clear()
        payments.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): PaymentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater,R.layout.item_payment,parent,false)
        return PaymentViewHolder(binding)
    }

    override fun getItemCount() = payments.size

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.view.payment = payments[position]
        holder.view.listener = this
    }

    class PaymentViewHolder(var view: ItemPaymentBinding) : RecyclerView.ViewHolder(view.root)

    override fun onItemClicked(v: View) {
        val action = ListPaymentsFragmentDirections.actionListToToDetail()
        val uuid = v.paymentId.text.toString()
        action.paymentUuid = uuid.toInt()
        Navigation.findNavController(v).navigate(action)
    }
}