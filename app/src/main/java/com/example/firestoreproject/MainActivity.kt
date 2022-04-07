package com.example.firestoreproject

import android.app.Dialog
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firestoreproject.databinding.ActivityMainBinding
import com.example.firestoreproject.databinding.DialoglayoutBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.EventListener

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity(), RecyclerItemClick {
    lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore
    lateinit var adapter: Adapter
    lateinit var linearLayoutManager: LinearLayoutManager
    var arrayList: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        adapter = Adapter(arrayList, this)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rv.layoutManager = linearLayoutManager
        binding.rv.adapter = adapter

        binding.btn.setOnClickListener {
            dialog()
        }

        db.collection("users")
            .addSnapshotListener(object : EventListener<QuerySnapshot?> {
                override fun onEvent(
                    documentSnapshots: QuerySnapshot?,
                    e: FirebaseFirestoreException?
                ) {
                    if (e != null) {
                        Log.d("TAG", "onEvent: error" + e.message)
                    }
                    for (document in documentSnapshots!!.documentChanges) {
                        when (document.type) {
                            DocumentChange.Type.ADDED -> {
                                val userModel: User =
                                    document.document.toObject(User::class.java)
                                userModel.dbReference = document.document.id
                                arrayList.add(userModel)
                                adapter.notifyDataSetChanged()
                            }
                            DocumentChange.Type.REMOVED -> {
                            }
                            DocumentChange.Type.MODIFIED -> {
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
    }

    fun dialog() {
        var dialog = Dialog(this)
        var dialogBinding = DialoglayoutBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialogBinding.btnadd.setOnClickListener {
            val user = hashMapOf(
                "name" to dialogBinding.editname.text.toString(),
                "classname" to dialogBinding.editclass.text.toString()
            )
            db.collection("documents").add(user)
                .addOnCompleteListener {
                    dialog.dismiss()
                }.addOnCanceledListener { }
            /*  db.collection("documents").document("details of student")
                  .set(user)
                  .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                  .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }*/
        }
        dialog.show()
    }

    override fun updateClick(user: User) {
        var dialog = Dialog(this)
        var dialogBinding = DialoglayoutBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialogBinding.btnadd.setOnClickListener {
            user.name =  dialogBinding.editname.text.toString()
            user.classname = dialogBinding.editclass.text.toString()

              db.collection("documents").document(user.dbReference)
                  .set(user)
                  .addOnSuccessListener {
                      dialog.dismiss()
                      Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                  .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
        }
        dialog.show()
    }


}