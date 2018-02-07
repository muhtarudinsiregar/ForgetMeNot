package com.example.ardin.forgetmenot

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class TaskDescriptionActivity : AppCompatActivity() {
    companion object {
        val EXTRA_TASK_DESCRIPTION = "task"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_description)
    }

    fun doneClicked(view: View) {}
}
