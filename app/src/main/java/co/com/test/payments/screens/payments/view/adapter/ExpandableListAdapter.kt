package co.com.test.payments.screens.payments.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import co.com.test.payments.R
import co.com.test.payments.databinding.GroupItemViewBinding
import co.com.test.payments.databinding.GroupViewBinding

/**
 * Created By oscar.vergara on 7/03/2021
 */
class ExpandableListAdapter(private val context: Context, private val groupList:List<String>, private val childrenList:HashMap<String,List<String>>, fragment: Fragment) : BaseExpandableListAdapter() {

    lateinit var groupBinding : GroupViewBinding
    lateinit var viewBinding : GroupItemViewBinding
    var hasCollapsed = false;

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val inflater = LayoutInflater.from(context)
        groupBinding = DataBindingUtil.inflate(inflater,R.layout.group_view,parent,false)
        groupBinding.groupName.text = groupList[groupPosition]
        return groupBinding.root
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val inflater = LayoutInflater.from(parent?.context)
        viewBinding = DataBindingUtil.inflate(inflater,R.layout.group_item_view,parent,false)

        val installment = childrenList[groupList[groupPosition]]?.get(childPosition)
        val childText = " ( $installment ) "

        viewBinding.installment.text = childText
        viewBinding.groupName.text = groupList[groupPosition]
        return viewBinding.root
    }

    override fun onGroupExpanded(groupPosition: Int) {
        super.onGroupExpanded(groupPosition)
        if(hasCollapsed) {
            groupBinding.arrow.setBackgroundResource(R.drawable.ic_arrow_drop_up)
        }
    }

    override fun onGroupCollapsed(groupPosition: Int) {
        groupBinding.arrow.setBackgroundResource(R.drawable.ic_arrow_drop_down)
        hasCollapsed = true
    }

    override fun getChildrenCount(groupPosition: Int): Int = childrenList[groupList[groupPosition]]?.size?:0

    override fun getChild(groupPosition: Int, childPosition: Int): String = childrenList[groupList[groupPosition]]?.get(childPosition)?:""

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getGroup(groupPosition: Int): String = groupList[groupPosition]

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    override fun hasStableIds(): Boolean = true

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getGroupCount(): Int = groupList.size


}