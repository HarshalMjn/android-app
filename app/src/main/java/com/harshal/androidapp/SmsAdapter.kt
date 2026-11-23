package com.harshal.androidapp

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SmsAdapter : RecyclerView.Adapter<SmsAdapter.ViewHolder>() {

    private var items: List<SmsMessage> = emptyList()

    fun submit(messages: List<SmsMessage>) {
        items = messages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sms, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val sender: TextView = view.findViewById(R.id.sender)
        private val date: TextView = view.findViewById(R.id.date)
        private val body: TextView = view.findViewById(R.id.body)

        fun bind(message: SmsMessage) {
            sender.text = message.sender.ifBlank { itemView.context.getString(R.string.unknown_sender) }
            date.text = DateUtils.getRelativeTimeSpanString(
                message.timestamp,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS,
            )
            body.text = message.body
        }
    }
}
