package com.madassignment.okuable.fragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.madassignment.okuable.R
import com.madassignment.okuable.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var binding: ProfileFragment
    private lateinit var mDatabase: DatabaseReference
    var user = FirebaseAuth.getInstance().currentUser
    private lateinit var imageUrl: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

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

        val refStorage = FirebaseStorage.getInstance().reference.child("Profile_Image/$uid")
        val localfile = File.createTempFile("tempImage", "jpg")
        refStorage.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.profileImage.setImageBitmap(bitmap)
        }


        /*binding.profileImage.setOnClickListener {
            ChangeImage()
        }*/







        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val phoneNum = binding.etPhoneNumber.text.toString()
            val address = binding.etAddress.text.toString()


            mDatabase.child(uid).child("name").setValue(name)
            mDatabase.child(uid).child("phoneNumber").setValue(phoneNum)
            mDatabase.child(uid).child("address").setValue(address)

            Toast.makeText(requireActivity(), "Profile updated successfully！", Toast.LENGTH_LONG)
                .show()

            refStorage.delete()

            refStorage.putFile(imageUrl).addOnSuccessListener(
                OnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener {
                        val dlUrl = it.toString()
                        mDatabase.child(uid).child("image").setValue(dlUrl).addOnSuccessListener {


                        }.addOnFailureListener {

                            Toast.makeText(
                                requireActivity(),
                                "Failed, Please Try Again!",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                })


        }



        return binding.root
    }

    /*private fun ChangeImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            imageUrl = data.data!!
            with(binding) { profile_image.setImageURI(imageUrl) }
        }
    }*/


     */
}

