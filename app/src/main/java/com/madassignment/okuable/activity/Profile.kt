package com.madassignment.okuable.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.madassignment.okuable.R
import com.madassignment.okuable.databinding.ActivityCommentsBinding
import com.madassignment.okuable.databinding.ActivityProfileBinding

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var mDatabase: DatabaseReference
    var user = FirebaseAuth.getInstance().currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)


        val tvUsername = binding.tvUsername
        val tvEmail = binding.tvEmailAddress
        val uid = user!!.uid



        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        mDatabase.child(uid).child("username").get().addOnSuccessListener {
            tvUsername.text = it.value.toString()
        }
        mDatabase.child(uid).child("email").get().addOnSuccessListener {
            tvEmail.text = it.value.toString()
        }










        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val phoneNum = binding.etPhoneNumber.text.toString()
            val address = binding.etAddress.text.toString()


            mDatabase.child(uid).child("name").setValue(name)
            mDatabase.child(uid).child("phoneNumber").setValue(phoneNum)
            mDatabase.child(uid).child("address").setValue(address)



            //uploadProfilePic()







            val intent = Intent(this, CarereceiverLogin::class.java)
            startActivity(intent)
             Toast.makeText(this, "Profile completed... Please login again", Toast.LENGTH_LONG).show()
        }



    }

    /*private fun uploadProfilePic() {

        imageUri = Uri.parse("android.resource://${com.madassignment.okuable.R.drawable.add_image}")
        storageReference = FirebaseStorage.getInstance().getReference("Users/"+auth.currentUser?.uid)
        storageReference.putFile(imageUri).addOnSuccessListener {

            Toast.makeText(context, "Profile successfully updated", Toast.LENGTH_LONG).show()

        }.addOnFailureListener{

            Toast.makeText(context,"Failed to upload the image", Toast.LENGTH_SHORT).show()
        }

    }*/




}