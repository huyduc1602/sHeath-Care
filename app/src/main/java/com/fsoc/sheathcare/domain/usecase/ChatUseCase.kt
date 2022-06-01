package com.fsoc.sheathcare.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.fsoc.sheathcare.common.extension.LOADING
import com.fsoc.sheathcare.domain.entity.Message
import com.fsoc.sheathcare.domain.repository.ChatRepo
import io.reactivex.Single
import retrofit2.Call
import javax.inject.Inject

class ChatUseCase @Inject constructor() {
    @Inject
    lateinit var chatRepo: ChatRepo
    val mLoading = MutableLiveData<LOADING>()
    fun getListDoctor(): Single<List<com.fsoc.sheathcare.domain.entity.User>> {
        return chatRepo.getListDoctor()
    }

    fun getListUserChat(currentId: String): Single<List<com.fsoc.sheathcare.domain.entity.User>> {
        return chatRepo.getListUserChat(currentId).flatMap { lstEmail ->
            chatRepo.getListUser(lstEmail)
        }
    }
    fun putNotification(message : Message) : Call<Message>{
        return chatRepo.putNotification(message)
    }
}