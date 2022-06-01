package com.fsoc.sheathcare.presentation.main.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fsoc.sheathcare.common.extension.applyIoScheduler
import com.fsoc.sheathcare.domain.entity.Message
import com.fsoc.sheathcare.domain.entity.User
import com.fsoc.sheathcare.domain.usecase.ChatUseCase
import com.fsoc.sheathcare.presentation.main.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ChatViewModel @Inject constructor() : MainViewModel() {
    @Inject
    lateinit var chatUseCase: ChatUseCase
    var listDoctor = MutableLiveData<List<User>>()
    var listUserChat = MutableLiveData<List<User>>()

    fun getListDoctor() {
        chatUseCase.getListDoctor()
            .applyIoScheduler(mLoading)
            .subscribe({ data ->
                listDoctor.value = data
            }, { error -> mError.value = error }).track()
    }

    fun getListUserChat(currentId: String) {
        chatUseCase.getListUserChat(currentId)
            .applyIoScheduler(mLoading)
            .subscribe({ data ->
                listUserChat.value = data
            }, { error -> mError.value = error }).track()
    }

    fun pushNotification(message: Message) {
        chatUseCase.putNotification(message).enqueue(object : Callback<Message> {
            override fun onFailure(call: Call<Message>, t: Throwable) {
                Log.e("Tag", "eror send FCM")
            }

            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                Log.e("Tag", "ok Post")
            }
        })
    }
}