package com.romakumari.timetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.romakumari.timetracker.databinding.ItemadminlayoutBinding

class AdminRecyclerAdapter( val employeeList: List<EmployeeDataClass>, var adminIterface: AdminIterface

) :
    RecyclerView.Adapter<AdminRecyclerAdapter.ViewHolder>() {

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var tvempname=view.findViewById<TextView>(R.id.employeename)
        var tvoutdate=view.findViewById<TextView>(R.id.tvoutdate)
        var  tvindate =view.findViewById<TextView>(R.id.tvindate)
//        var btnremark=view.findViewById<ImageView>(R.id.btnremarkadd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): AdminRecyclerAdapter.ViewHolder {
        var view=LayoutInflater.from(parent.context)
            .inflate(R.layout.itemadminlayout,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: AdminRecyclerAdapter.ViewHolder, position: Int) {
        holder.tvempname.setText(employeeList[position].employeeName)
//         holder.tvindate.setText(employeeList[position].markIn)
//         holder.tvoutdate.setText(employeeList[position].markOut)
          adminIterface.eyeclick(employeeList[position],position)
    }
    override fun getItemCount(): Int {
        return employeeList.size
    }

}