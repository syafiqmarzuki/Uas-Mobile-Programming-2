package com.msyafiqm.uas.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.msyafiqm.uas.ComplainActivity
import com.msyafiqm.uas.R
import com.msyafiqm.uas.models.Complain
import kotlinx.android.synthetic.main.item_complain.view.*

class ComplainAdapter (private var complains : List<Complain>, private var context : Context) : RecyclerView.Adapter<ComplainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_complain, parent, false))
    }

    override fun getItemCount() = complains.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.binding(complains[position], context)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun binding(c : Complain, context: Context){
            itemView.title.text = c.title
            itemView.message.text = c.message
            itemView.input1.text = c.input1
            itemView.input2.text = c.input2
            itemView.setOnClickListener {
                val i = Intent(context, ComplainActivity::class.java).apply {
                    putExtra("COMPLAIN", c)
                    putExtra("IS_UPDATE", true)
                }
                context.startActivity(i)
            }
        }
    }

}