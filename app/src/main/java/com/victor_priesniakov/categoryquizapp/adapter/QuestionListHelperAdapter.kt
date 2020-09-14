package com.victor_priesniakov.categoryquizapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.victor_priesniakov.categoryquizapp.Common.Common
import com.victor_priesniakov.categoryquizapp.R
import com.victor_priesniakov.categoryquizapp.interface1.IonRecyclerViewItemClickListener
import com.victor_priesniakov.categoryquizapp.model.CurrentQuestion
import kotlinx.android.synthetic.main.layout_question_list_helper_item.view.*

class QuestionListHelperAdapter (
    internal var context:Context,
    internal var answerSheetList: List<CurrentQuestion>): RecyclerView.Adapter<QuestionListHelperAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.layout_question_list_helper_item, parent, false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTxtQuestionNumber.text = (position + 1).toString()
        if(answerSheetList[position].type ==Common.ANSWER_TYPE.RIGHT_ANSWER )
            holder.mLayoutWrapper.setBackgroundResource(R.drawable.grid_item_right_answer)
        else if (answerSheetList[position].type ==Common.ANSWER_TYPE.WRONG_ANSWER)
            holder.mLayoutWrapper.setBackgroundResource(R.drawable.grid_item_wrong_answer)
        else
            holder.mLayoutWrapper.setBackgroundResource(R.drawable.grid_item_no_answer)

        holder.setIonRecyclerViewItemClickListener(object : IonRecyclerViewItemClickListener{
            override fun onClick(view: View?, position: Int) {
                LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(Intent(Common.KEY_GO_TO_QUESTION).putExtra(Common.KEY_GO_TO_QUESTION, position))
            }
        })

    }

    override fun getItemCount(): Int {
        return answerSheetList.size
    }

    inner class MyViewHolder (itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{

        override fun onClick(v: View?) {
            iOnRecyclerViewItemClickListener.onClick(v!!, adapterPosition)
        }

        internal var mTxtQuestionNumber:TextView
        internal var mLayoutWrapper:LinearLayout
        lateinit var iOnRecyclerViewItemClickListener:IonRecyclerViewItemClickListener

        fun setIonRecyclerViewItemClickListener(iOnRecyclerViewItemClickListener:IonRecyclerViewItemClickListener){
            this.iOnRecyclerViewItemClickListener = iOnRecyclerViewItemClickListener
        }

        init {
            mTxtQuestionNumber = itemView.findViewById(R.id.text_question_number) as TextView
            mLayoutWrapper = itemView.findViewById(R.id.layout_wrapper) as LinearLayout
            itemView.setOnClickListener(this)
        }

    }
}