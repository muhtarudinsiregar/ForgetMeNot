package com.example.ardin.forgetmenot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_task_description.*

class TaskDescriptionActivity : AppCompatActivity() {
    companion object {
        val EXTRA_TASK_DESCRIPTION = "task"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_description)
    }

    fun doneClicked(view: View) {
        //get value from descriptionText EditText
        val taskDescription = descriptionText.text.toString()

        //jika terdapat nilai yang kosong, maka kembali lagi ke main activity
        if (taskDescription.isEmpty()) {
            setResult(Activity.RESULT_CANCELED)
        } else {
            val result = Intent()
            result.putExtra(EXTRA_TASK_DESCRIPTION, taskDescription)
            setResult(Activity.RESULT_OK, result)
        }

        //close activity
        finish()
    }
}
