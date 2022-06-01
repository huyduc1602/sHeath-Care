package com.fsoc.sheathcare.presentation.main.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.click
import com.fsoc.sheathcare.common.extension.hideKeyboard
import com.fsoc.sheathcare.common.extension.observe
import com.fsoc.sheathcare.common.extension.show
import com.fsoc.sheathcare.domain.entity.CommentProduct
import com.fsoc.sheathcare.domain.entity.User
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.detail.DetailInfoProductFragment.Companion.ID_COMMENT
import com.fsoc.sheathcare.presentation.main.detail.infordapter.CommentAdapter
import com.fsoc.sheathcare.presentation.main.home.SlideVideoViewModel
import com.google.firebase.firestore.FieldValue
import com.ominext.healthy.common.extension.loadThumb
import kotlinx.android.synthetic.main.fragment_comment_product.*

class CommentProductFragment : BaseFragment<SlideVideoViewModel>() {
    private lateinit var idComment: String
    private lateinit var user: User
    private var listComment = mutableListOf<CommentProduct>()
    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter(listComment)
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_comment_product
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(SlideVideoViewModel::class.java)
        idComment = arguments?.getString(ID_COMMENT) ?: ""
    }

    override fun setUpView() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Đánh giá sản phẩm"
        initList()
        initEvents()

    }

    private fun initEvents() {
        edtWriteComment.doAfterTextChanged {
            if (it.toString().isEmpty()) imgSend.show(false) else imgSend.show(true)
        }
        imgSend.click {
            val content = edtWriteComment.text.toString()
            val avatar = user.avatar ?: ""
            val timestamp = FieldValue.serverTimestamp()
            user.name?.let { name ->
                    viewModel.pushCommentProduct(
                        idComment,listComment.size.toString(), content, avatar, name, timestamp
                    )
            }
        }
    }

    private fun initList() {
        rvComment.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentAdapter
        }
    }

    override fun observable() {
        observe(viewModel.userProfile) { user ->
            this.user = user
            imgAvatarAdmin.loadThumb(user.avatar)
        }
        observe(viewModel.listComment) { listComment ->
            this.listComment.clear()
            this.listComment.addAll(listComment)
            rvComment.scrollToPosition(rvComment.adapter!!.itemCount - 1)
            commentAdapter.notifyDataSetChanged()
        }
        observe(viewModel.comment) { comment ->
            edtWriteComment.setText("")
            hideKeyboard()
//            commentAdapter.updateData(comment)

        }
    }

    override fun fireData() {
        viewModel.getUser()
        viewModel.getCommentProduct(idComment)
    }
}