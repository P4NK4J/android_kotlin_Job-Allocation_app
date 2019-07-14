package com.example.android_kotlin_job_allocation_app.model

import java.text.DateFormat
import java.util.*

class job() {

    var jobName: String? = null
    var assignedBy: String? = null
    var assignedTo: String? = null
    var timeAssigned: Long? = null
    var id: Int? = null


    constructor(jobName: String, assignedBy: String, assignedTo: String,
                timeAssigned: Long, id: Int): this() {
        
        this.jobName = jobName
        this.assignedBy = assignedBy
        this.assignedTo = assignedTo
        this.timeAssigned = timeAssigned
        this.id = id


    }
    fun showHumanDate(timeAssigned: Long): String {

        var dateFormat: java.text.DateFormat = DateFormat.getDateInstance()
        var formattedDate: String = dateFormat.format(Date(timeAssigned).time)

        return "created on  ${formattedDate}"
    }




    }