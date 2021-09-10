package com.madassignment.okuable.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.ktx.Firebase
import com.madassignment.okuable.R
import com.madassignment.okuable.activity.AddEvent
import com.madassignment.okuable.adapter.eventAdapter
import com.madassignment.okuable.data.Event
import com.madassignment.okuable.databinding.FragmentEventOrganiserBinding
import com.squareup.okhttp.internal.DiskLruCache
import java.util.*


class EventFragment_Organiser : Fragment() {

    private lateinit var eventList: ArrayList<Event>
    private var mAdapter: eventAdapter? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding : FragmentEventOrganiserBinding = DataBindingUtil.inflate(inflater ,
            R.layout.fragment_event__organiser, container, false)



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








        binding.btnAddEvent.setOnClickListener {
            val intent = Intent(context, AddEvent::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    companion object {

        lateinit  var appContext: Context

    }
}


