package com.madassignment.okuable.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.madassignment.okuable.R
import com.madassignment.okuable.databinding.ActivityReasonBinding
import kotlinx.android.synthetic.main.activity_reason.*


class Reason : AppCompatActivity() {

    private lateinit var binding: ActivityReasonBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reason)
        val db = FirebaseFirestore.getInstance()
        //val reasonTxt = reason_et.text.toString()

        val intent = intent
        val eventName: String = intent?.getStringExtra("EventName").toString()
        var btnAdd = binding.addBtn


        binding.cgBtnBack.setOnClickListener {
            finish()
        }

        btnAdd.setOnClickListener {


            val reasonTxt = reason_job.text.toString()
            val data: MutableMap<String, Any> = HashMap()
            data["reason"] = reasonTxt
            data["status"] = "reject"

            db.collection("Event")
                .document(eventName)
                .update(data)

                .addOnSuccessListener {
                    Toast.makeText(this, "Reject reason added successfully", Toast.LENGTH_LONG ).show()
                    finish()
                    val intent = Intent(this, MainActivity3::class.java)
                    //intent.putExtra("EventName", eventName)
                    this.startActivity(intent)
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Reject reason Failed to add ", Toast.LENGTH_LONG ).show()
                }

        }


    }


}