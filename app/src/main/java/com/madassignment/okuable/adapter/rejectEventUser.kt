package com.madassignment.okuable.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.madassignment.okuable.R
import com.madassignment.okuable.activity.AdminEventDetails
import com.madassignment.okuable.data.Event
import com.madassignment.okuable.fragment.EventFragment_Admin
import com.madassignment.okuable.fragment.EventFragment_Organiser

class rejectEventUser (
    var context: Context,
    private var events: ArrayList<Event>

) :

    RecyclerView.Adapter<rejectEventUser.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var eventPic: ImageView = itemView.findViewById(R.id.event_pic)
        var eventName: TextView = itemView.findViewById(R.id.eventName_tv)
        var startDate: TextView = itemView.findViewById(R.id.startDate_tv)
        var endDate: TextView = itemView.findViewById(R.id.endDate_tv)
        var address: TextView = itemView.findViewById(R.id.address_tv)
        var reason: TextView = itemView.findViewById(R.id.newReason)


    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.event_item_admin, parent, false)


        //user = FirebaseAuth.getInstance().currentUser
        //reference = FirebaseDatabase.getInstance().getReference("Users")
        // currentUserId = "5bJBTRI8QHM0YH0bwVdumSqsR5H3"//carereceiver
        //currentUserId = "KqwNtEpvjgdO8lpLfBXEcu8QdAI2" //caregiver

        val viewHolder = ViewHolder(itemView)

        //mAdapter?.notifyDataSetChanged()
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: rejectEventUser.ViewHolder, i: Int) {

        val currentPosition = events[i]
        val context = EventFragment_Organiser.appContext
        viewHolder.eventName.text = currentPosition.name
        viewHolder.endDate.text = currentPosition.endDate
        viewHolder.startDate.text = currentPosition.startDate
        viewHolder.address.text = currentPosition.location
        viewHolder.reason.text = currentPosition.reason



        Glide.with(context)
            .load(currentPosition.dlUrl)
            .into(viewHolder.eventPic)

        //onclicklistener when user click on recycleview
        viewHolder.itemView.setOnClickListener {
            val eventName = viewHolder.eventName.text


            //pass the eventName to eventDetails class
           // val intent = Intent(context, AdminEventDetails::class.java)
            //intent.putExtra("EventName", eventName)
            //context.startActivity(intent)


        }


    }


    override fun getItemCount(): Int {
        return events.size
    }

    fun filterList(filtered: ArrayList<Event>) {
        this.events = filtered
        notifyDataSetChanged()
    }


}