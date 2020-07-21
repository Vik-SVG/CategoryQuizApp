package com.victor_priesniakov.categoryquizapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.victor_priesniakov.categoryquizapp.R
import com.victor_priesniakov.categoryquizapp.`interface`.IonRecyclerViewItemClickListener
import com.victor_priesniakov.categoryquizapp.model.Category

class CategoryAdapter (internal var context: Context,

                       internal var categoryList:List<Category>): RecyclerView.Adapter<CategoryAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{

        internal var txt_category_name:TextView
        internal var card_category:CardView
        internal lateinit var iOnRecyclerViewItemClickListener:IonRecyclerViewItemClickListener

        fun setiOnRecyclerViewItemClickListener (ionRecyclerViewItemClickListener: IonRecyclerViewItemClickListener){
            this.iOnRecyclerViewItemClickListener = ionRecyclerViewItemClickListener
        }

        init {
            txt_category_name = itemView.findViewById(R.id.txt_category_name) as TextView
            card_category = itemView.findViewById(R.id.card_category) as CardView
            itemView.setOnClickListener(this)
        }

        override fun onClick(mView: View?) {
            if (mView != null) {
                iOnRecyclerViewItemClickListener.onClick(mView, adapterPosition)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

    val itemView:View = LayoutInflater.from(context).inflate(R.layout.layout_category_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    holder.txt_category_name.text = categoryList[position].name

        holder.setiOnRecyclerViewItemClickListener(object : IonRecyclerViewItemClickListener {
            override fun onClick(view: View, position: Int) {
            Toast.makeText(context, "Click on "+ categoryList[position].name, Toast.LENGTH_SHORT).show()
            } })
    }

}