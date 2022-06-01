package com.fsoc.sheathcare.presentation.main.chat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.click
import com.fsoc.sheathcare.common.extension.show
import com.fsoc.sheathcare.common.utils.CameraUtils
import com.fsoc.sheathcare.common.utils.FirestoreUtils
import com.fsoc.sheathcare.domain.entity.ImageMessageChat
import com.fsoc.sheathcare.domain.entity.Message
import com.fsoc.sheathcare.domain.entity.Notification
import com.fsoc.sheathcare.domain.entity.TextMessageChat
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.MainActivity
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_sub_chat.*
import java.io.ByteArrayOutputStream
import java.util.*

class ChatDetailFragment : BaseFragment<ChatViewModel>() {
    private lateinit var otherUserId: String
    private lateinit var currentChannelId: String
    private lateinit var token: String
    private lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section
    private val RC_SELECT_IMAGE = 2
    private lateinit var emailCurrent: String
    private lateinit var nameCurrent: String
    private lateinit var nameChat : String


    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_sub_chat
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FirestoreUtils.removeListener(messagesListenerRegistration)
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(activity ?: return, viewModelFactory)
            .get(ChatViewModel::class.java)
    }

    override fun setUpView() {
        initView()
        initEvents()
    }

    private fun initEvents() {

    }

    private fun initView() {
        editText_message.doAfterTextChanged {
            if (!it.toString().isEmpty()) {
                imageView_send.show(true)
            }
        }
        otherUserId = arguments?.getString(ChatFragment.EMAIL) ?: ""
        token = arguments?.getString(ChatFragment.TOKEN) ?: ""
        nameChat = arguments?.getString(ChatFragment.NAME) ?: ""
        (activity as? AppCompatActivity)?.supportActionBar?.title = nameChat
        emailCurrent = MainActivity.user.email.toString()
        nameCurrent = MainActivity.user.name.toString()
        MainActivity.user.email?.let {
            FirestoreUtils.getOrCreateChatChanel(otherUserId, it) { channelId ->
                currentChannelId = channelId
                messagesListenerRegistration =
                    FirestoreUtils.addChatMessagesListener(
                        channelId,
                        requireContext(),
                        this::updateRecyclerView
                    )
                imageView_send.click {
                    val messageToSend = MainActivity.user.name?.let { it1 ->
                        MainActivity.user.email?.let { it2 ->
                            TextMessageChat(
                                editText_message.text.toString(), Calendar.getInstance().time,
                                it2, otherUserId,
                                it1
                            )
                        }
                    }
                    editText_message.setText("")
                    imageView_send.show(false)
                    pushNotification(token, "Thông báo", "$nameCurrent đã gửi tin nhắn cho bạn ")
                    if (messageToSend != null) {
                        FirestoreUtils.sendMessage(messageToSend, channelId)
                    }
                }

                fab_send_image.setOnClickListener {
                    val intent = Intent().apply {
                        type = "image/*"
                        action = Intent.ACTION_GET_CONTENT
                        putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                    }
                    startActivityForResult(
                        Intent.createChooser(intent, "Select Image"),
                        RC_SELECT_IMAGE
                    )
                }
            }
        }
    }

    override fun observable() {

    }

    override fun fireData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val selectedImagePath = data.data

            val selectedImageBmp =
                MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val selectedImageBytes = outputStream.toByteArray()

            CameraUtils.uploadMessageImage(selectedImageBytes) { imagePath ->
                val messageToSend =
                    ImageMessageChat(
                        imagePath, Calendar.getInstance().time, emailCurrent,
                        otherUserId, nameCurrent
                    )
                FirestoreUtils.sendMessage(messageToSend, currentChannelId)
                pushNotification(token, "Thông báo", "$nameCurrent đã gửi tin nhắn cho bạn ")
            }
        }
    }

    private fun pushNotification(token: String, title: String, body: String) {
        val notification = Notification(title, body)
        val message = Message(token, notification)
        viewModel.pushNotification(message)
    }

    private fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            recycler_view_messages?.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        recycler_view_messages?.scrollToPosition(
            (recycler_view_messages?.adapter?.itemCount ?: 0) - 1
        )
    }
}