package com.fsoc.sheathcare.presentation.main.home

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
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
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter

    class VideoPlayerRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : RecyclerView(context, attrs, defStyleAttr) {

        enum class VolumeState {
            ON, OFF
        }

        // ui
        private lateinit var thumbnail: ImageView
        private lateinit var volumeControl: ImageView
        private lateinit var progressBar: ProgressBar
        private lateinit var viewHolderParent: View
        private lateinit var frameLayout: FrameLayout
        private var videoSurfaceView: PlayerView
        private var videoPlayer: SimpleExoPlayer

        // vars
//        private var mediaObjects = ArrayList<MediaObject>()
        private var videoSurfaceDefaultHeight = 0
        private var screenDefaultHeight = 0

        //    private val context: Context? = null
        private var playPosition = -1
        private var isVideoViewAdded = false
        private var requestManager: RequestManager? = null

        // controlling playback state
        private var volumeState: VolumeState? = null


        init {
            val display =
                (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
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

            setVolumeControl(VolumeState.ON)

            addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {}
                override fun onChildViewDetachedFromWindow(view: View) {

                    if (this@VideoPlayerRecyclerView::viewHolderParent.isInitialized && viewHolderParent == view) {
                        resetVideoView()
                    }
                }
            })

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
                            Log.e(TAG, "onPlayerStateChanged: Buffering video.")
                            progressBar.visibility = View.VISIBLE
                        }
                        Player.STATE_ENDED -> {
                            Log.d(TAG, "onPlayerStateChanged: Video ended.")
                            videoPlayer.seekTo(0)
                        }
                        Player.STATE_IDLE -> {
                        }
                        Player.STATE_READY -> {
                            Log.e(TAG, "onPlayerStateChanged: Ready to play.")
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


        private val videoViewClickListener = OnClickListener { toggleVolume() }

        private fun getVisibleVideoSurfaceHeight(playPosition: Int): Int {

            val at =
                playPosition - (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            Log.d(TAG, "getVisibleVideoSurfaceHeight: at: $at")

            val child = getChildAt(at) ?: return 0

            val location = IntArray(2)
            child.getLocationInWindow(location)

            return if (location[1] < 0) {
                location[1] + videoSurfaceDefaultHeight
            } else {
                screenDefaultHeight - location[1]
            }
        }

        // Remove the old player
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

        private fun addVideoView() {
            frameLayout.addView(videoSurfaceView)
            isVideoViewAdded = true
            videoSurfaceView.requestFocus()
            videoSurfaceView.visibility = View.VISIBLE
            videoSurfaceView.alpha = 1f
            thumbnail.visibility = View.GONE
        }

        private fun resetVideoView() {
            if (isVideoViewAdded) {
                removeVideoView(videoSurfaceView)
                playPosition = -1
                videoSurfaceView.visibility = View.INVISIBLE
                thumbnail.visibility = View.VISIBLE
            }
        }


        fun releasePlayer() {
            videoPlayer.release()
        }

        private fun toggleVolume() {
            if (volumeState === VolumeState.OFF) {
                Log.d(TAG, "togglePlaybackState: enabling volume.")
                setVolumeControl(VolumeState.ON)
            } else if (volumeState === VolumeState.ON) {
                Log.d(TAG, "togglePlaybackState: disabling volume.")
                setVolumeControl(VolumeState.OFF)
            }
        }

        private fun setVolumeControl(state: VolumeState) {
            volumeState = state
            if (state === VolumeState.OFF) {
                if (this::volumeControl.isInitialized) {
                    videoPlayer.volume = 0f
                    animateVolumeControl()
                }
            } else if (state === VolumeState.ON) {
                if (this::volumeControl.isInitialized) {
                    videoPlayer.volume = 1f
                    animateVolumeControl()
                }
            }
        }

        private fun animateVolumeControl() {
            volumeControl.bringToFront()
            if (volumeState === VolumeState.OFF) {
                requestManager?.load(R.drawable.exo_controls_pause)
                    ?.into(volumeControl)
            } else if (volumeState === VolumeState.ON) {
                requestManager?.load(R.drawable.exo_icon_fastforward)
                    ?.into(volumeControl)
            }
            volumeControl.animate().cancel()
            volumeControl.alpha = 1f
            volumeControl.animate()
                .alpha(0f)
                .setDuration(600).startDelay = 1000
        }

//        fun setMediaObjects(mediaObjects: List<MediaObject>) {
//            this.mediaObjects.addAll(mediaObjects)
//        }

        companion object {
            const val TAG = "VideoPlayerRecyclerView"
        }

    }