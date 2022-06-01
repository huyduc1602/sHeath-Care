package com.fsoc.sheathcare.presentation.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.domain.entity.VideoShow
import com.ominext.healthy.common.extension.loadImage
import kotlinx.android.synthetic.main.item_video_home.view.*

class VideoAdapter(
    var listItem: ArrayList<VideoShow>,
    private val requestManager: RequestManager,
    var context: Context
) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val media_container = view.findViewById<FrameLayout>(R.id.media_container)
        private val title = view.findViewById<TextView>(R.id.title)
        val thumbnail = view.findViewById<ImageView>(R.id.thumbnail)
        val volumeControl = view.findViewById<ImageView>(R.id.volume_control)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        private var parent = view.findViewById<View>(R.id.parent)
        var requestManager: RequestManager? = null
        fun bind(item: VideoShow, requestManager: RequestManager) {
            parent.tag = this
            itemView.txtTitleVideo.text = item.title
            itemView.txtCreateAt.text = item.createAt
//            itemView.thumbnail.loadImage(item.thumbnail)
            requestManager.load(item.thumbnail).into(itemView.thumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_video_home, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listItem[position], requestManager)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    fun getData(): List<VideoShow> {
        return listItem
    }

    fun setData(listVideo: List<VideoShow>) {
        listItem = listVideo as ArrayList<VideoShow>
        notifyDataSetChanged()
    }

}