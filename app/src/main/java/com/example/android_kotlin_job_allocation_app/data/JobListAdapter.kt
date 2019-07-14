package com.example.android_kotlin_job_allocation_app.data

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.android_kotlin_job_allocation_app.R
import com.example.android_kotlin_job_allocation_app.model.job
import kotlinx.android.synthetic.main.popup.view.*

class JobListAdapter(private val list: ArrayList<job>,
                              private val context: Context): RecyclerView.Adapter<JobListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {

        //create a view from out xml file (list_row file)

        val view = LayoutInflater.from(context)
               .inflate(R.layout.list_row, parent, false)

        return ViewHolder(view, context, list)

    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){

        holder.bindView(list[position])      //binding the views with certain position of job in above created list of jobs

    }
        // class made inner to invoke certain functions
    inner class ViewHolder(itemView: View, context: Context, list: ArrayList<job>): RecyclerView.ViewHolder(itemView) , View.OnClickListener {

        var mContext = context
        var mList = list

        var jobName = itemView.findViewById(R.id.ListJobName) as TextView
        var assignedBy = itemView.findViewById(R.id.ListAssignedBy) as TextView
        var assignedDate = itemView.findViewById(R.id.ListDate)    as TextView
        var assignedTo = itemView.findViewById(R.id.ListAssignedTo) as TextView
        var deleteButton = itemView.findViewById(R.id.DeleteButton) as Button
        var editButton = itemView.findViewById(R.id.EditButton) as Button

        // this class is used to create out view in OnCreateViewHolder function
        // now we have to transform those views in objects in this class


        fun bindView(job: job){

            jobName.text = job.jobName
            assignedBy.text = job.assignedBy
            assignedTo.text = job.assignedTo
            assignedDate.text = job.showHumanDate(System.currentTimeMillis())
            deleteButton.setOnClickListener(this)
            editButton.setOnClickListener(this) //registering the buttons for click small brackets are used bcz in function below
                                                // is not being implemented bcz the curly brackets are calling the function into its context
        }

        override fun onClick(v: View?) {

            var mPosition: Int = adapterPosition
            var  job = mList[mPosition]


            when(v!!.id) {
                deleteButton.id -> {
                    deleteJob(job.id!!)
                    mList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)

                }

                editButton.id -> {

                    editJob(job)

                }
            }

        }

        fun deleteJob(id: Int) {

            var db: JobDatabaseHandler = JobDatabaseHandler(mContext)
            db.deletejob(id)

        }

        fun editJob(job: job) {

            var dialogBuilder: AlertDialog.Builder?
            var dialog: AlertDialog?
            var dbHandler: JobDatabaseHandler = JobDatabaseHandler(context)

            var view = LayoutInflater.from(context).inflate(R.layout.popup, null)
            var JobName = view.popenterjobId
            var assignedBy = view.popenterallocatorId
            var assignedTo = view.popenteracceptorId
            var saveButton = view.popsaveButtonId

            dialogBuilder = AlertDialog.Builder(context).setView(view)
            dialog = dialogBuilder!!.create()
            dialog?.show()

            saveButton.setOnClickListener {
                var name = JobName.text.toString().trim()
                var aBy =  assignedBy.text.toString().trim()
                var aTo = assignedTo.text.toString().trim()

                if (!TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(aBy)
                    && !TextUtils.isEmpty(aTo)) {
                    // var chore = Chore()

                    job.jobName = name
                    job.assignedTo = aTo
                    job.assignedBy = aBy

                    dbHandler!!.updatejob(job)
                    notifyItemChanged(adapterPosition, job)


                    dialog!!.dismiss()


                }
        }
    }
 }
}


