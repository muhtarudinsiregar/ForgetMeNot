package com.example.ardin.forgetmenot

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val taskList: MutableList<String> = mutableListOf()
    private val adapter by lazy { makeAdapter(taskList) }
    private val ADD_TASK_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        taskListView.adapter = adapter

        taskListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> }
    }


    fun addTaskClicked(view: View) {
        val intent = Intent(this, TaskDescriptionActivity::class.java)
        startActivityForResult(intent, ADD_TASK_REQUEST)
    }

    private fun makeAdapter(list: List<String>): ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
}
