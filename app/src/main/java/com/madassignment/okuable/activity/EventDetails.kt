package com.madassignment.okuable.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.auth.User

import com.madassignment.okuable.R
import com.madassignment.okuable.data.Caregiver
import com.madassignment.okuable.data.Event
import com.madassignment.okuable.databinding.ActivityEventDetailsBinding
import org.w3c.dom.Document


class EventDetails : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailsBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_details)




        binding.btnBack.setOnClickListener {
            finish()
        }




        val intent = intent
        val eventName: String = intent?.getStringExtra("EventName").toString()


        binding.eventNameTv.text = eventName

        binding.commentTv.setOnClickListener{
            val intent = Intent(this, Comments::class.java)
            intent.putExtra("EventName", eventName)
            this.startActivity(intent)
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("Event").document(eventName)

            .addSnapshotListener(object : EventListener<DocumentSnapshot> {
                override fun onEvent(snapshot: DocumentSnapshot?, exception: FirebaseFirestoreException?) {
                    if (snapshot!!.exists()) {
                        val events = snapshot.toObject(Event::class.java)
                        val image = snapshot.get("dlUrl").toString()


                        binding.startDateTv.text = events!!.startDate
                        binding.endDateTv.text = events!!.endDate
                        binding.LocationTv.text = events!!.location
                        binding.descriptionTv.text = events!!.description
                        binding.linkTv.text = events!!.link


                        Glide.with(this@EventDetails)
                            .load(image)
                            .into(binding.eventPic)


                    } else {

                        Toast.makeText(this@EventDetails, "Failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
            )
    }
}












