package com.example.ardin.forgetmenot

import android.annotation.TargetApi
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val taskList: MutableList<String> = mutableListOf()
    private val adapter by lazy { makeAdapter(taskList) }
    private val ADD_TASK_REQUEST = 1
    private val PREFS_TASKS = "prefs_tasks"
    private val KEY_TASK_LIST = "tasks_list"

    private val tickReceiver by lazy { makeBroadcastReceiver() }

    //that sets the date and time on the screen if it receives a time change broadcast from the system.
    private fun makeBroadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_TIME_TICK) {
                    dateTimeTextView.text = getCurrentTimestamps()
                }
            }

        }
    }

    companion object {
        private const val LOG_TAG = "MainActivityLog"

        @TargetApi(Build.VERSION_CODES.N)
        private fun getCurrentTimestamps(): String {
            val simpleDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            val now = Date()
            return simpleDate.format(now)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        taskListView.adapter = adapter

        taskListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> }

        val savedList = getSharedPreferences(PREFS_TASKS, Context.MODE_PRIVATE).getString(KEY_TASK_LIST, null)
        if (savedList != null) {
            val items = savedList.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            taskList.addAll(items)
        }
    }

    override fun onResume() {
        super.onResume()

        //update timestampt for textView
        dateTimeTextView.text = getCurrentTimestamps()

        //register broadcast reciever every minute
        registerReceiver(tickReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    override fun onPause() {
        super.onPause()

        try {
            //unregister receiver when on pause, agar tidak overhead system
            unregisterReceiver(tickReceiver)
        } catch (e: IllegalArgumentException) {
            Log.e(MainActivity.LOG_TAG, "time tick reciever not registered", e)
        }
    }

    override fun onStop() {
        super.onStop()

        val savedList = StringBuilder()
        for (task in taskList) {
            savedList.append(task)
            savedList.append(",")
        }

        getSharedPreferences(PREFS_TASKS, Context.MODE_PRIVATE).edit()
                .putString(KEY_TASK_LIST, savedList.toString()).apply()
    }

    fun addTaskClicked(view: View) {
        val intent = Intent(this, TaskDescriptionActivity::class.java)
        startActivityForResult(intent, ADD_TASK_REQUEST)
    }

    private fun makeAdapter(list: List<String>): ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //cek if activity
        if (requestCode == ADD_TASK_REQUEST) {
            //cek if activity is success
            if (resultCode == Activity.RESULT_OK) {
                //extract informasi dari intent
                val task = data?.getStringExtra(TaskDescriptionActivity.EXTRA_TASK_DESCRIPTION)
                task?.let {
                    taskList.add(task)
                    //trigger to refresh the view
                    adapter.notifyDataSetChanged()
                }

            }
        }

    }
}
