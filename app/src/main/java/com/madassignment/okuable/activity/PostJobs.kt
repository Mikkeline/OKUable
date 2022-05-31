package com.madassignment.okuable.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.madassignment.okuable.R
import com.madassignment.okuable.data.Caregiver
import com.madassignment.okuable.data.Users
import com.madassignment.okuable.databinding.ActivityPostJobsBinding
import kotlinx.android.synthetic.main.activity_splash_screen.*


class PostJobs : AppCompatActivity() {

    private lateinit var binding: ActivityPostJobsBinding
    private var imageUrl : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_jobs)

        val uid = FirebaseAuth.getInstance().uid

        val database = Firebase.database
        val myRef = database.reference.child("Users").child(uid!!)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    val cgname = snapshot.child("name").value
                    binding.etFullName.text = Editable.Factory.getInstance().newEditable(cgname.toString())

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        var btnBack  = binding.cgBtnBack
        btnBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        var q1 = binding.spinnerQ1
        var q2 = binding.spinnerQ2
        var q3 = binding.spinnerQ3
        var ansq1 = ""
        var ansq2 = ""
        var ansq3 = ""
        var service : String = "Services : "
        var count : Int = 0

        val location : Spinner = binding.spLocation
        var ansloc = ""

        val cb1 = binding.cb1
        val cb2 = binding.cb2
        val cb3 = binding.cb3
        val cb4 = binding.cb4
        val cb5 = binding.cb5
        val cb6 = binding.cb6
        val cb7 = binding.cb7
        val cb8 = binding.cb8


        binding.cgAddImage.setOnClickListener {
            AddImage()
        }
        val state = resources.getStringArray(R.array.state)
        if (location != null){
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line , state)
            location.adapter = adapter
        }

        location.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val spinner = binding.spLocation
                ansloc = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val option = resources.getStringArray(R.array.option)

        if (q1 != null){
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, option)
            q1.adapter = adapter
        }

        q1.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val spinner = binding.spinnerQ1
                ansq1 = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        if (q2 != null){
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, option)
            q2.adapter = adapter
        }

        q2.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val spinner = binding.spinnerQ2
                ansq2 = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        if (q3 != null){
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, option)
            q3.adapter = adapter
        }

        q3.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val spinner = binding.spinnerQ3
                ansq3 = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.cgBtnSubmit.setOnClickListener {

            if (cb1.isChecked) {
                service += " Medication oral and injections "
            }
            if (cb2.isChecked) {
                service += ", Patient's Activities of Daily Living(ADL) "
            }
            if (cb3.isChecked) {
                service += ", Bathing and Grooming "
            }
            if (cb4.isChecked) {
                service += ", Medical Escorts "
            }
            if (cb5.isChecked) {
                service += ", Meal Arrangements And Companionship "
            }
            if (cb6.isChecked) {
                service += ", Toileting And Personal Hygiene Care "
            }
            if (cb7.isChecked) {
                service += ", Ryle's Tube Feeding/Changing "
            }
            if (cb8.isChecked) {
                service += ", Travel with Client (follow up)"
            }

            val jobtitle = binding.etJobtitle.text.toString()
            val fullname = binding.etFullName.text.toString().trim()
            val exp = binding.etExperience.text.toString()
            val skills = binding.etSkills.text.toString()
            val maxtime = binding.etMax.text.toString()
            val mintime = binding.etMin.text.toString()
            val price = ("RM") + binding.etPrice.text.toString() + ("/per hour")
            val desc = binding.etDesc.text.toString()

            val uid = FirebaseAuth.getInstance().uid

            if (imageUrl != null) {
                if (jobtitle != "" && fullname != "" && skills != "" && maxtime != "" && mintime != "" && price != "" && desc != ""){

                    val database = Firebase.database
                    val myRef = database.getReference("Caregiver")
                    val caregiver = Caregiver(jobtitle,fullname,skills,exp,ansloc,service,mintime,maxtime,price,desc,ansq1,ansq2,ansq3,"pending")
                    myRef.child(uid.toString()).setValue(caregiver).addOnSuccessListener {
                        val refStorage = FirebaseStorage.getInstance().reference.child("Caregiver_Image/$uid")

                        refStorage.putFile(imageUrl!!).addOnSuccessListener(
                            OnSuccessListener {
                                it.storage.downloadUrl.addOnSuccessListener {
                                    val dlUrl = it.toString()
                                    myRef.child(uid.toString()).child("image").setValue(dlUrl).addOnSuccessListener {

                                        myRef.child(uid.toString()).child("uid").setValue(uid.toString())
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()

                                    }.addOnFailureListener{

                                        Toast.makeText(this,"Failed, Please Try Again!",Toast.LENGTH_SHORT).show()

                                    }
                                }
                            })


                    }.addOnFailureListener{

                        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()

                    }


                }else{
                    Toast.makeText(this,"Please Fill in all the field!",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Picture must be added!",Toast.LENGTH_SHORT).show()
            }


        }


        binding.btnPlus.setOnClickListener {
            count++
            if (count > 60) {
                count = 60
                Toast.makeText(
                    this,
                    "60 is the maximum!",
                    Toast.LENGTH_LONG
                ).show()
            }
            binding.etExperience.setText(count.toString())
        }
        binding.btnMinus.setOnClickListener {
            if (count <= 0){
                Toast.makeText(this, "0 is the minimum value. ",Toast.LENGTH_LONG).show()
            }else{
                count--
                binding.etExperience.setText(count.toString())
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
            with(binding) { cgAddImage.setImageURI(imageUrl) }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}