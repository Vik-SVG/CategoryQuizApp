package com.victor_priesniakov.categoryquizapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.victor_priesniakov.categoryquizapp.Common.Common
import com.victor_priesniakov.categoryquizapp.QuestionMainActivity
import com.victor_priesniakov.categoryquizapp.R
import com.victor_priesniakov.categoryquizapp.interface1.IonRecyclerViewItemClickListener
import com.victor_priesniakov.categoryquizapp.model.Category

class CategoryAdapter (internal var context: Context,
                       var fActivity: FragmentActivity?,
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
            override fun onClick(view: View?, position: Int) {
                Common.selectedCategory = categoryList[position]


                       val intent = Intent(context, QuestionMainActivity::class.java)
                        context.startActivity(intent)


                /*val fragment = QuestionMainFragment()
                fActivity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container, fragment, "tag")
                    ?.addToBackStack(null)
                    ?.commit()*/

                //TODO: fragment or activity
            } })
    }

}