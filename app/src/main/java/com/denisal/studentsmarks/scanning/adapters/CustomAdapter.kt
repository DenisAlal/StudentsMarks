package com.denisal.studentsmarks.scanning.adapters

import com.denisal.studentsmarks.databinding.ChildLayoutBinding
import com.denisal.studentsmarks.databinding.GroupLayoutBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter


class CustomAdapter(
    context: Context,
    val groupList:List<String>,
    val childMap:Map<String,List<String>>
    ):BaseExpandableListAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getGroupCount(): Int {
        return groupList.size
    }

    override fun getChildrenCount(p0: Int): Int {
        val course = groupList[p0]
        return childMap[course]?.size ?: 0
    }

    override fun getGroup(p0: Int): Any { //gruppani nomi
        return groupList[p0]
    }

    override fun getChild(p0: Int, p1: Int): Any {
        val course = groupList[p0]
        return childMap[course]!![p1]
    }

    override fun getGroupId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        return p1.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View {
        val binding:GroupLayoutBinding = if(p2==null){
            GroupLayoutBinding.inflate(inflater,p3,false)
        }else{
            GroupLayoutBinding.bind(p2)
        }
         binding.groupTv.text = groupList[p0]
        return binding.root
    }

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        val course = groupList[p0]
        val lesson = childMap[course]?.get(p1)

        val binding: ChildLayoutBinding = if (p3==null){
            ChildLayoutBinding.inflate(inflater,p4,false)
        }else{
            ChildLayoutBinding.bind(p3)
        }
        binding.childTv.text = lesson
        return binding.root
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }
}