package com.imran.gshopadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imran.gshopadmin.databinding.AllOrderDetailItemBinding
import com.imran.gshopadmin.model.AllOrderModel

class AllOrderAdapter(val list : ArrayList<AllOrderModel>,val context : Context)
    : RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>() {

    inner class AllOrderViewHolder(val binding: AllOrderDetailItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        return AllOrderViewHolder(
            AllOrderDetailItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.binding.productName.text = list[position].name
        holder.binding.productPrice.text = list[position].price
        holder.binding.btnCancel.setOnClickListener {
//            holder.binding.btnProceed.text = "Canceled"
            holder.binding.btnProceed.visibility = GONE


            updateStatus("Canceled", list[position].orderId!!)
        }

        when (list[position].status) {
            "Ordered" -> {
                holder.binding.btnProceed.text = "Dispatched"
                holder.binding.btnProceed.setOnClickListener {
                    updateStatus("Dispatched", list[position].orderId!!)
                }
            }
            "Dispatched" -> {
                holder.binding.btnProceed.text = "Delivered"
                holder.binding.btnProceed.setOnClickListener {
                    updateStatus("Delivered", list[position].orderId!!)
                }

            }
            "Delivered" -> {
                holder.binding.btnCancel.visibility = GONE
                holder.binding.btnProceed.isEnabled = false

                holder.binding.btnProceed.text = "Already Delivered"
//                holder.binding.btnCancel.setOnClickListener {
//                    updateStatus("Canceled", list[position].orderId!!)
//                }
            }
                "Canceled"->{
                    holder.binding.btnProceed.visibility = GONE
                    holder.binding.btnCancel.isEnabled = false
                }
            }
        }
    fun updateStatus(str : String, doc : String){
        val data = hashMapOf<String, Any>()
        data["status"] = str
        Firebase.firestore.collection("allOrders")
        .document(doc).update(data).addOnSuccessListener {
                Toast.makeText(context, "Status Updated", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }

}