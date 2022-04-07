package com.example.firestoreproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class Adapter (var recyclerArray: ArrayList<User> =ArrayList(),
               var recyclerItemClick: RecyclerItemClick
    ): RecyclerView.Adapter<Adapter.AdapterHolder>() {
        class AdapterHolder(view: View): RecyclerView.ViewHolder(view) {
            var tvname: TextView = view.findViewById(R.id.editname)
            var tvclass:TextView = view.findViewById(R.id.editclass)

        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): Adapter.AdapterHolder {
            val itemView= LayoutInflater.from(parent.context).inflate(R.layout.recyclerlayout,parent,false)
            return AdapterHolder(itemView)

        }

        override fun onBindViewHolder(holder: Adapter.AdapterHolder, position: Int) {
            holder.tvname.setText("${recyclerArray.get(position).name}")
            holder.tvclass.setText("${recyclerArray.get(position).classname}")
            holder.itemView.setOnClickListener {
                recyclerItemClick.updateClick(recyclerArray.get(position))
            }
//        holder.tvdate.setText("${recyclerArray.get(position).date}")


        }

        override fun getItemCount(): Int {
            return recyclerArray.size
        }


    }
