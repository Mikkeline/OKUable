package com.madassignment.okuable.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.madassignment.okuable.R
import com.madassignment.okuable.databinding.ActivityCommentsBinding
import com.madassignment.okuable.databinding.ActivityProfileBinding


class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var mDatabase: DatabaseReference
    var user = FirebaseAuth.getInstance().currentUser
    private lateinit var imageUrl : Uri


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

        binding.profileImage.setOnClickListener {
            AddImage()
        }









        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val phoneNum = binding.etPhoneNumber.text.toString()
            val address = binding.etAddress.text.toString()


            if (!(name == null && phoneNum == null && address == null )) {

                mDatabase.child(uid).child("name").setValue(name)
                mDatabase.child(uid).child("phoneNumber").setValue(phoneNum)
                mDatabase.child(uid).child("address").setValue(address)



                val refStorage = FirebaseStorage.getInstance().reference.child("Profile_Image/$uid")

                refStorage.putFile(imageUrl).addOnSuccessListener(
                    OnSuccessListener {
                        it.storage.downloadUrl.addOnSuccessListener {
                            val dlUrl = it.toString()
                            mDatabase.child(uid).child("image").setValue(dlUrl).addOnSuccessListener {

                                val intent = Intent(this, CarereceiverLogin::class.java)
                                startActivity(intent)
                                Toast.makeText(this, "Profile completed. Please login again!", Toast.LENGTH_LONG).show()

                            }.addOnFailureListener{

                                Toast.makeText(this,"Failed, Please Try Again!",Toast.LENGTH_SHORT).show()

                            }
                        }
                    })


            }else{
                Toast.makeText(this,"Please insert all the fields!",Toast.LENGTH_SHORT).show()
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
            with(binding) { profileImage.setImageURI(imageUrl) }
        }
    }



}