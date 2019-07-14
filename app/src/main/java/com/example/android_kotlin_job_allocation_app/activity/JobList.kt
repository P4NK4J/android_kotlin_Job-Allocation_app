package com.example.android_kotlin_job_allocation_app.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import com.example.android_kotlin_job_allocation_app.R
import com.example.android_kotlin_job_allocation_app.data.JobDatabaseHandler
import com.example.android_kotlin_job_allocation_app.data.JobListAdapter
import com.example.android_kotlin_job_allocation_app.model.job
import kotlinx.android.synthetic.main.activity_job_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup.view.*

class JobList : AppCompatActivity() {

    private var adapter: JobListAdapter? = null
    private var joblist: ArrayList<job>? = null
    private var jobListItems: ArrayList<job>? = null
    private var dialogBuilder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null

    private var layoutManager: RecyclerView.LayoutManager? = null
    var dbhandler: JobDatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_list)

        dbhandler = JobDatabaseHandler(this)

        layoutManager = LinearLayoutManager(this)
        joblist = ArrayList<job>()
        jobListItems = ArrayList()
        adapter = JobListAdapter(jobListItems!!, this)


        //setup list = recycler view
        recyclerviewId.layoutManager = layoutManager
        recyclerviewId.adapter = adapter

        // load our jobs
        joblist = dbhandler!!.readJobs()
        joblist!!.reverse()

        for (c in joblist!!.iterator()) {

            val job = job()
            job.jobName = c.jobName
            job.assignedBy = "Assigned By:  ${c.assignedBy}"
            job.assignedTo = "Assigned to:  ${c.assignedTo}"
            job.id = c.id
            job.showHumanDate(c.timeAssigned!!)

            jobListItems!!.add(job)
        }

        adapter!!.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.top_menu, menu) //menu object is passed here declared in overriding function
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {  //this function is used to provide and onClickListener to the add menu button

        if (item!!.itemId == R.id.add_menu_button){

            Log.d("chutiyapa", "bht bda")
            createPopupDialog()
        }

        return super.onOptionsItemSelected(item)
    }

    fun createPopupDialog(){  // instantiate dialog builder and dialog

        var view = layoutInflater.inflate(R.layout.popup, null) // this view will have our popup
        var jobName = view.popenterjobId
        var assignedBy = view.popenterallocatorId
        var assignedTo = view.popenteracceptorId
        var savejob = view.popsaveButtonId

        // instantiating dialog builder
        dialogBuilder = AlertDialog.Builder(this).setView(view)
        dialog  = dialogBuilder!!.create()
        dialog?.show()

        savejob.setOnClickListener {
            if (!TextUtils.isEmpty(jobName.text.toString().trim())
                && !TextUtils.isEmpty((assignedBy.text.toString().trim()))
                && !TextUtils.isEmpty(assignedTo.text.toString().trim())){

                var job = job()
                job.jobName = jobName.text.toString().trim()
                job.assignedBy = assignedBy.text.toString().trim()
                job.assignedTo = assignedTo.text.toString().trim()


                dbhandler!!.createJob(job)
                dialog!!.dismiss()

                startActivity(Intent(this, JobList :: class.java))
                finish()

            }
        }


    }
}
