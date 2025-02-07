package com.romakumari.timetracker

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class EmployeeDataClass(var id : String="",
     var employeeName:String?="" ,
    var markIn: Boolean= false,
    var markOut: Boolean ?= false,
    var shortLeave :Boolean?=false,
     var remarks: String?="",
      var email:String?="" ,

     var date:String? = SimpleDateFormat("dd/MM/yyyy hh:mm:a").format(Calendar.getInstance().time),
//     val time : String= SimpleDateFormat("HH:mm a", Locale.getDefault()).format(java.util.Timer()),
     var location : String?=""
)
