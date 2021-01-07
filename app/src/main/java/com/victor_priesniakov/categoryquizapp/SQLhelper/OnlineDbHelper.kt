package com.victor_priesniakov.categoryquizapp.SQLhelper

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.*
import com.victor_priesniakov.categoryquizapp.interface1.MyCallback
import com.victor_priesniakov.categoryquizapp.model.Question
import dmax.dialog.SpotsDialog

class OnlineDbHelper(internal var context: Context,
                    internal var firebaseDatabase: FirebaseDatabase) {
    internal var mQuiz:DatabaseReference
        init {
            mQuiz = this.firebaseDatabase.getReference("/CategoryQuiz")
    }
    companion object{
        private var instance:OnlineDbHelper? = null

        @Synchronized
        fun getInstance (context: Context, firebaseDatabase: FirebaseDatabase):OnlineDbHelper{
            if(instance == null)
                instance = OnlineDbHelper(context, firebaseDatabase)
            return instance!!

        }
    }

    fun readData(myCallback: MyCallback, category:String){
        val dialog = SpotsDialog.Builder().setContext(context).setCancelable(false).build()
      //  if(!dialog.isShowing)
//            dialog.show() //TODO:exeption
        mQuiz.child(category)
            .child("question")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val questionList = ArrayList<Question>()
                    for (questionSnapshot in snapshot.children)
                        questionList.add(questionSnapshot.getValue(Question::class.java)!!)

                    myCallback.setQuestionList(questionList)

                    if (dialog.isShowing)
                        dialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, ""+error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}