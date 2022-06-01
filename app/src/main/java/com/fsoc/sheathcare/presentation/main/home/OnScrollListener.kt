package com.fsoc.sheathcare.presentation.main.home

import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.presentation.adapter.VideoAdapter
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class OnScrollListener(
    private val context: Context
) : RecyclerView.OnScrollListener() {

    private lateinit var thumbnail: ImageView
    private lateinit var volumeControl: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var frameLayout: FrameLayout
    private lateinit var viewHolderParent: View


    private var videoSurfaceView: PlayerView
    private var videoPlayer: SimpleExoPlayer
    private var requestManager: RequestManager? = null


    private var isVideoViewAdded = false
    private var playPosition = -1
    private var videoSurfaceDefaultHeight = 0
    private var screenDefaultHeight = 0
    private var volumeState: VideoPlayerRecyclerView.VolumeState? = null


    init {
        val display =
            (context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val point = Point()
        display.getSize(point)
        videoSurfaceDefaultHeight = point.x
        screenDefaultHeight = point.y

        videoSurfaceView = PlayerView(context)
        videoSurfaceView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        // 2. Create the player
        videoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        // Bind the player to the view.
        videoSurfaceView.useController = false
        videoSurfaceView.player = videoPlayer

        setVolumeControl(VideoPlayerRecyclerView.VolumeState.ON)

        videoPlayer.addListener(object : Player.EventListener {
            override fun onTimelineChanged(
                timeline: Timeline,
                @Nullable manifest: Any?,
                reason: Int
            ) {
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
            }

            override fun onLoadingChanged(isLoading: Boolean) {}

            override fun onPlayerStateChanged(
                playWhenReady: Boolean,
                playbackState: Int
            ) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        Log.e(VideoPlayerRecyclerView.TAG, "onPlayerStateChanged: Buffering video.")
                        progressBar.visibility = View.VISIBLE
                    }
                    Player.STATE_ENDED -> {
                        Log.d(VideoPlayerRecyclerView.TAG, "onPlayerStateChanged: Video ended.")
                        videoPlayer.seekTo(0)
                    }
                    Player.STATE_IDLE -> {
                    }
                    Player.STATE_READY -> {
                        Log.e(VideoPlayerRecyclerView.TAG, "onPlayerStateChanged: Ready to play.")
                        progressBar.visibility = View.GONE
                        if (!isVideoViewAdded) {
                            addVideoView()
                        }
                    }
                    else -> {
                    }
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {}
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
            override fun onPlayerError(error: ExoPlaybackException) {}
            override fun onPositionDiscontinuity(reason: Int) {}
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
            override fun onSeekProcessed() {}
        })
    }

    private fun addVideoView() {
        frameLayout.addView(videoSurfaceView)
        isVideoViewAdded = true
        videoSurfaceView.requestFocus()
        videoSurfaceView.visibility = View.VISIBLE
        videoSurfaceView.alpha = 1f
        thumbnail.visibility = View.GONE
    }

    private fun removeVideoView(videoView: PlayerView) {
        val viewGroupParent = videoView.parent ?: return
        val parent = viewGroupParent as ViewGroup
        val index: Int = parent.indexOfChild(videoView)
        if (index >= 0) {
            parent.removeViewAt(index)
            isVideoViewAdded = false
            viewHolderParent.setOnClickListener(null)
        }
    }


    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            Log.d(VideoPlayerRecyclerView.TAG, "onScrollStateChanged: called.")
            if (this::thumbnail.isInitialized)
                thumbnail.visibility = View.VISIBLE

            // There's a special case when the end of the list has been reached.
            // Need to handle that with this bit of logic
            if (!recyclerView.canScrollVertically(1)) {
                playVideo(true, recyclerView)
            } else {
                playVideo(false, recyclerView)
            }
        }
    }

    private fun playVideo(isEndOfList: Boolean, recyclerView: RecyclerView) {
        val targetPosition: Int

        if (!isEndOfList) {
            val startPosition =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            var endPosition =
                (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            // if there is more than 2 list-items on the screen, set the difference to be 1
            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1
            }

            // something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return
            }

            // if there is more than 1 list-item on the screen
            targetPosition = if (startPosition != endPosition) {
                val startPositionVideoHeight: Int =
                    getVisibleVideoSurfaceHeight(startPosition, recyclerView)
                val endPositionVideoHeight: Int =
                    getVisibleVideoSurfaceHeight(endPosition, recyclerView)
                if (startPositionVideoHeight > endPositionVideoHeight) startPosition else endPosition
            } else {
                startPosition
            }
        } else {
            targetPosition = (recyclerView.adapter as VideoAdapter).getData().size - 1
        }

        Log.d(VideoPlayerRecyclerView.TAG, "playVideo: target position: $targetPosition")

        // video is already playing so return
        if (targetPosition == playPosition) {
            return
        }

        // set the position of the list-item that is to be played
        playPosition = targetPosition

        // remove any old surface views from previously playing videos
        videoSurfaceView.visibility = View.INVISIBLE
        removeVideoView(videoSurfaceView)

        val currentPosition =
            targetPosition - (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        val child = recyclerView.getChildAt(currentPosition) ?: return

        val holder = child.tag as VideoAdapter.ViewHolder
        thumbnail = holder.thumbnail
        progressBar = holder.progressBar
        volumeControl = holder.volumeControl
        viewHolderParent = holder.itemView
        requestManager = holder.requestManager
        frameLayout = holder.media_container

        videoSurfaceView.player = videoPlayer

        viewHolderParent.setOnClickListener(videoViewClickListener)

        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            context, Util.getUserAgent(context, "RecyclerView VideoPlayer")
        )
        val mediaUrl = (recyclerView.adapter as VideoAdapter).getData()[targetPosition].urlVideo
        val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(mediaUrl))
        videoPlayer.prepare(videoSource)
        videoPlayer.playWhenReady = true
    }

    private val videoViewClickListener = View.OnClickListener { toggleVolume() }

    private fun toggleVolume() {
        if (volumeState === VideoPlayerRecyclerView.VolumeState.OFF) {
            Log.d(VideoPlayerRecyclerView.TAG, "togglePlaybackState: enabling volume.")
            setVolumeControl(VideoPlayerRecyclerView.VolumeState.ON)
        } else if (volumeState === VideoPlayerRecyclerView.VolumeState.ON) {
            Log.d(VideoPlayerRecyclerView.TAG, "togglePlaybackState: disabling volume.")
            setVolumeControl(VideoPlayerRecyclerView.VolumeState.OFF)
        }
    }


    private fun getVisibleVideoSurfaceHeight(playPosition: Int, recyclerView: RecyclerView): Int {

        val at =
            playPosition - (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        Log.d(VideoPlayerRecyclerView.TAG, "getVisibleVideoSurfaceHeight: at: $at")

        val child = recyclerView.getChildAt(at) ?: return 0

        val location = IntArray(2)
        child.getLocationInWindow(location)

        return if (location[1] < 0) {
            location[1] + videoSurfaceDefaultHeight
        } else {
            screenDefaultHeight - location[1]
        }
    }

    private fun setVolumeControl(state: VideoPlayerRecyclerView.VolumeState) {
        volumeState = state
        if (state === VideoPlayerRecyclerView.VolumeState.OFF) {
            if (this::volumeControl.isInitialized) {
                videoPlayer.volume = 0f
                animateVolumeControl()
            }
        } else if (state === VideoPlayerRecyclerView.VolumeState.ON) {
            if (this::volumeControl.isInitialized) {
                videoPlayer.volume = 1f
                animateVolumeControl()
            }
        }
    }

    private fun animateVolumeControl() {
        volumeControl.bringToFront()
        if (volumeState === VideoPlayerRecyclerView.VolumeState.OFF) {
            requestManager?.load(R.drawable.exo_controls_pause)
                ?.into(volumeControl)
        } else if (volumeState === VideoPlayerRecyclerView.VolumeState.ON) {
            requestManager?.load(R.drawable.exo_icon_fastforward)
                ?.into(volumeControl)
        }
        volumeControl.animate().cancel()
        volumeControl.alpha = 1f
        volumeControl.animate()
            .alpha(0f)
            .setDuration(600).startDelay = 1000
    }

    fun resetVideoView(view: View) {
        if (this::viewHolderParent.isInitialized && viewHolderParent == view && isVideoViewAdded) {
            removeVideoView(videoSurfaceView)
            playPosition = -1
            videoSurfaceView.visibility = View.INVISIBLE
            thumbnail.visibility = View.VISIBLE
        }
    }

    fun releasePlayer() {
        videoPlayer.playWhenReady = false
        videoPlayer.stop()
        videoPlayer.release()
    }

}