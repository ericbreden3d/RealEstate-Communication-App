package com.example.kw_prototype

import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

// Static factory for retrieving formatted current date and time
class DateMachine {
    companion object {
        fun getDate() : String {
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
            var fDate = formatter.format(date)
            fDate = fDate.removeRange(fDate.length-6, fDate.length-2)
            fDate = StringBuilder(fDate).insert(fDate.length-2, ' ').toString()
            return fDate
        }
    }
}