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
import com.google.firebase.firestore.FirebaseFirestore
import com.madassignment.okuable.R
import com.madassignment.okuable.adapter.eventAdapter
import com.madassignment.okuable.data.Event
import com.madassignment.okuable.databinding.FragmentEventBinding
import java.util.ArrayList

class EventFragment_User : Fragment() {
    private lateinit var eventList: ArrayList<Event>
    private var mAdapter: eventAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentEventBinding= DataBindingUtil.inflate(inflater ,
            R.layout.fragment_event, container, false)



       EventFragment_Organiser.appContext = requireContext()

    var rvEvents: RecyclerView = binding.eventList

    eventList = ArrayList()


    //mAdapter.notifyDataSetChanged()
    rvEvents.layoutManager = LinearLayoutManager(parentFragment?.context, LinearLayoutManager.VERTICAL, false)
    rvEvents.setHasFixedSize(true)
    rvEvents.invalidate()

    //getData firestore
    val db = FirebaseFirestore.getInstance()
    db.collection("Event").get()
    .addOnSuccessListener { documents ->
        for (document in documents) {
            if(document.get("status")!!.equals("approved")) {
                eventList.add(document.toObject(Event::class.java))
            }


        }
        mAdapter = eventAdapter(EventFragment_Organiser.appContext, eventList)
        rvEvents.adapter = mAdapter

    }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
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
        val filtered = ArrayList<Event>()
        //looping through existing elements and adding the element to filtered list
        eventList.filterTo(filtered) {
            //if the existing elements contains the search input
            it.name.lowercase().contains(text.lowercase()) ||
                    it.location.lowercase().contains(text.lowercase())

        }
        //calling a method of the adapter class and passing the filtered list
        mAdapter!!.filterList(filtered)
    }


companion object {

    lateinit  var appContext: Context

}
}


