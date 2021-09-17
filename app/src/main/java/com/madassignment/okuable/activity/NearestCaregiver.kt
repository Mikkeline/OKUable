package com.madassignment.okuable.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.madassignment.okuable.R
import com.madassignment.okuable.adapter.CaregiverAdapter
import com.madassignment.okuable.data.CaregiverList

class NearestCaregiver : AppCompatActivity() {

    private lateinit var cgList: ArrayList<CaregiverList>
    private var mAdapter: CaregiverAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearest_caregiver)

        val btnBack : ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        val rvCaregiver : RecyclerView = findViewById(R.id.caregiver_list)

        cgList = ArrayList()
        mAdapter = CaregiverAdapter(this, cgList)

        val intent = intent
        val location: String = intent.getStringExtra("loc").toString()

        //mAdapter.notifyDataSetChanged()
        rvCaregiver.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCaregiver.setHasFixedSize(true)
        rvCaregiver.invalidate()

        /**getData firebase*/
        val database = Firebase.database
        val myRef = database.reference.child("Caregiver")

        //read from firebase
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    for (Snapshot in snapshot.children){
                        //retrieve all data
                        val caregivers = Snapshot.getValue(CaregiverList::class.java)

                        val status = caregivers?.status.toString()
                        val loc = caregivers?.location.toString()
                        //only display status approve
                        if (status == "approve") {
                            if (loc == location){
                                if (caregivers != null){
                                    cgList.add(caregivers!!)
                                }else{
                                    Toast.makeText(this@NearestCaregiver, "COULD NOT FIND CAREGIVER IN THE AREA!",Toast.LENGTH_LONG).show()
                                }
                            }

                        }

                    }
                    rvCaregiver.adapter = mAdapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@NearestCaregiver, "COULD NOT FIND CAREGIVER IN THE AREA!",Toast.LENGTH_LONG).show()
            }
        })
    }
}