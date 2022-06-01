package com.fsoc.sheathcare.presentation.main.chat.adapter

import android.content.Context
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.domain.entity.TextMessageChat
import com.fsoc.sheathcare.presentation.main.chat.MessageItem
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_text_message.view.*

class TextMessageItem(val message: TextMessageChat,
                      val context: Context
)
    : MessageItem(message) {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_message_text.text = message.text
        super.bind(viewHolder, position)
    }

    override fun getLayout() = R.layout.item_text_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is TextMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? TextMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

}