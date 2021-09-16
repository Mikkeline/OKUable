package com.madassignment.okuable.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.madassignment.okuable.R
import com.madassignment.okuable.databinding.FragmentProfileBinding
import com.okuable.okuable.data.Users
import kotlinx.android.synthetic.main.fragment_profile.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri : Uri
    private lateinit var mDatabase: DatabaseReference
    var user = FirebaseAuth.getInstance().currentUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(inflater ,R.layout.fragment_profile, container, false)

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




        mDatabase.child(uid).child("name").get().addOnSuccessListener {
            etName.setText(it.value.toString())
        }

        mDatabase.child(uid).child("phoneNumber").get().addOnSuccessListener {
            etPhoneNumber.setText(it.value.toString())
        }

        mDatabase.child(uid).child("address").get().addOnSuccessListener {
            etAddress.setText(it.value.toString())
        }









        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val phoneNum = binding.etPhoneNumber.text.toString()
            val address = binding.etAddress.text.toString()


                mDatabase.child(uid).child("name").setValue(name)
                mDatabase.child(uid).child("phoneNumber").setValue(phoneNum)
                 mDatabase.child(uid).child("address").setValue(address)

            Toast.makeText(requireActivity(), "Profile updated successfully", Toast.LENGTH_LONG).show()

            //uploadProfilePic()









        }



        return binding.root
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