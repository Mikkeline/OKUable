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
import com.madassignment.okuable.activity.CaregiverDetails
import com.madassignment.okuable.data.CaregiverList
import com.madassignment.okuable.fragment.CaregiverFragment_Admin
import com.madassignment.okuable.fragment.CaregiverFragment_Public

class approveJobAdapter (
    val context: Context,
    private var caregivers: ArrayList<CaregiverList>
) :
    RecyclerView.Adapter<approveJobAdapter.ViewHolder>(){

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

        val viewHolder = ViewHolder(itemView)

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        val currentPosition = caregivers[i]
        val context = CaregiverFragment_Admin.appContext

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
            intent.putExtra("uid",currentPosition.uid)
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
