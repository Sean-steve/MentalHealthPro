package com.sean_steve.mentalhealthpro

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController

import com.sean_steve.mentalhealthpro.placeholder.PlaceholderContent.PlaceholderItem

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class RecyclerViewAdapter(
    private var titles: List<String>, private var details:List<String>, private var images:List<Int>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView = itemView.findViewById(R.id.rv_title)
        var itemDetails: TextView = itemView.findViewById(R.id.rv_desc)
        var itemPicture: ImageView = itemView.findViewById(R.id.rv_image)

        init {
            itemView.setOnClickListener { v: View ->
                val position: Int = absoluteAdapterPosition
                if (position == 1) {
                    Toast.makeText(itemView.context,
                        "opening ${position + 1}...",
                        Toast.LENGTH_SHORT).show()
                    v.findNavController().navigate(R.id.action_illnessRecyclerView_to_anxiety)
                } else if (position == 2) {
                    Toast.makeText(itemView.context,
                        "opening ${position + 1}...",
                        Toast.LENGTH_SHORT).show()
                    v.findNavController()
                        .navigate(R.id.action_illnessRecyclerView_to_moodRelatedDisorder)
                } else if (position == 3) {
                    Toast.makeText(itemView.context,
                        "opening ${position + 1}...",
                        Toast.LENGTH_SHORT).show()
                    v.findNavController()
                        .navigate(R.id.action_illnessRecyclerView_to_depressiveDisorder)
                } else if (position == 4) {
                    Toast.makeText(itemView.context,
                        "opening ${position + 1}...",
                        Toast.LENGTH_SHORT).show()
                    v.findNavController().navigate(R.id.action_illnessRecyclerView_to_phobia2)


                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.illness_recycler_view, parent, false)
        return ViewHolder(v)


    }
    override fun getItemCount(): Int {
        return titles.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemTitle.text= titles[position]
        holder.itemDetails.text=details[position]
        holder.itemPicture.setImageResource(images[position])
    }

}