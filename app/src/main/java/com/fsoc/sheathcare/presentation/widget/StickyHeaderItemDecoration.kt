package com.fsoc.sheathcare.presentation.widget

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class StickyHeaderItemDecoration<VH : StickyHeaderItemDecoration.StickyHeaderViewHolder>(private val listener: StickyHeaderInterface<VH>) : RecyclerView.ItemDecoration() {

    private var viewHolder: VH? = null

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val topChild = parent.findChildViewUnder(0F, 0F) ?: return

        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }

        getHeaderViewForItem(topChildPosition, parent)
        val contactPoint = viewHolder?.view?.bottom ?: -1
        if (contactPoint == -1) {
            return
        }

        val childInContact = getChildInContact(parent, contactPoint)
        val childInContactPosition = parent.getChildAdapterPosition(childInContact!!)

        if (childInContact != null && childInContactPosition != RecyclerView.NO_POSITION && listener.isHeader(childInContactPosition)) {
            moveHeader(c, viewHolder?.view!!, childInContact)
            return
        }

        drawHeader(c, viewHolder?.view!!)
    }

    private fun getHeaderViewForItem(itemPosition: Int, parent: RecyclerView) {
        val headerPosition = listener.getHeaderPositionForItem(itemPosition)
        if (viewHolder == null) {
            viewHolder = listener.onCreateHeader(parent, headerPosition)
            fixLayoutSize(parent, viewHolder!!.view!!)
        }
        listener.onBindHeader(viewHolder!!, headerPosition)
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0F, 0F)
        header.draw(c)
        c.restore()
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View?) {
        c.save()
        c.translate(0F, (nextHeader!!.top - currentHeader.height).toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child.bottom > contactPoint) {
                if (child.top <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    /**
     * Properly measures and layouts the top sticky header.
     * @param parent ViewGroup: RecyclerView in this case.
     */
    private fun fixLayoutSize(parent: ViewGroup, view: View) {

        // Specs for parent (RecyclerView)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // Specs for children (headers)
        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)

        view.measure(childWidthSpec, childHeightSpec)

        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    open class StickyHeaderViewHolder(var view: View? = null)

    interface StickyHeaderInterface<VH : StickyHeaderViewHolder> {

        /**
         * This method gets called by [StickyHeaderItemDecoration] to fetch the position of the header item in the adapter
         * that is used for (represents) item at specified position.
         * @param itemPosition int. Adapter's position of the item for which to do the search of the position of the header item.
         * @return int. Position of the header item in the adapter.
         */
        fun getHeaderPositionForItem(itemPosition: Int): Int {
            var headerPosition = 0
            var temp = itemPosition
            do {
                if (this.isHeader(temp)) {
                    headerPosition = temp
                    break
                }
                temp -= 1
            } while (temp >= 0)
            return headerPosition
        }

        /**
         * This method gets called by [StickyHeaderItemDecoration] to create viewholder for header
         *
         * @param headerPosition int. The position of header
         */
        fun onCreateHeader(parent: RecyclerView, headerPosition: Int): VH

        /**
         * This method gets called by [StickyHeaderItemDecoration] to setup the header View.
         * @param header StickyHeaderViewHolder. Header to set the data on.
         * @param headerPosition int. Position of the header item in the adapter.
         */
        fun onBindHeader(header: VH, headerPosition: Int)

        /**
         * This method gets called by [StickyHeaderItemDecoration] to verify whether the item represents a header.
         * @param itemPosition int.
         * @return true, if item at the specified adapter's position represents a header.
         */
        fun isHeader(itemPosition: Int): Boolean
    }
}