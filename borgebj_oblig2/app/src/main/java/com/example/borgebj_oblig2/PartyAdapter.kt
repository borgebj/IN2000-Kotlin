package com.example.borgebj_oblig2

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView

class PartyAdapter(private val liste: MutableList<AlpacaParty>) :
    RecyclerView.Adapter<PartyAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewt: View
        val textEn: TextView
        val circle: CircleImageView
        val textTo: TextView
        var textTre: TextView


        init {
            // Define click listener for the ViewHolder's View.
            viewt = view.findViewById(R.id.view)
            circle = view.findViewById(R.id.circle)
            textEn = view.findViewById(R.id.textEn)
            textTo = view.findViewById(R.id.textTo)
            textTre = view.findViewById(R.id.textTre)
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.element, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.viewt.setBackgroundColor(Color.parseColor(liste[position].color))
        viewHolder.textEn.text = liste[position].name

        Glide.with(viewHolder.itemView.context)
            .load(liste[position].img)
            .placeholder(R.mipmap.ic_launcher_round)
            .fitCenter()
            .into(viewHolder.circle)
        viewHolder.textTo.text = "Leader: ${liste[position].leader}"
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = liste.size

}