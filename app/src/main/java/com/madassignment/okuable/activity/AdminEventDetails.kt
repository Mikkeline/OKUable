package com.madassignment.okuable.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.madassignment.okuable.R
import com.madassignment.okuable.data.Event
import com.madassignment.okuable.databinding.ActivityAdminEventDetailsBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_reason.*


class AdminEventDetails : AppCompatActivity() {

    private lateinit var binding: ActivityAdminEventDetailsBinding
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_event_details)




        binding.btnBack.setOnClickListener {
            finish()
        }




        val intent = intent
        val eventName: String = intent?.getStringExtra("EventName").toString()


        binding.eventNameTv.text = eventName


        binding.btnReject.setOnClickListener{
            val intent = Intent(this, Reason::class.java)
            intent.putExtra("EventName", eventName)
            this.startActivity(intent)
        }

        binding.btnApprove.setOnClickListener{

            dialogYesOrNo(
                this,
                "Are you sure you want to approve this event record?",
                "Yes to Approve event, \nNo to cancel this action. ",
                DialogInterface.OnClickListener { dialog, id ->
                    val data: MutableMap<String, Any> = HashMap()
                    data["status"] = "approved"
                    db.collection("Event")
                        .document(eventName)
                        .update(data)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Event is approved", Toast.LENGTH_LONG ).show()
                            finish()
                            val intent = Intent(this, MainActivity3::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener{
                            Toast.makeText(this, "Event Failed to add ", Toast.LENGTH_LONG ).show()
                        }
                }
            )

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


                        Glide.with(this@AdminEventDetails)
                            .load(image)
                            .into(binding.eventPic)


                    } else {

                        Toast.makeText(this@AdminEventDetails, "Failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
            )


    }

    fun dialogYesOrNo(
        activity: Activity,
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(activity)
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
            listener.onClick(dialog, id)
        })
        builder.setNegativeButton("No", null)
        val alert = builder.create()
        alert.setTitle(title)
        alert.setMessage(message)
        alert.show()
    }
}