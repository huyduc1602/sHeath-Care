package com.fsoc.sheathcare.presentation.main.chat

import android.view.Gravity
import android.widget.FrameLayout
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.extension.backgroundResource
import com.fsoc.sheathcare.domain.entity.MessageChat
import com.fsoc.sheathcare.presentation.main.MainActivity
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_text_message.view.*
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat

abstract class MessageItem(private val messageChat: MessageChat) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        setTimeText(viewHolder)
        setMessageRootGravity(viewHolder)
    }

    private fun setTimeText(viewHolder: ViewHolder) {
        val dateFormat = SimpleDateFormat
            .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        viewHolder.itemView.textView_message_time.text = dateFormat.format(messageChat.time)
    }

    private fun setMessageRootGravity(viewHolder: ViewHolder) {
        if (messageChat.senderId == MainActivity.user.email) {
            viewHolder.itemView.message_root.apply {
                backgroundResource = R.drawable.rect_round_white
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                this.layoutParams = lParams
            }

        } else {
            viewHolder.itemView.message_root.apply {
                backgroundResource = R.drawable.rect_round_primary_color
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = lParams
            }
        }
    }
}