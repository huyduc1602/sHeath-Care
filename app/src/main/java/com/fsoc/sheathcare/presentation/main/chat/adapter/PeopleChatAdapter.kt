package com.fsoc.sheathcare.presentation.main.chat.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.extension.isEven
import com.fsoc.sheathcare.domain.entity.Doctor
import com.fsoc.sheathcare.domain.entity.InfoProduct
import com.fsoc.sheathcare.domain.entity.User
import com.fsoc.sheathcare.presentation.main.MainActivity
import com.fsoc.sheathcare.presentation.main.covidnews.adapter.BaseOrderAdapter
import com.ominext.healthy.common.extension.loadThumb
import kotlinx.android.synthetic.main.item_doctor.view.*

class PeopleChatAdapter(
    data: MutableList<User>,
    private val itemClick: (obj: User) -> Unit
) : BaseOrderAdapter<User>(
    data,
    R.layout.item_doctor,
    R.layout.item_doctor
) {
    private var position: Int = 0
    override fun getViewType(position: Int): ViewType {
        return ViewType.CONTENT
    }

    override fun bindItemView(view: View, obj: User) {
        view.apply {
            when (MainActivity.user.type) {
                0 -> {
                    if (position.isEven()) {
                        imgAvatarDoctor.setImageDrawable(resources.getDrawable(R.drawable.doctor_male))
                    } else {
                        imgAvatarDoctor.setImageDrawable(resources.getDrawable(R.drawable.doctor_female))
                    }
                }
                else -> {
                    imgAvatarDoctor.loadThumb(obj.avatar)
                }
            }
            txtSpecialize.text = obj.name
        }
    }

    override fun onItemClick(obj: User) {
        itemClick.invoke(obj)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        this.position = position
    }

    override fun onItemLongClick(obj: User) {

    }

}