package com.fsoc.sheathcare.presentation.main.detail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * The common adapter helps you implement expandable recycler easier.
 * SH: The View Holder you want to use as the Header. Your header view holder must implement class CommonSectionHeaderViewHolder
 * CH: The View Holder you want to use as the Child. Your child view holder must implement class CommonSectionChildViewHolder
 */
abstract class CommonExpandableAdapter<SH : CommonSectionHeaderViewHolder, CH : CommonSectionChildViewHolder>() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var mSectionsExpanded: ArrayList<Boolean>? = null

    private var mExpandEnable: Boolean = true

    private var mIsOpenAtFirst: Boolean = true

    private var positionDisabledClick = -1

    companion object {
        const val SECTION = 1001
        const val CONTENT = 1002
        const val LOADING = 1003
    }

    constructor(mIsOpenAtFirst: Boolean) : this() {
        this.mIsOpenAtFirst = mIsOpenAtFirst
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SECTION) {
            getSectionViewHolder(parent)
                .setOnClickSectionListener(this::onClickSection)
        } else {
            getChildViewHolder(parent, viewType)
                .setOnClickChildListener(this::onClickChild)
        }
    }

    override fun getItemCount(): Int {
        var itemCount = getSectionCollapsibleCount()
        (0 until getSectionCollapsibleCount()).forEach { section ->
            itemCount += getChildOfSectionCollapsibleCount(section)
        }
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        if (getSectionCollapsibleCount() == 0) return getChildTypeWithCheck(0, 0)

        val (sectionIndex, childIndex) = getSectionIndexAndChildIndex(position)

        return if (position in getAllSectionPosition()) {
            SECTION
        } else {
            getChildTypeWithCheck(sectionIndex, childIndex)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (sectionIndex, childIndex) = getSectionIndexAndChildIndex(position)
        if (holder is CommonSectionHeaderViewHolder) {
            holder.sectionIndex = sectionIndex
            onBindSectionViewHolder(holder as SH, sectionIndex)
            holder.onSectionExpandedChanged(sectionIndex, mSectionsExpanded!![sectionIndex])
        } else if (holder is CommonSectionChildViewHolder) {
            holder.sectionIndex = sectionIndex
            holder.childIndex = childIndex
            onBindChildViewHolder(
                holder as CH,
                sectionIndex,
                childIndex,
                getChildTypeWithCheck(sectionIndex, childIndex)
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        val (sectionIndex, childIndex) = getSectionIndexAndChildIndex(position)
        if (holder is CommonSectionHeaderViewHolder) {
            holder.sectionIndex = sectionIndex
            onBindSectionViewHolderPayloads(holder as SH, sectionIndex, payloads)
        } else if (holder is CommonSectionChildViewHolder) {
            holder.sectionIndex = sectionIndex
            holder.childIndex = childIndex
            onBindChildViewHolderPayloads(
                holder as CH,
                sectionIndex,
                childIndex,
                getChildTypeWithCheck(sectionIndex, childIndex),
                payloads
            )
        }
    }

    open fun onBindSectionViewHolderPayloads(
        sectionHolder: SH,
        sectionIndex: Int,
        payloads: MutableList<Any>
    ) {

    }

    open fun onBindChildViewHolderPayloads(
        childHolder: CH,
        sectionIndex: Int,
        childIndex: Int,
        childType: Int,
        payloads: MutableList<Any>
    ) {

    }

    internal fun getSectionIndexAndChildIndex(position: Int): Pair<Int, Int> {
        val allSectionPosition = getAllSectionPosition()
        return if (position in allSectionPosition) {
            Pair(
                allSectionPosition.indexOfLast { sectionPosition -> position == sectionPosition },
                -1
            )
        } else {
            val sectionIndexOfPosition =
                allSectionPosition.indexOfLast { sectionPosition -> position >= sectionPosition }
            val sectionChildPosition = position - allSectionPosition[sectionIndexOfPosition] - 1
            Pair(sectionIndexOfPosition, sectionChildPosition)
        }
    }

    internal fun getSectionIndex(position: Int): Int {
        val allSectionPosition = getAllSectionPosition()
        return if (position in allSectionPosition) {
            allSectionPosition.indexOfLast { sectionPosition -> position == sectionPosition }
        } else {
            val sectionIndexOfPosition =
                allSectionPosition.indexOfLast { sectionPosition -> position >= sectionPosition }
            sectionIndexOfPosition
        }
    }

    protected fun getAllSectionPosition(): Array<Int> {
        if (getSectionCollapsibleCount() == 0) return arrayOf()

        val sectionIndex = arrayListOf<Int>()
        sectionIndex.add(0)

        (1 until getSectionCollapsibleCount()).forEach { section ->
            sectionIndex.add(getChildCountBeforeSection(section) + section)
        }
        return sectionIndex.toTypedArray()
    }

    private fun getChildCountBeforeSection(sectionIndex: Int): Int {
        var childCount = 0
        (0 until sectionIndex).forEach { s ->
            childCount += getChildOfSectionCollapsibleCount(s)
        }
        return childCount
    }

    fun onClickSection(holder: CommonSectionHeaderViewHolder?, position: Int) {
        if (positionDisabledClick == position) return
        val sectionIndex = getSectionIndex(position)

        if (mExpandEnable) {
            val newState = !mSectionsExpanded!![sectionIndex]
            mSectionsExpanded!![sectionIndex] = newState

            holder?.onSectionExpandedChanged(sectionIndex, newState)
            expandOrCollapseSection(newState, sectionIndex)
            onSectionStateChanged(sectionIndex, newState)
        } else {
            holder?.onSectionExpandedChanged(sectionIndex, true)
            onSectionStateChanged(sectionIndex, true)
        }
    }

    protected fun onClickChild(holder: CommonSectionChildViewHolder, position: Int) {
        //#13460
        if (position == RecyclerView.NO_POSITION) {
            return
        }
        val (sectionIndex, childIndex) = getSectionIndexAndChildIndex(position)
        holder.onChildClicked(sectionIndex, childIndex)
    }

    private fun expandOrCollapseSection(expand: Boolean, sectionIndex: Int) {
        val sectionPosition = getAllSectionPosition()[sectionIndex]
        if (expand) {
            notifyItemRangeInserted(sectionPosition + 1, getChildOfSection(sectionIndex))
        } else {
            notifyItemRangeRemoved(sectionPosition + 1, getChildOfSection(sectionIndex))
        }
    }

    protected fun getChildOfSectionCollapsibleCount(section: Int): Int {
        return if (mSectionsExpanded!![section]) {
            getChildOfSection(section)
        } else {
            0
        }
    }

    private fun getSectionCollapsibleCount(): Int {
        val count = getSectionCount()
        if (mSectionsExpanded == null || mSectionsExpanded!!.size != count) {
            initSectionsExpanded(count)
        }
        return count
    }

    protected fun initSectionsExpanded(count: Int) {
        mSectionsExpanded = ArrayList()
        for (i in 0..count - 1) {
            mSectionsExpanded?.add(mIsOpenAtFirst)
        }
    }

    fun notifyItemInserted(sectionIndex: Int, childIndex: Int) {
        notifyItemRangeInserted(sectionIndex, childIndex, 1)
    }

    fun notifyItemChanged(sectionIndex: Int, childIndex: Int, payloads: Any?) {
        notifyItemRangeChanged(sectionIndex, childIndex, 1, payloads)
    }

    fun notifyItemRemoved(sectionIndex: Int, childIndex: Int) {
        notifyItemRangeRemoved(sectionIndex, childIndex, 1)
    }

    fun notifyItemRangeInserted(sectionIndex: Int, childStartIndex: Int, itemCount: Int) {
        if (mSectionsExpanded!![sectionIndex]) {
            val sectionPosition = getAllSectionPosition()[sectionIndex]
            notifyItemRangeInserted(sectionPosition + childStartIndex + 1, itemCount)
        }
    }

    fun notifyItemRangeChanged(
        sectionIndex: Int,
        childStartIndex: Int,
        itemCount: Int,
        payloads: Any?
    ) {
        if (mSectionsExpanded!![sectionIndex]) {
            val sectionPosition = getAllSectionPosition()[sectionIndex]
            if (payloads != null) {
                notifyItemRangeChanged(sectionPosition + childStartIndex + 1, itemCount, payloads)
            } else {
                notifyItemRangeChanged(sectionPosition + childStartIndex + 1, itemCount)
            }
        }
    }

    fun notifyItemRangeRemoved(sectionIndex: Int, childStartIndex: Int, itemCount: Int) {
        if (mSectionsExpanded!![sectionIndex]) {
            val sectionPosition = getAllSectionPosition()[sectionIndex]
            notifyItemRangeRemoved(sectionPosition + childStartIndex + 1, itemCount)
        }
    }

    protected fun getChildTypeWithCheck(sectionIndex: Int, childIndex: Int): Int {
        val childType = getChildType(sectionIndex, childIndex)
        if (childType == SECTION) throw IllegalStateException("Please don't use $childType as a type value of child view holder")
        return childType
    }

    fun setEnableExpand(enable: Boolean) {
        mExpandEnable = enable
    }

    fun setExpandAll() {
        mSectionsExpanded?.let {
            for (i in 0 until it.size) {
                it[i] = true
            }
        }
    }

    fun setDisabledClickSection(position: Int) {
        positionDisabledClick = position
    }

    /**
     * Return your child type view holder
     */
    open fun getChildType(sectionIndex: Int, childIndex: Int): Int = CONTENT

    /**
     * Called when the state of a section is changed
     */
    open fun onSectionStateChanged(sectionIndex: Int, newState: Boolean) = Unit

    /**
     * Create your section view holder and return it
     */
    abstract fun getSectionViewHolder(parent: ViewGroup): SH

    /**
     * Create your child view holder and return it
     */
    abstract fun getChildViewHolder(parent: ViewGroup, childType: Int): CH

    /**
     * Define how many section you want
     */
    abstract fun getSectionCount(): Int

    /**
     * Define how many child in the section
     * @param section: the section index
     */
    abstract fun getChildOfSection(section: Int): Int

    /**
     * Bind data on your section view holder
     * @param sectionIndex: The section index must be bind data
     */
    abstract fun onBindSectionViewHolder(sectionHolder: SH, sectionIndex: Int)


    /**
     * Bind data on your child view holder
     * @param sectionIndex: The section index of child view holder
     * @param childIndex: The child index of section index must be bind data
     */
    abstract fun onBindChildViewHolder(
        childHolder: CH,
        sectionIndex: Int,
        childIndex: Int,
        childType: Int
    )
}
