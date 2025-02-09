package com.romakumari.timetracker

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class EmployeeDataClass(var id : String="", var employeeName:String?= null ,
    var markIn: Boolean= false,
    var markOut: Boolean ?= false,
    var shortLeave :Boolean?=false,
    var remarks: String?="",
    var email:String?="" ,
     var datein: String? = null ,
     var dateout:String? =null,
     var dateshortleave:String?=null,
     var location : String?=""
)
