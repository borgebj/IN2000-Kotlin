package com.example.borgebj_oblig2

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


class PartyAdapter(private val liste: MutableList<AlpacaParty>) :
    RecyclerView.Adapter<PartyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewt: View = view.findViewById(R.id.view)
        val textEn: TextView = view.findViewById(R.id.textEn)
        val circle: CircleImageView = view.findViewById(R.id.circle)
        val textTo: TextView = view.findViewById(R.id.textTo)
        var textTre: TextView = view.findViewById(R.id.textTre)
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
            .fitCenter()
            .into(viewHolder.circle)
        viewHolder.textTo.text = "Leader: ${liste[position].leader}"
        val votes: String = liste[position].votes
        val total: String = liste[position].total

        // sjekker om variablene ikke er 'null' etter formatering - finner prosent og viser
        if ("$votes" != "null" && "$total" != "null") {
            val tall: String = String.format("%.2f", (votes.toDouble() * 100) / total.toDouble())
            viewHolder.textTre.text = "Votes: $votes - $tall%"
        } else viewHolder.textTre.text = ""
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = liste.size

}