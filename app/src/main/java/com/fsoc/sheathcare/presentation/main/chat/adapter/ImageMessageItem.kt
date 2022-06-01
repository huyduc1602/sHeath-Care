package com.fsoc.sheathcare.presentation.main.chat.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.domain.entity.ImageMessageChat
import com.fsoc.sheathcare.presentation.main.chat.MessageItem
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_image_message.*

class ImageMessageItem(val message: ImageMessageChat,
                       val context: Context
)
    : MessageItem(message) {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)
        Glide.with(context)
            .load(message.imagePath)
            .placeholder(R.drawable.image_default)
            .into(viewHolder.imageView_message_image)
    }

    override fun getLayout() = R.layout.item_image_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ImageMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ImageMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}