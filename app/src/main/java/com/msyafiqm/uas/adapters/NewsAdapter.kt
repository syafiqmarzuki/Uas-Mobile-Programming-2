package com.msyafiqm.uas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.msyafiqm.uas.R
import com.msyafiqm.uas.models.News
import com.msyafiqm.uas.services.ApiUtils
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter(var news : List<News> , var context: Context) : RecyclerView.Adapter<NewsAdapter.ViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false))

    override fun getItemCount() = news.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.binding(news[position], context)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun binding(n : News, context: Context){
            itemView.tv_title.text = n.title
            Glide.with(context).load("${ApiUtils.API_URL+"/images/"}"+n.image).into(itemView.iv_image)
            itemView.setOnClickListener {
                Toast.makeText(context, "${n.title} dipilih", Toast.LENGTH_SHORT).show()
            }
        }
    }
}