package com.fsoc.sheathcare.presentation.main.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.observe
import com.fsoc.sheathcare.common.utils.FirestoreUtils
import com.fsoc.sheathcare.domain.entity.User
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.MainActivity
import com.fsoc.sheathcare.presentation.main.chat.adapter.PeopleChatAdapter
import com.fsoc.sheathcare.presentation.main.detail.DetailInfoProductFragment
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : BaseFragment<ChatViewModel>() {
    private var listData = mutableListOf<User>()
    private val type = MainActivity.user.type
    private val peopleChatAdapter: PeopleChatAdapter by lazy {
        PeopleChatAdapter(listData) { user ->
            clickItem(user)
        }
    }

    companion object {
        const val NAME = "NAME"
        const val EMAIL = "EMAIL"
        const val TOKEN = "TOKEN"
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_chat
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(activity ?: return, viewModelFactory)
            .get(ChatViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        FirestoreUtils.removeListener()
    }

    override fun setUpView() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Liên hệ"
        (activity as MainActivity).initView()
        initView()
    }

    private fun initView() {
        rvDoctor.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = peopleChatAdapter
        }
    }

    override fun observable() {
        if (type == 0) {
            observe(viewModel.listDoctor) {
                listData.clear()
                listData.addAll(it)
                peopleChatAdapter.notifyDataSetChanged()
            }
        } else {
            observe(viewModel.listUserChat) {
                listData.clear()
                if (it.isNotEmpty()) {
                    listData.addAll(it)
                    peopleChatAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun fireData() {
        if (type == 0) {
            viewModel.getListDoctor()
        } else {
            MainActivity.user.email?.let {
                viewModel.getListUserChat(it)
            }
        }
//        viewModel.checkAppExpire()
    }

    private fun clickItem(obj: User) {
        val arg = Bundle().apply {
            putString(NAME, obj.name)
            putString(EMAIL, obj.email)
            putString(TOKEN, obj.token)
        }
        navigatePresent(R.id.chatDetailFragment, arg)
    }
}