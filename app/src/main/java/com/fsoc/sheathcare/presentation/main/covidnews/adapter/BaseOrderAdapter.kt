package com.fsoc.sheathcare.presentation.main.covidnews.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fsoc.sheathcare.common.extension.click

abstract class BaseOrderAdapter<T>(
    private val data: List<T>,
    private val layoutHeadId: Int,
    private val layoutContentId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val HEAD = 0
        private const val CONTENT = 1
    }

    enum class ViewType {
        HEAD, CONTENT
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if (getViewType(position) == ViewType.HEAD) {
            HEAD
        } else {
            CONTENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            HEAD -> OrderViewHolder<T>(
                parent, layoutHeadId, this
            )
            else -> OrderViewHolder<T>(
                parent, layoutContentId, this
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OrderViewHolder<*>) {
            (holder as OrderViewHolder<T>).bindData(data[position])
        }
    }

    abstract fun getViewType(position: Int): ViewType
    abstract fun bindItemView(view: View, obj: T)
    abstract fun onItemClick(obj: T)
    abstract fun onItemLongClick(obj: T)

}

private class OrderViewHolder<T>(
    parent: ViewGroup,
    layoutID: Int,
    private val adapter: BaseOrderAdapter<T>
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(layoutID, parent, false)
) {
    fun bindData(obj: T) {
        itemView.apply {
            adapter.bindItemView(this, obj)
            click { adapter.onItemClick(obj) }
            setOnLongClickListener {
                adapter.onItemLongClick(obj)
                return@setOnLongClickListener true
            }
        }
    }
}

