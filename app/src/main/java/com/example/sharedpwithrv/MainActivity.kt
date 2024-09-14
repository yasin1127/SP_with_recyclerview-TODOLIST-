package com.example.sharedpwithrv

import Player
import TaskAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var tasklist: MutableList<Player>
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskadapter: TaskAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editTaskEditTask: EditText
    private lateinit var aet:EditText
    private lateinit var set:EditText
    private lateinit var ret:EditText


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)

        recyclerView = findViewById(R.id.Rv)
        editTaskEditTask = findViewById(R.id.editText)
        aet = findViewById(R.id.ageeT)
        set = findViewById(R.id.statusT)
        ret = findViewById(R.id.ratingT)
        tasklist = retrieveTask()



        val saveButton: Button = findViewById(R.id.btn)

        saveButton.setOnClickListener {
            val NameText = editTaskEditTask.text.toString()
            val AgeText = aet.text.toString().toInt()
            val StatusText =set.text.toString()
            val RatingText=ret.text.toString().toInt()
            if(NameText.isNotEmpty()){
               val task =Player(NameText,AgeText,StatusText,RatingText,true)
                tasklist.add(task)
                saveTask(tasklist)
                taskadapter.notifyItemInserted(tasklist.size-1)
                editTaskEditTask.text.clear()
                aet.text.clear()
                set.text.clear()
                ret.text.clear()

            }
            else{
                Toast.makeText(this,"Player Field  can't be empty",Toast.LENGTH_SHORT).show()
            }
        }
        taskadapter= TaskAdapter(tasklist,object : TaskAdapter.TaskClickLister{
            override fun onEditClick(position: Int) {
            editTaskEditTask.setText(tasklist[position].Name)
                aet.setText(tasklist[position].Age)
                set.setText(tasklist[position].Status)
                ret.setText(tasklist[position].Rating)
                tasklist.removeAt(position)
                taskadapter.notifyDataSetChanged()
            }

            override fun onDeleteClick(position: Int) {
            val alertDialog =AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle("Delete Task")
                alertDialog.setMessage("Are you sure you want to delete this task?")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                  deleteTask(position)
                }
                alertDialog.setNegativeButton("No") { _, _ ->  }
                alertDialog.show()
            }

        })
        recyclerView.adapter= taskadapter
        recyclerView.layoutManager= LinearLayoutManager(this)
    }

    private fun saveTask(tasklist: MutableList<Player>) {
    val editor = sharedPreferences.edit()
        val taskset = HashSet<String>()

        tasklist.forEach{ taskset.add(it.Name)}
        editor.putStringSet("Name", taskset)
        editor.apply()
        tasklist.forEach{taskset.add(it.Age.toString())}
        editor.putStringSet("Age", taskset)
        editor.apply()
        tasklist.forEach{taskset.add(it.Status)}
        editor.putStringSet("Status", taskset)
        editor.apply()
        tasklist.forEach{taskset.add(it.Rating.toString())}
        editor.putStringSet("Rating", taskset)
        editor.apply()

    }

    private fun deleteTask(position: Int) {
    tasklist.removeAt(position)
        taskadapter.notifyItemRemoved(position)
        saveTask(tasklist)
    }

    private fun retrieveTask(): MutableList<Player> {
    val tasks = sharedPreferences.getStringSet("tasks", HashSet())?:HashSet()
        return tasks.map{Player(it,0,it ,0 ,false)}.toMutableList()
    }

}