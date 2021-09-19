package com.madassignment.okuable.fragment



import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.madassignment.okuable.R
import com.madassignment.okuable.adapter.CaregiverAdapter
import com.madassignment.okuable.adapter.adminCaregiver
import com.madassignment.okuable.data.CaregiverList
import com.madassignment.okuable.databinding.FragmentCaregiverAdminBinding
import java.util.ArrayList
import java.util.*

class CaregiverFragment_Admin : Fragment() {
    private lateinit var cgList: ArrayList<CaregiverList>
    private var mAdapter: adminCaregiver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentCaregiverAdminBinding = DataBindingUtil.inflate(inflater ,R.layout.fragment_caregiver__admin, container, false)

        appContext = requireContext()

        val rvCaregivers: RecyclerView = binding.caregiverList

        cgList = ArrayList()

        //mAdapter.notifyDataSetChanged()
        val llm = LinearLayoutManager(context)
        rvCaregivers.layoutManager = llm
        rvCaregivers.setHasFixedSize(true)
        rvCaregivers.invalidate()

        /**getData firebase*/
        val database = Firebase.database
        val myRef = database.reference.child("Caregiver")


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (Snapshot in snapshot.children){
                        val caregivers = Snapshot.getValue(CaregiverList::class.java)

                        val status = caregivers?.status.toString()

                        if (status == "pending") {
                            cgList.add(caregivers!!)
                        }
                    }
                    mAdapter = adminCaregiver(appContext, cgList)
                    rvCaregivers.adapter = mAdapter
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
                filter(editable.toString().lowercase())
            }
        })


        return binding.root
    }

    private fun filter(text: String) {
        //new array list that will hold the filtered data
        val filteredNames = ArrayList<CaregiverList>()
        //looping through existing elements and adding the element to filtered list
        cgList.filterTo(filteredNames) {
            //if the existing elements contains the search input
            it.name.lowercase().contains(text.lowercase())||
                    it.jobtitle.lowercase().contains(text.lowercase())||
                    it.pricerate.lowercase().contains(text.lowercase())
        }
        //calling a method of the adapter class and passing the filtered list
        mAdapter!!.filterList(filteredNames)
    }


    companion object {

        lateinit  var appContext: Context

    }

}