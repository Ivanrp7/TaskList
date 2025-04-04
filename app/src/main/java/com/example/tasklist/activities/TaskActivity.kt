package com.example.tasklist.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tasklist.R
import com.example.tasklist.activities.TaskListActivity.Companion
import com.example.tasklist.data.Category
import com.example.tasklist.data.CategoryDAO
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    companion object {
        const val CATEGORY_ID = "CATEGORY_ID"
        const val TASK_ID = "TASK_ID"
    }

    lateinit var binding: ActivityTaskBinding

    lateinit var taskDAO: TaskDAO
    lateinit var task: Task
    lateinit var categoryDAO: CategoryDAO
    lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getLongExtra(TASK_ID, -1L)
        val categoryId = intent.getLongExtra(CATEGORY_ID, -1L)

        taskDAO = TaskDAO(this)
        categoryDAO = CategoryDAO(this)

        category = categoryDAO.findById(categoryId)!!

        if (id != -1L) {
            task = taskDAO.findById(id)!!
            binding.titleEditText.setText(task.title)
            supportActionBar?.title = "Editar tarea"
        } else {
            task = Task(-1L, "", false, category)
            supportActionBar?.title = "Crear tarea"
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()

            task.title = title

            if (task.id != -1L) {
                taskDAO.update(task)
            } else {
                taskDAO.insert(task)
            }

            finish()
        }
    }
}