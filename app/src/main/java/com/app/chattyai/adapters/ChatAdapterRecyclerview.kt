package com.app.chattyai.adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.chattyai.R
import com.app.chattyai.models.Messages
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapterRecyclerview(
    private val messageList: List<Messages>,
    val imgsend: ImageView?,
    val chatrecyclerview: RecyclerView?
) :
    RecyclerView.Adapter<ChatAdapterRecyclerview.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        var layout = -1
        when (viewType) {
            Messages.TYPE_MESSAGE -> layout = R.layout.item_message
            Messages.TYPE_MESSAGE_REMOTE -> layout = R.layout.itemmessageleft
            Messages.TYPE_ACTION -> layout = R.layout.item_action
        }
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]

        val d = Date()
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val currentDateTimeString = sdf.format(d)
        holder.setTime(currentDateTimeString)
        if (message.getmType() == Messages.TYPE_MESSAGE_REMOTE) {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            holder.rlMessage.layoutParams = params

            val handler = Handler()
            val lastPosition = messageList.size - 1
            val layoutManager = chatrecyclerview?.layoutManager as LinearLayoutManager
            layoutManager.scrollToPosition(lastPosition)
            val runnable = object : Runnable {
                var index = 0
                override fun run() {
                    if (index < message.getmMessage().length) {
                        imgsend?.visibility = View.GONE
                        holder.txtmessage.append(message.getmMessage()[index].toString())
                        index++
                        handler.postDelayed(this, 100)


                    }else{
                        imgsend?.visibility = View.VISIBLE
                        chatrecyclerview?.post {
                            layoutManager.scrollToPosition(lastPosition)
                        }

                    }
                }
            }
            handler.post(runnable)


            holder.imgshare.setOnClickListener{
                val appLink =  holder.txtmessage.text.toString()+"   \n _____open ChattyAI_____." // Replace with your app link
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, appLink)
                context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            }

//            holder.rlMessage.setBackground(context.getResources().getDrawable(R.drawable.strokeedittextcorner))

        } else if (message.getmType() == Messages.TYPE_MESSAGE) {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            holder.rlMessage.layoutParams = params
            holder.setMessage(message.getmMessage())

//            holder.rlMessage.setBackground(context.getResources().getDrawable(R.drawable.strokeedittextcorner))

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return messageList[position].getmType()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val txtmessage: TextView = itemView.findViewById(R.id.txtmessage)
         val rlMessage: RelativeLayout = itemView.findViewById(R.id.rlMessage)
         val txtTime: TextView = itemView.findViewById(R.id.txtTime)
         val imgshare: ImageView = itemView.findViewById(R.id.imgshare)

        fun setTime(time: String) {
            if (txtTime == null) return
            txtTime.text = time
        }

        fun setMessage(message: String) {
            if (txtmessage == null) return
            txtmessage.text = message
        }
         fun scrollToBottom() {
            chatrecyclerview?.postDelayed({
                chatrecyclerview.smoothScrollToPosition(messageList.size - 1)
            }, 100)
        }
    }
}
