package com.madassignment.okuable.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.madassignment.okuable.R
import com.madassignment.okuable.activity.CaregiverDetails
import com.madassignment.okuable.activity.NearestCaregiver
import com.madassignment.okuable.activity.PostJobs
import com.madassignment.okuable.adapter.CaregiverAdapter
import com.madassignment.okuable.data.CaregiverList
import com.madassignment.okuable.databinding.FragmentCaregiverPublicBinding


class CaregiverFragment_Public : Fragment() {

    private lateinit var cgList: ArrayList<CaregiverList>
    private var mAdapter: CaregiverAdapter? = null
    private var list: List<CaregiverList>? = null
    private lateinit var binding: FragmentCaregiverPublicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = inflate(inflater ,
            R.layout.fragment_caregiver_public, container, false)

        appContext = requireContext()

        var rvCaregivers: RecyclerView = binding.caregiverList

        //mAdapter.notifyDataSetChanged()

        val llm = LinearLayoutManager(context)
        rvCaregivers.layoutManager = llm
        rvCaregivers.setHasFixedSize(true)
        rvCaregivers.invalidate()

        cgList = ArrayList()

        /**getData firebase*/
        val database = Firebase.database
        val myRef = database.reference.child("Caregiver")

        //read from firebase
        myRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    for (Snapshot in snapshot.children){
                        //retrieve all data
                        val caregivers = Snapshot.getValue(CaregiverList::class.java)

                        val status = caregivers?.status.toString()
                        //only display status approve
                        if (status == "approve") {


                            cgList.add(caregivers!!)
                        }

                    }
                    mAdapter = CaregiverAdapter(appContext, cgList)
                    rvCaregivers.adapter = mAdapter

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString())
            }
        })

        binding.btnAddJob.setOnClickListener {
            val intent = Intent(context, PostJobs::class.java)
            startActivity(intent)
        }

        binding.btnLocation.setOnClickListener {
            val intent = Intent(activity, NearestCaregiver::class.java)
            intent.putExtra("location", "Selangor")
            startActivity(intent)
        }

        return binding.root
    }

    private fun filter(text: String) {
        //new array list that will hold the filtered data
        val filteredNames = ArrayList<CaregiverList>()
        //looping through existing elements and adding the element to filtered list
        cgList.filterTo(filteredNames) {
            //if the existing elements contains the search input
            it.name.lowercase().contains(text.lowercase()) ||
            it.jobtitle.lowercase().contains(text.lowercase()) ||
            it.pricerate.lowercase().contains(text.lowercase())
        }
        //calling a method of the adapter class and passing the filtered list
        mAdapter!!.filterList(filteredNames)
    }


    companion object {

        lateinit  var appContext: Context

    }


    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        appContext = requireContext()

        val rvCaregivers: RecyclerView = binding.caregiverList

        cgList = ArrayList()
        mAdapter = CaregiverAdapter(appContext, cgList)

        //mAdapter.notifyDataSetChanged()
        rvCaregivers.layoutManager = LinearLayoutManager(context)
        rvCaregivers.setHasFixedSize(true)
        rvCaregivers.invalidate()

        *//**getData firebase*//*
        val database = Firebase.database
        val myRef = database.reference.child("Caregiver")

        //read from firebase
        myRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    for (Snapshot in snapshot.children){
                        //retrieve all data
                        val caregivers = Snapshot.getValue(CaregiverList::class.java)

                        val status = caregivers?.status.toString()
                        //only display status approve
                        if (status == "approve") {
                            cgList.add(caregivers!!)
                        }

                    }
                    rvCaregivers.adapter = mAdapter

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString())
            }
        })

        binding.btnAddJob.setOnClickListener {
            val intent = Intent(context, PostJobs::class.java)
            startActivity(intent)
        }

        binding.btnLocation.setOnClickListener {
            val intent = Intent(activity, NearestCaregiver::class.java)
            intent.putExtra("location", "Selangor")
            startActivity(intent)
        }


        super.onViewCreated(view, savedInstanceState)
    }*/
}




