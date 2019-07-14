package com.example.android_kotlin_job_allocation_app.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.android_kotlin_job_allocation_app.model.*
import java.text.DateFormat
import java.util.*

class JobDatabaseHandler(context: Context):
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        // use SQL to create table
        var CREATE_JOB_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"+
                KEY_JOB_NAME + " TEXT," +
                KEY_JOB_ASSIGNED_BY + " TEXT," +
                KEY_JOB_ASSIGNED_TO + " TEXT," +
                KEY_JOB_ASSIGNED_TIME + " LONG" +");"
        db?.execSQL(CREATE_JOB_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        // create a new table after deleting the previous using above function
        onCreate(db)
    }

    /* CRUD = create read update delete */

    fun createJob(job: job) {

        var db: SQLiteDatabase = writableDatabase

        var values: ContentValues = ContentValues()
        values.put(KEY_JOB_NAME, job.jobName)
        values.put(KEY_JOB_ASSIGNED_BY, job.assignedBy)
        values.put(KEY_JOB_ASSIGNED_TO, job.assignedTo)
        values.put(KEY_JOB_ASSIGNED_TIME, System.currentTimeMillis())

        db.insert(TABLE_NAME, null, values)

        Log.d("DEBUG", "SUCCESS")
        db.close()
    }

    fun readAJob(id: Int): job {

        var db: SQLiteDatabase = writableDatabase
        var cursor: Cursor = db.query(
            TABLE_NAME, arrayOf(
                KEY_ID, KEY_JOB_NAME, KEY_JOB_ASSIGNED_BY,
                KEY_JOB_ASSIGNED_TO, KEY_JOB_ASSIGNED_TIME
            ), KEY_ID + "=?", arrayOf(id.toString()),
            null, null, null, null
        )

        if (cursor != null)
            cursor.moveToFirst()

        var job = job()

        job.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
        job.jobName = cursor.getString(cursor.getColumnIndex(KEY_JOB_NAME))
        job.assignedBy = cursor.getString(cursor.getColumnIndex(KEY_JOB_ASSIGNED_BY))
        job.assignedTo = cursor.getString(cursor.getColumnIndex(KEY_JOB_ASSIGNED_TO))
        job.timeAssigned = cursor.getLong(cursor.getColumnIndex(KEY_JOB_ASSIGNED_TIME))

        var dateFormat: java.text.DateFormat = DateFormat.getDateInstance()
        var formatteddate = dateFormat.format(
            Date(
                cursor.getLong
                    (cursor.getColumnIndex(KEY_JOB_ASSIGNED_TIME))
            ).time
        )

        return job

    }

    fun readJobs(): ArrayList<job> {


        var db: SQLiteDatabase = readableDatabase
        var list: ArrayList<job> = ArrayList()

        //Select all chores from table
        var selectAll = "SELECT * FROM " + TABLE_NAME

        var cursor: Cursor = db.rawQuery(selectAll, null)

        //loop through our chores
        if (cursor.moveToFirst()) {
            do {
                var job = job()

                job.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                job.jobName = cursor.getString(cursor.getColumnIndex(KEY_JOB_NAME))
                job.assignedTo = cursor.getString(cursor.getColumnIndex(KEY_JOB_ASSIGNED_TO))
                job.timeAssigned = cursor.getLong(cursor.getColumnIndex(KEY_JOB_ASSIGNED_TIME))
                job.assignedBy = cursor.getString(cursor.getColumnIndex(KEY_JOB_ASSIGNED_BY))

                list.add(job)

            }while (cursor.moveToNext())
        }


        return list

    }

    fun updatejob(job: job): Int {
        var db: SQLiteDatabase = writableDatabase

        var values: ContentValues = ContentValues()
        values.put(KEY_JOB_NAME, job.jobName)
        values.put(KEY_JOB_ASSIGNED_BY, job.assignedBy)
        values.put(KEY_JOB_ASSIGNED_TO, job.assignedTo)
        values.put(KEY_JOB_ASSIGNED_TIME, System.currentTimeMillis())

        //update a row
        return db.update(TABLE_NAME, values, KEY_ID + "=?", arrayOf(job.id.toString()))
    }

    fun deletejob(id: Int) {
        var db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_NAME, KEY_ID + "=?", arrayOf(id.toString()))

        db.close()
    }

    fun getJobsCount(): Int {
        var db: SQLiteDatabase = readableDatabase
        var countQuery = "SELECT * FROM " + TABLE_NAME
        var cursor: Cursor = db.rawQuery(countQuery, null)

        return cursor.count
    }
}