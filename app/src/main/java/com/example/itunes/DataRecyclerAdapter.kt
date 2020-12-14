package com.example.itunes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_view.view.*

class DataRecyclerAdapter(private val results : Array<SongTable>):RecyclerView.Adapter<DataRecyclerAdapter.DataViewHolder>() {

    class DataViewHolder(val view : View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_view,parent,false)
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int){
        holder.view.collectionNameTextView.text = results[position].collectionName?:""
        holder.view.nameTextView.text = results[position].trackName?:""
        holder.view.wrapperTypeTextView.text = (results[position].wrapperType?:"")+"-"+(results[position].kind?:"")
        holder.view.artistNameTextView.text = results[position].artistName?:""
        holder.view.relaseDateTextView.text = results[position].releaseDate

    }

    override fun getItemCount(): Int = results.size
}