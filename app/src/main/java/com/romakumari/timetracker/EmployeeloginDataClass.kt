package com.romakumari.timetracker

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class EmployeeloginDataClass(
      var id : String="",
      var employeeName:String?="",
      var email:String?="",
      var date:String? = SimpleDateFormat("dd/MM/yyyy hh:mm:a").format(Calendar.getInstance().time),
)
