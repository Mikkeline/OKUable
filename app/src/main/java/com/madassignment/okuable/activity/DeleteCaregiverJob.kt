package com.madassignment.okuable.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
import com.madassignment.okuable.databinding.ActivityDeleteCaregiverJobBinding

class DeleteCaregiverJob : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteCaregiverJobBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delete_caregiver_job)

        val intent = intent
        val inputName: String = intent?.getStringExtra("name").toString()
        val uid: String = intent?.getStringExtra("uid").toString()
        val inputJobTitle: String = intent?.getStringExtra("jobtitle").toString()
        Toast.makeText(this,"You have already posted one job, here is the details. ", Toast.LENGTH_LONG).show()

        binding.cgFullName.text = inputName
        binding.cgJobtitle.text = inputJobTitle

        val database = Firebase.database
        val myRef = database.reference.child("Caregiver").child(uid)
        val myRef2 = database.reference.child("Users").child(uid)

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

                    Glide.with(this@DeleteCaregiverJob)
                        .load(image)
                        .into(binding.cgImage)


                }else{
                    Toast.makeText(this@DeleteCaregiverJob, "Error in retrieving details, Please contact Admin. ", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        myRef2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    val contact = snapshot.child("phoneNumber").value.toString()
                    val email = snapshot.child("email").value.toString()

                    binding.cgContactNumber.text = contact
                    binding.cgEmail.text = email

                }else{
                    Toast.makeText(this@DeleteCaregiverJob, "Error in retrieving contact info, Please contact Admin. ", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnDelete.setOnClickListener {
            dialogYesOrNo(
                this,
                "Are you sure you want to delete this jobs record?",
                "Yes to Delete record, \nNo to keep the record. ",
                DialogInterface.OnClickListener { dialog, id ->
                    myRef.child("status").setValue("Delete").addOnSuccessListener {
                        Toast.makeText(this, "Jobs Record has been deleted, please contact Admin if you have any inquiry. ", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                })
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