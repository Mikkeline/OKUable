package com.madassignment.okuable.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.madassignment.okuable.R
import kotlinx.android.synthetic.main.activity_reason.*

class CaregiverReason : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_reason)

        var addReason: Button = findViewById(R.id.addBtn)
        var tvReason: EditText = findViewById(R.id.job_reason)
        var btnBack : ImageButton = findViewById(R.id.cg_btnBack)

        val inputJobTitle: String = intent?.getStringExtra("jobtitle").toString()

        val uid: String = intent?.getStringExtra("uid").toString()
        mDatabase = FirebaseDatabase.getInstance().getReference("Caregiver")
        val database = Firebase.database
        val myRef = database.reference.child("Caregiver").child(uid)

        btnBack.setOnClickListener{
            finish()
        }



        //var btnAdd = binding.addBtn
        addReason.setOnClickListener {

            mDatabase.child(uid).child("reason").setValue(tvReason.text.toString())
            mDatabase.child(uid).child("status").setValue("reject")

                .addOnSuccessListener {
                    Toast.makeText(this, "Reason added successfully", Toast.LENGTH_LONG ).show()
                    finish()
                    val intent = Intent(this, MainActivity3::class.java)
                    this.startActivity(intent)

                }
                .addOnFailureListener{
                    Toast.makeText(this, "Reason Failed to add ", Toast.LENGTH_LONG ).show()
                }


        }

    }


}