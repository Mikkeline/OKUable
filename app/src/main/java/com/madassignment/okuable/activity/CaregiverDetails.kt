package com.madassignment.okuable.activity

import android.os.Bundle
import android.widget.Toast
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
        val inputJobTitle: String = intent?.getStringExtra("jobtitle").toString()

        binding.cgFullName.text = inputName
        binding.cgJobtitle.text = inputJobTitle

        val database = Firebase.database
        val myRef = database.reference.child("Caregiver").child(inputName)

        //retrieve data from firebase
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                        val caregivers = snapshot.getValue(Caregiver::class.java)

                        /*val Name = snapshot.child("name").value.toString()
                        val JobTitle = snapshot.child("jobtitle").value.toString()
                        val Skills = snapshot.child("skills").value.toString()
                        val Exp = snapshot.child("exp").value.toString()
                        val Location = snapshot.child("location").value.toString()
                        val Service = snapshot.child("service").value.toString()
                        val MinTime = snapshot.child("mintime").value.toString()
                        val MaxTime = snapshot.child("maxTime").value.toString()
                        val PriceRate = snapshot.child("pricerate").value.toString()
                        val Desc = snapshot.child("desc").value.toString()
                        val Q1 = snapshot.child("q1").value.toString()
                        val Q2 = snapshot.child("q2").value.toString()
                        val Q3 = snapshot.child("q3").value.toString()*/
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

                    Glide.with(this@CaregiverDetails)
                         .load(image)
                         .into(binding.cgImage)


                }else{
                    Toast.makeText(this@CaregiverDetails, "error", Toast.LENGTH_LONG).show()
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

