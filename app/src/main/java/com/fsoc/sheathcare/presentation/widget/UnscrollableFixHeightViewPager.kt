package com.fsoc.sheathcare.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.Transformation
import androidx.fragment.app.FragmentStatePagerAdapter

class UnscrollableFixHeightViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    companion object {
        const val DURATION = 200L
    }
    private var isPagingEnabled = true
    private var mAnimStarted = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return this.isPagingEnabled && super.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var maxHeight = heightMeasureSpec
        if (!mAnimStarted && null != adapter) {
            var height = 0
            val child =
                (adapter as FragmentStatePagerAdapter).getItem(currentItem).view
            if (child != null) {
                child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                height = child.measuredHeight
            }

            // Not the best place to put this animation, but it works pretty good.
            val newHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            if (!(layoutParams.height == 0 || heightMeasureSpec == newHeight)) {
                val targetHeight = height
                val currentHeight = layoutParams.height
                val heightChange = targetHeight - currentHeight
                val animation: Animation = object : Animation() {
                    override fun applyTransformation(
                        interpolatedTime: Float,
                        t: Transformation?
                    ) {
                        if (interpolatedTime >= 1) {
                            layoutParams.height = targetHeight
                        } else {
                            val stepHeight = (heightChange * interpolatedTime).toInt()
                            layoutParams.height = currentHeight + stepHeight
                        }
                        requestLayout()
                    }

                    override fun willChangeBounds(): Boolean {
                        return true
                    }
                }
                animation.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        mAnimStarted = true
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        mAnimStarted = false
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                animation.duration = DURATION
                startAnimation(animation)
                mAnimStarted = true
            } else {
                maxHeight = newHeight
            }
        }

        super.onMeasure(widthMeasureSpec, maxHeight)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event)
    }
}
