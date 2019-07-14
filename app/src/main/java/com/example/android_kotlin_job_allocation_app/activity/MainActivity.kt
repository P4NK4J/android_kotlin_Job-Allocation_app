package com.example.android_kotlin_job_allocation_app.activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.example.android_kotlin_job_allocation_app.R
import com.example.android_kotlin_job_allocation_app.data.JobDatabaseHandler
import com.example.android_kotlin_job_allocation_app.data.JobListAdapter
import com.example.android_kotlin_job_allocation_app.model.job
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var dbhandler: JobDatabaseHandler? = null
    var progressBar: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = ProgressDialog(this)

        dbhandler = JobDatabaseHandler(this)

        checkDB()



       saveButton.setOnClickListener {
           progressBar!!.setMessage("saving...")
           progressBar!!.show()

            if (!TextUtils.isEmpty(jobId.text.toString()) && !TextUtils.isEmpty(allocatorId.text.toString()) &&
                !TextUtils.isEmpty(acceptorId.text.toString())){

                // save to database

                var job: job =  job()
                job.jobName = jobId.text.toString()
                job.assignedTo = acceptorId.text.toString()
                job.assignedBy = allocatorId.text.toString()

                saveTODB(job)
                progressBar!!.cancel()
                startActivity(Intent(this, JobList:: class.java))



            }
            else {
                Toast.makeText(this, "Please enter the information", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun saveTODB(job: job){
        dbhandler!!.createJob(job)
    }

    fun checkDB(){ //checking if database has some data then it will redirect user to second job list page
        if (dbhandler!!.getJobsCount() > 0)
            startActivity(Intent(this, JobList :: class.java))

    }
}
