package com.madassignment.okuable.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.madassignment.okuable.R
import com.madassignment.okuable.activity.AddEvent
import com.madassignment.okuable.adapter.eventAdapter
import com.madassignment.okuable.data.Event
import com.madassignment.okuable.databinding.FragmentEventBinding
import com.madassignment.okuable.databinding.FragmentEventOrganiserBinding
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



        CaregiverFragment_Public.appContext = requireContext()

    val rvEvents: RecyclerView = binding.eventList

    eventList = ArrayList()
    mAdapter = eventAdapter(this.requireContext(), eventList)

    //mAdapter.notifyDataSetChanged()
    rvEvents.layoutManager = LinearLayoutManager(parentFragment?.context, LinearLayoutManager.VERTICAL, false)
    rvEvents.setHasFixedSize(true)
    rvEvents.invalidate()

    /**getData firebase*/



    val db = FirebaseFirestore.getInstance()
    db.collection("Event").get()
    .addOnSuccessListener { documents ->
        for (document in documents) {
            if(document.get("status")!!.equals("approved")) {
                eventList.add(document.toObject(Event::class.java));
            }


        }

        rvEvents.adapter = mAdapter

    }



    return binding.root
}

companion object {

    lateinit  var appContext: Context

}
}


