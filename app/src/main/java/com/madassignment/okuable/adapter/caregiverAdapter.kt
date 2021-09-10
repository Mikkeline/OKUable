package com.madassignment.okuable.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.madassignment.okuable.R
import com.madassignment.okuable.activity.CaregiverDetails
import com.madassignment.okuable.data.CaregiverList
import com.madassignment.okuable.fragment.CaregiverFragment_Public
import java.util.*
import kotlin.collections.ArrayList


class CaregiverAdapter(
    val context: Context,
    private var caregivers: ArrayList<CaregiverList>
) :
    RecyclerView.Adapter<CaregiverAdapter.ViewHolder>(){

    private var firestore: FirebaseFirestore? = null
    private var reference: DatabaseReference? = null
    private var currentUserId: String? = null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var pic: ImageView = itemView.findViewById(R.id.cir_pic)
        var price: TextView = itemView.findViewById(R.id.cir_pricerate)
        var jobtitle: TextView = itemView.findViewById(R.id.cir_jobtitle)
        var name: TextView = itemView.findViewById(R.id.cir_name)
        var location: TextView = itemView.findViewById(R.id.cir_location)
        var desc: TextView = itemView.findViewById(R.id.cir_desc)

    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.caregiver_item_row, parent,false)

        //firestore = FirebaseFirestore.getInstance()
        //user = FirebaseAuth.getInstance().currentUser
        //reference = FirebaseDatabase.getInstance().getReference("Users")
        // currentUserId = "5bJBTRI8QHM0YH0bwVdumSqsR5H3"//carereceiver
        //currentUserId = "KqwNtEpvjgdO8lpLfBXEcu8QdAI2" //caregiver

        val viewHolder = ViewHolder(itemView)
        
        //mAdapter?.notifyDataSetChanged()
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        val currentPosition = caregivers[i]
        val context = CaregiverFragment_Public.appContext

        viewHolder.jobtitle.text = currentPosition.jobtitle
        viewHolder.price.text = currentPosition.pricerate
        viewHolder.name.text = currentPosition.name
        viewHolder.location.text = currentPosition.location
        viewHolder.desc.text = currentPosition.desc

        Glide.with(context)
            .load(currentPosition.image)
            .into(viewHolder.pic)

        //onclicklistener when user click on recycleview
        viewHolder.itemView.setOnClickListener {
            val jobtitle = viewHolder.jobtitle.text
            val name = viewHolder.name.text

            //intent to caregiver detail activity
            val intent = Intent(context, CaregiverDetails::class.java)
            intent.putExtra("jobtitle", jobtitle)
            intent.putExtra("name", name)
            //Toast.makeText(context,"$name + $jobtitle",Toast.LENGTH_LONG).show()

            context.startActivity(intent)


        }
    }


    override fun getItemCount(): Int {
        return caregivers.size
    }

    fun filterList(filteredNames: ArrayList<CaregiverList>) {
        this.caregivers = filteredNames
        notifyDataSetChanged()
    }


}







