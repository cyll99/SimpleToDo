package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class MainActivity : AppCompatActivity() {

    var listOfTask = mutableListOf<String>()
    lateinit var  adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //remove the item from the list
                listOfTask.removeAt(position)

                //notify the adapter that the data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }


        loadItems()

        //look up recyclerview in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTask, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        ///set up the button and input field so that the user can enter a task

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // get a reference to the button and set on click listener

        findViewById<Button>(R.id.button).setOnClickListener{
            //Grab the text the user has inputted into @id/addTextField

            val userInputedTask = inputTextField.text.toString()

            // Add the string to our list of task
            listOfTask.add(userInputedTask)

            //Notify the adaptapter that our data has been updated
            adapter.notifyItemInserted(listOfTask.size - 1)

            //Reset the text field
            inputTextField.setText("")

            saveItems()
        }

    }

    // save the data the user has inputted by reading and writing from a file

    // create a mehtode to get the data file needed
    fun getDataFile(): File{
    // every line will represent an item
        return File(filesDir, "data.txt")
    }

    // load the item by reading every line in the data file

    fun loadItems(){
        try {
            listOfTask = FileUtils.readLines(getDataFile(), Charset.defaultCharset())

        }catch (ioException : IOException){
            ioException.printStackTrace()
        }
    }

    // save items by writing them into the data file

    fun saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), listOfTask)

        }catch (ioException : IOException){
            ioException.printStackTrace()
        }
    }
}