package com.fsoc.sheathcare.presentation.main.detail.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.domain.entity.ProductCategoryModel
import com.fsoc.sheathcare.presentation.widget.StickyHeaderItemDecoration


class PharmacyNewsStickyAdapter(
    private var mListPharmacyNews: ArrayList<ProductCategoryModel>,
    private val onItemClicked: ((position: String) -> Unit)? = null
) : CommonExpandableAdapter<PharmacyNewsSeactionHeaderHolder,
        PharmacyNewsSeactionChildHolder>(),
    StickyHeaderItemDecoration.StickyHeaderInterface<PharmacyNewsSeactionHeaderStickyHolder> {

    private var hasLoadingItem: Boolean = false
    var isRefresh: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SECTION -> {
                getSectionViewHolder(parent)
                    .setOnClickSectionListener(this::onClickSection)
            }
            LOADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.listitem_loading, parent, false)
                LoadingViewHolder(view)
            }
            else -> {
                getChildViewHolder(parent, viewType)
                    .setOnClickChildListener(this::onClickChild)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getSectionCollapsibleCount() == 0) return getChildTypeWithCheck(0, 0)

        val (sectionIndex, childIndex) = getSectionIndexAndChildIndex(position)

        return if (position in getAllSectionPosition()) {
            SECTION
        } else {
            if (position == itemCount - 1 && hasLoadingItem) {
                LOADING
            } else {
                getChildTypeWithCheck(sectionIndex, childIndex)
            }
        }
    }

    override fun getItemCount(): Int {
        var itemCount = getSectionCollapsibleCount()
        (0 until getSectionCollapsibleCount()).forEach { section ->
            itemCount += getChildOfSectionCollapsibleCount(section)
        }
        return if (hasLoadingItem) {
            itemCount + 1
        } else {
            itemCount
        }
    }

    override fun getSectionViewHolder(parent: ViewGroup): PharmacyNewsSeactionHeaderHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group_pharmacy_news, parent, false)
        return PharmacyNewsSeactionHeaderHolder(view)
    }

    override fun getChildViewHolder(
        parent: ViewGroup,
        childType: Int
    ): PharmacyNewsSeactionChildHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pharmacy_news, parent, false)
        return PharmacyNewsSeactionChildHolder(
            itemView
        )
    }

    override fun getSectionCount(): Int = mListPharmacyNews.size

    override fun getChildOfSection(section: Int): Int {
        return mListPharmacyNews[section].categoryLists.size
    }

    override fun onBindSectionViewHolder(
        sectionHolder: PharmacyNewsSeactionHeaderHolder,
        sectionIndex: Int
    ) {
        if (mListPharmacyNews.isEmpty()) return
        val pharmacyNewsItem = mListPharmacyNews[sectionIndex]
        sectionHolder.onBind(pharmacyNewsItem)
    }

    override fun onBindChildViewHolder(
        childHolder: PharmacyNewsSeactionChildHolder,
        sectionIndex: Int,
        childIndex: Int,
        childType: Int
    ) {
        if (mListPharmacyNews.isEmpty()) return
        val pharmacyNewsItem = mListPharmacyNews[sectionIndex]
        val meetingVisitor = pharmacyNewsItem.categoryLists[childIndex]
        childHolder.onBind(meetingVisitor, sectionIndex)
        childHolder.itemView.setOnClickListener {
            onItemClicked?.invoke("${pharmacyNewsItem.key}$childIndex")
        }
    }

    override fun onSectionStateChanged(sectionIndex: Int, newState: Boolean) {
//        onSectionStateChanged?.invoke(sectionIndex)
    }

    //region implement StickyHeaderInterface
    override fun onCreateHeader(
        parent: androidx.recyclerview.widget.RecyclerView,
        headerPosition: Int
    ): PharmacyNewsSeactionHeaderStickyHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group_pharmacy_news, parent, false)
        return PharmacyNewsSeactionHeaderStickyHolder(view)
    }

    override fun onBindHeader(header: PharmacyNewsSeactionHeaderStickyHolder, headerPosition: Int) {
        if (mListPharmacyNews.isEmpty()) return
        val sectionIndex = getSectionIndex(headerPosition)
        val expandState = mSectionsExpanded?.get(sectionIndex) ?: false
        val pharmacyNewsItem = mListPharmacyNews[sectionIndex]
        header.onBind(pharmacyNewsItem, expandState)
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return getItemViewType(itemPosition) == SECTION
    }

    fun showLoadingItem(isLoading: Boolean) {
        hasLoadingItem = isLoading
        if (hasLoadingItem) {
            notifyItemInserted(itemCount)
        } else {
            notifyItemRemoved(itemCount + 1)
        }
    }

    //endregion implement StickyHeaderInterface

    fun hasLastSectionExpand(): Boolean {
        if (mSectionsExpanded.isNullOrEmpty()) {
            return false
        }
        return mSectionsExpanded?.last()?.not() ?: false
    }

    private fun getSectionCollapsibleCount(): Int {
        val count = getSectionCount()
        if (mSectionsExpanded == null) {
            initSectionsExpanded(count)
            return count
        }
        if (mSectionsExpanded!!.size < count) {
            if (isRefresh.not()) {
                for (i in mSectionsExpanded!!.size..count - 1) {
                    mSectionsExpanded?.add(true)
                }
            } else {
                initSectionsExpanded(count)
            }
        }
        return count
    }
}
