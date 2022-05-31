package com.madassignment.okuable.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.madassignment.okuable.R
import com.madassignment.okuable.data.Caregiver
import com.madassignment.okuable.databinding.ActivityCaregiverDetailsBinding
import java.util.*


class CaregiverDetails : AppCompatActivity() {

    private lateinit var binding: ActivityCaregiverDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_caregiver_details)

        val intent = intent
        val inputName: String = intent?.getStringExtra("name").toString()
        val uid: String = intent?.getStringExtra("uid").toString()
        val inputJobTitle: String = intent?.getStringExtra("jobtitle").toString()

        binding.cgFullName.text = inputName
        binding.cgJobtitle.text = inputJobTitle

        val database = Firebase.database
        val myRef = database.reference.child("Caregiver").child(uid)
        val myRef2 = database.reference.child("Users").child(uid)

        //retrieve data from firebase
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val caregivers = snapshot.getValue(Caregiver::class.java)

                    val image = snapshot.child("image").value

                    binding.cgSkills.text = caregivers!!.skills
                    binding.cgExperience.text = caregivers!!.exp + " Year(s)"
                    binding.cgLocation.text = caregivers!!.location
                    binding.cgService.text = caregivers!!.service
                    binding.cgTimeMin.text = caregivers!!.mintime
                    binding.cgTimeMax.text = caregivers!!.maxTime
                    binding.cgPrice.text = caregivers!!.pricerate
                    binding.cgQ1.text = caregivers!!.q1
                    binding.cgQ2.text = caregivers!!.q2
                    binding.cgQ3.text = caregivers!!.q3
                    binding.cgDesc.text = caregivers!!.desc

                    Glide.with(this@CaregiverDetails)
                        .load(image)
                        .into(binding.cgImage)


                } else {
                    Toast.makeText(
                        this@CaregiverDetails,
                        "Error in retrieving details, Please contact Admin. ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.cgEmail.setOnClickListener {
            val email = binding.cgEmail.text
            val mail = Intent(Intent.ACTION_SENDTO)
            mail.data = Uri.parse("mailto:") // only email apps should handle this
            mail.putExtra(Intent.EXTRA_EMAIL, email)
            mail.putExtra(Intent.EXTRA_SUBJECT, "From OKUable")
            if (mail.resolveActivity(this.packageManager) != null) {
                startActivity(mail)
            }
        }

        binding.cgContactNumber.setOnClickListener {

            val number = binding.cgContactNumber.text
            val call = Intent(Intent.ACTION_DIAL);
            call.data = Uri.parse("tel:$number")
            startActivity(call)
        }


        myRef2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val contact = snapshot.child("phoneNumber").value.toString()
                    val email = snapshot.child("email").value.toString()

                    binding.cgContactNumber.text = contact
                    binding.cgEmail.text = email

                } else {
                    Toast.makeText(
                        this@CaregiverDetails,
                        "Error in retrieving contact info, Please contact Admin. ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.btnBack.setOnClickListener {
            finish()
        }


    }

}
