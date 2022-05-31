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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.madassignment.okuable.R
import com.madassignment.okuable.data.Caregiver
import com.madassignment.okuable.databinding.ActivityAdminCaregiverDetailsBinding


class AdminCaregiverDetails : AppCompatActivity() {
private lateinit var binding: ActivityAdminCaregiverDetailsBinding
private lateinit var mDatabase: DatabaseReference
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_caregiver_details)
    val intent = intent
    val inputName: String = intent?.getStringExtra("name").toString()
    val uid: String = intent?.getStringExtra("uid").toString()
    val inputJobTitle: String = intent?.getStringExtra("jobtitle").toString()

    val db = FirebaseFirestore.getInstance()


    //mDatabase = FirebaseDatabase.getInstance().getReference("Users")

    binding.cgFullName.text = inputName
    binding.cgJobtitle.text = inputJobTitle

    val database = Firebase.database
    val myRef = database.reference.child("Caregiver").child(uid)
    mDatabase = FirebaseDatabase.getInstance().getReference("Caregiver")

    //retrieve data from firebase
    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                val caregivers = snapshot.getValue(Caregiver::class.java)

                val image = snapshot.child("image").value

                binding.cgSkills.text = caregivers!!.skills
                binding.cgExperience.text = caregivers!!.exp +" Year(s)"
                binding.cgLocation.text = caregivers!!.location
                binding.cgService.text = caregivers!!.service
                binding.cgTimeMin.text = caregivers!!.mintime
                binding.cgTimeMax.text = caregivers!!.maxTime
                binding.cgPrice.text = caregivers!!.pricerate
                binding.cgQ1.text = caregivers!!.q1
                binding.cgQ2.text = caregivers!!.q2
                binding.cgQ3.text = caregivers!!.q3
                binding.cgDesc.text = caregivers!!.desc

                Glide.with(this@AdminCaregiverDetails)
                    .load(image)
                    .into(binding.cgImage)


            }else{
                Toast.makeText(this@AdminCaregiverDetails, "Error in retrieving details, Please contact Admin. ", Toast.LENGTH_LONG).show()
            }
        }

        override fun onCancelled(error: DatabaseError) {

        }
    })

    binding.btnReject.setOnClickListener{
        val intent2 = Intent(this, CaregiverReason::class.java)
        //intent.putExtra("name", inputName)
        intent2.putExtra("uid", uid)
        intent2.putExtra("jobtitle", inputJobTitle)
        startActivity(intent2)
    }

    binding.btnApprove.setOnClickListener{

        dialogYesOrNo(
            this,
            "Are you sure you want to approve this jobs record?",
            "Yes to Approve record, \nNo to cancel this action. ",
            DialogInterface.OnClickListener { dialog, id ->
                mDatabase.child(uid).child("status").setValue("approve")
                    .addOnSuccessListener {
                        Toast.makeText(this, "Approved", Toast.LENGTH_LONG ).show()
                        finish()
                        val intent = Intent(this, MainActivity3::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "Fail, please try again", Toast.LENGTH_LONG ).show()
                    }
            }
        )

    }

    binding.btnBack.setOnClickListener {
        finish()
    }
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