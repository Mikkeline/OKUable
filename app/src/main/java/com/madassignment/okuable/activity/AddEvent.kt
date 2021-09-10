package com.madassignment.okuable.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.madassignment.okuable.activity.MainActivity
import com.madassignment.okuable.R
import com.madassignment.okuable.data.Event
import com.madassignment.okuable.databinding.ActivityAddEventBinding
import java.util.*
import kotlin.collections.HashMap

class AddEvent : AppCompatActivity() {
    private var c: Calendar? = null
    private var dpd: DatePickerDialog? = null

    private lateinit var binding: ActivityAddEventBinding
    private lateinit var imageUrl : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_event)

        var btnBack  = binding.cgBtnBack
        btnBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val eventName: EditText = findViewById(R.id.eventName_et)
        val eventLocation: EditText = findViewById(R.id.location_et)
        val done: Button = findViewById(R.id.doneBtn)
        val startDate: TextView = findViewById(R.id.startDate_tv)
        val endDate: TextView = findViewById(R.id.endDate_tv)
        val description: TextView = findViewById(R.id.description_et)
        val link: TextView = findViewById(R.id.link_et)
      //  val imageLink: TextView = findViewById(R.id.imageLink_tv)



        startDate.setOnClickListener(View.OnClickListener {

            c = Calendar.getInstance()
            val day = c?.get(Calendar.DAY_OF_MONTH)
            val month = c?.get(Calendar.MONTH)
            val year = c?.get(Calendar.YEAR)
            dpd = day?.let { it1 ->
                month?.let { it2 ->
                    year?.let { it3 ->
                        DatePickerDialog(this@AddEvent,
                            { view, mYear, mMonth, mDay -> startDate.setText(mDay.toString() + "/" + (mMonth + 1) + "/" + mYear) },
                            it1,
                            it2,
                            it3
                        )
                    }
                }
            }
            dpd?.show()
        })



        endDate.setOnClickListener(View.OnClickListener {

            c = Calendar.getInstance()
            val day = c?.get(Calendar.DAY_OF_MONTH)
            val month = c?.get(Calendar.MONTH)
            val year = c?.get(Calendar.YEAR)
            dpd = day?.let { it1 ->
                month?.let { it2 ->
                    year?.let { it3 ->
                        DatePickerDialog(this@AddEvent,
                            { view, mYear, mMonth, mDay -> endDate.setText(mDay.toString() + "/" + (mMonth + 1) + "/" + mYear) },
                            it1,
                            it2,
                            it3
                        )
                    }
                }
            }
            dpd?.show()
        })


        binding.cgAddImage.setOnClickListener {
            AddImage()
        }

        done.setOnClickListener {
            val name = eventName.text.toString()
            val location = eventLocation.text.toString()
            val startDate = startDate.text.toString()
            val endDate = endDate.text.toString()
            val description = description.text.toString()
            val link = link.text.toString()
            // val imageLink = imageLink.text.toString()

            if (!name.isEmpty() && !location.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty() &&
                !description.isEmpty() && !link.isEmpty() ){


            val refStorage = FirebaseStorage.getInstance().reference.child("Event/$name")

            refStorage.putFile(imageUrl).addOnSuccessListener(
                OnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener {
                        val dlUrl = it.toString()
                        //   imageLink.setText(dlUrl)
                        // val imageLink = imageLink.text.toString()

                        // Toast.makeText(this,"Event Added Successfully.. Waiting for approval from admin..",Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                        val event =
                            Event(name, location, startDate, endDate, description, link, dlUrl)
                        saveFireStore(name, location, startDate, endDate, description, link, dlUrl)

                    }
                })


        }

            if(name.isEmpty()){
                Toast.makeText(this, "Please enter your Event's Name!", Toast.LENGTH_LONG).show()
            }

            if(location.isEmpty()){
                Toast.makeText(this, "Please enter your Event's Location!", Toast.LENGTH_LONG).show()
            }

            if(description.isEmpty()){
                Toast.makeText(this, "Please enter your Event's Description!", Toast.LENGTH_LONG).show()
            }

            if(startDate.isEmpty()){
                Toast.makeText(this, "Please enter your Event's Start Date!", Toast.LENGTH_LONG).show()
            }

            if(endDate.isEmpty()){
                Toast.makeText(this, "Please enter your Event's End Date!", Toast.LENGTH_LONG).show()
            }

            if(link.isEmpty()){
                Toast.makeText(this, "Please enter your Event's Link!", Toast.LENGTH_LONG).show()
            }



        }

    }

    private fun AddImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUrl = data.data!!
            with(binding) { cgAddImage.setImageURI(imageUrl) }
        }

    }

    fun saveFireStore(name: String, location: String, startDate: String, endDate: String, description: String, link: String, dlUrl: String) {
        val db = FirebaseFirestore.getInstance()
        val eventName: EditText = findViewById(R.id.eventName_et)

        val user: MutableMap<String, Any> = HashMap()
        user["name"] = name
        user["startDate"] = startDate
        user["endDate"] = endDate
        user["location"] = location
        user["description"] = description
        user["link"] = link
        user["dlUrl"] = dlUrl
        user["status"] = "pending"

        db.collection("Event").document(name)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Event added successfully... Waiting for approval from admin ", Toast.LENGTH_LONG ).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Event Failed to add ", Toast.LENGTH_LONG ).show()
            }
        //   readFireStoreData()
    }

    /*fun readFireStoreData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .get()
            .addOnCompleteListener {

                val result: StringBuffer = StringBuffer()

                if(it.isSuccessful) {
                    for(document in it.result!!) {
                        result.append(document.data.getValue("firstName")).append(" ")
                            .append(document.data.getValue("lastName")).append("\n\n")
                    }
                    textViewResult.setText(result)
                }
            }

    }*/
}