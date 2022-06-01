package com.fsoc.sheathcare.data.repository

import com.fsoc.sheathcare.data.api.NotificationApi
import com.fsoc.sheathcare.domain.entity.Message
import com.fsoc.sheathcare.domain.entity.User
import com.fsoc.sheathcare.domain.repository.ChatRepo
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import retrofit2.Call
import javax.inject.Inject
import kotlin.collections.ArrayList

class ChatRepoImpl @Inject constructor() : ChatRepo {
    @Inject
    lateinit var fireStore: FirebaseFirestore

    @Inject
    lateinit var api: NotificationApi
    private var listDoctor = mutableListOf<User>()
    private var listUserChat = mutableListOf<User>()
    private var listEmail = mutableListOf<String>()
    override fun getListDoctor(): Single<List<User>> {
        return Single.create { emitter ->
            listDoctor.clear()
            fireStore.collection("users")
                .whereEqualTo("type", 1)
                .get().addOnSuccessListener { data ->
                    data.forEach {
                        listDoctor.add(it.toObject(User::class.java))
                    }
                    emitter.onSuccess(listDoctor)
                }.addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun getListUserChat(currentId: String): Single<List<String>> {
        return Single.create { emitter ->
            listEmail.clear()
            fireStore.collection("chatChannels")
                .whereArrayContains("userIds", currentId)
                .get().addOnSuccessListener { data ->
                    data.forEach { listIds ->
                        val result = listIds.data.values.elementAt(0) as ArrayList<String>
                        listEmail.add(result.elementAt(0))
                    }
                    emitter.onSuccess(listEmail)
                }.addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun getListUser(email: List<String>): Single<List<User>> {
        return Single.create { emitter ->
            listUserChat.clear()
            email.forEach { email ->
                fireStore.collection("users").document(email).get()
                    .addOnSuccessListener { result ->
                        listUserChat.add(result.toObject(User::class.java)!!)
                    }.addOnFailureListener { exception ->
                        emitter.onError(exception)
                    }
            }
            emitter.onSuccess(listUserChat)
        }
    }

    override fun putNotification(message: Message): Call<Message> {
        return api.sendNotifcation(message)
    }
}