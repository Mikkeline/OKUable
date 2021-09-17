package com.madassignment.okuable.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
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
import com.madassignment.okuable.adapter.CaregiverAdapter
import com.madassignment.okuable.data.CaregiverList
import com.madassignment.okuable.databinding.FragmentCaregiverUserBinding
import java.util.*

class CaregiverFragment_User : Fragment() {

    private lateinit var cgList: ArrayList<CaregiverList>
    private var mAdapter: CaregiverAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentCaregiverUserBinding = DataBindingUtil.inflate(inflater ,R.layout.fragment_caregiver_user, container, false)

        CaregiverFragment_Public.appContext = requireContext()

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

                        if (status == "approve") {
                            cgList.add(caregivers!!)
                        }
                    }
                    mAdapter = CaregiverAdapter(CaregiverFragment_Public.appContext, cgList)
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

        binding.btnLocation.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            var str = ""
            // dialog title
            builder.setTitle("Choose a Location. ")

            val state = arrayOf(
                "Kuala Lumpur",
                "Labuan",
                "Putrajaya",
                "Johor",
                "Kedah",
                "Kelantan",
                "Malacca",
                "Negeri Sembilan",
                "Pahang",
                "Penang",
                "Perak",
                "Perlis",
                "Sabah",
                "Sarawak",
                "Selangor",
                "Terengganu"
            )

            // set single choice items
            builder.setSingleChoiceItems(
                state, // array
                -1 // initial selection (-1 none)
            ){dialog, i ->}


            // alert dialog positive button
            builder.setPositiveButton("Okay"){dialog,which->
                val position = (dialog as AlertDialog).listView.checkedItemPosition
                // if selected, then get item text
                if (position !=-1){
                    val selectedItem = state[position]
                    str = selectedItem
                    val intent = Intent(activity, NearestCaregiver::class.java)
                    intent.putExtra("loc", str)
                    startActivity(intent)
                }
            }

            // alert dialog other buttons
            builder.setNeutralButton("Cancel",null)

            // set dialog non cancelable
            builder.setCancelable(false)

            // finally, create the alert dialog and show it
            val dialog = builder.create()
            dialog.show()

            // initially disable the positive button
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

            // dialog list item click listener
            dialog.listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    // enable positive button when user select an item
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .isEnabled = position != -1

                }
        }

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