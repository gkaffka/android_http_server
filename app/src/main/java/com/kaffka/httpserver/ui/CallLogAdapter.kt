package com.kaffka.httpserver.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaffka.httpserver.databinding.CallItemBinding

class CallLogAdapter(private val calls: List<String>) :
    RecyclerView.Adapter<CallLogAdapter.CallViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CallViewHolder(
            CallItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        holder.bind(calls[position])
    }

    override fun getItemCount() = calls.size

    class CallViewHolder(private val callItemBinding: CallItemBinding) :
        RecyclerView.ViewHolder(callItemBinding.root) {
        fun bind(formattedCall: String) {
            callItemBinding.callText.text = formattedCall
        }
    }
}
