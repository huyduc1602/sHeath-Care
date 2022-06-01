package com.fsoc.sheathcare.presentation.main.detail.infordapter

import android.view.View
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.domain.entity.CommentProduct
import com.fsoc.sheathcare.presentation.main.covidnews.adapter.BaseOrderAdapter
import com.ominext.healthy.common.extension.loadThumb
import kotlinx.android.synthetic.main.item_comment_product.view.*

class CommentAdapter(
    data: MutableList<CommentProduct>
) : BaseOrderAdapter<CommentProduct>(
    data,
    R.layout.item_comment_product,
    R.layout.item_comment_product
) {
    private var listComment = data
    override fun getViewType(position: Int): ViewType {
        return ViewType.CONTENT
    }

    override fun bindItemView(view: View, obj: CommentProduct) {
        view.apply {
            imgAvatarComment.loadThumb(obj.avatar)
            txtName.text = obj.seader
            txtCommentContent.text = obj.content
        }
    }

    override fun onItemClick(obj: CommentProduct) {

    }

    override fun onItemLongClick(obj: CommentProduct) {

    }

    fun updateData(commentProduct: CommentProduct){
        listComment.add(commentProduct)
        notifyItemInserted(listComment.size)
    }
}