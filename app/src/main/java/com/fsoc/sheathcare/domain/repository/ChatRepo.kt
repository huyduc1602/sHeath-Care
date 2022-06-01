package com.fsoc.sheathcare.domain.repository

import com.fsoc.sheathcare.domain.entity.Message
import com.fsoc.sheathcare.domain.entity.User
import io.reactivex.Single
import retrofit2.Call

interface ChatRepo {
    fun getListDoctor(): Single<List<User>>
    fun getListUserChat(currentId : String): Single<List<String>>
    fun getListUser(email : List<String>): Single<List<User>>
    fun putNotification(message : Message) : Call<Message>

}