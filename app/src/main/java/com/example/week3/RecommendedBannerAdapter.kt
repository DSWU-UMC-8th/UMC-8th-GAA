package com.example.week3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecommendedBannerAdapter (
    private val dataSet: Array<RecommendedBannerInfo>
): RecyclerView.Adapter<RecommendedBannerAdapter.ViewHolder> () {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.bannerTitle)
        val background: ImageView = view.findViewById(R.id.bannerBackground)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_recommended, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = dataSet[position].bannerTitle
        holder.background.setImageResource(dataSet[position].bannerBackground)
    }

    override fun getItemCount() = dataSet.size

}