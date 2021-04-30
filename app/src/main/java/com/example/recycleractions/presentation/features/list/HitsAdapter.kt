package com.example.recycleractions.presentation.features.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleractions.R
import com.example.recycleractions.domain.entities.Hit
import com.example.recycleractions.presentation.utils.DateTimeUtil
import kotlinx.android.synthetic.main.hit_item.view.*

class HitsAdapter(private val context: Context,
                  private val hits: MutableList<Hit>,
                  private val listener: HitListener) : RecyclerView.Adapter<HitsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hit_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hits.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hit = hits[position]

        holder.setTitle(hit)

        holder.authorTime.text = hit.author.plus(" - ").plus(DateTimeUtil.getTimeAgo(hit.created_at))
        holder.itemView.setOnClickListener {
            listener.onclick(hit)
        }
    }

    fun clear() {
        hits.clear()
    }

    fun addItems(hitList: List<Hit>){
        hits.addAll(hitList)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        hits.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): Hit {
        return hits[position]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.txtTitle
        val authorTime: TextView = itemView.txtAuthorTime

        fun setTitle(hit: Hit){
            val hitTitle = if(!hit.title.isNullOrEmpty()){
                hit.title
            }else{
                hit.story_title
            }
            title.text = hitTitle
        }
    }
}