package com.fsoc.sheathcare.common.utils

import android.content.Context
import android.util.Log
import com.fsoc.sheathcare.domain.entity.*
import com.fsoc.sheathcare.presentation.main.chat.adapter.ImageMessageItem
import com.fsoc.sheathcare.presentation.main.chat.adapter.TextMessageItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item

object FirestoreUtils {
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val chatChannelsCollectionRef = firestoreInstance.collection("chatChannels")

    fun getOrCreateChatChanel(
        otherUserId: String,
        currentUserId: String,
        onComplete: (channelId: String) -> Unit
    ) {
        firestoreInstance.collection("users").document(currentUserId)
            .collection("engagedChatChannels")
            .document(otherUserId).get().addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                }
                val newChannel =
                    chatChannelsCollectionRef.document(currentUserId + "_$otherUserId")
                newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                firestoreInstance.collection("users").document(currentUserId)
                    .collection("engagedChatChannels")
                    .document(otherUserId)
                    .set(mapOf("channelId" to newChannel.id))

                firestoreInstance.collection("users").document(otherUserId)
                    .collection("engagedChatChannels")
                    .document(currentUserId)
                    .set(mapOf("channelId" to newChannel.id))

                onComplete(newChannel.id)
            }
    }

    fun sendMessage(messageChat: MessageChat, channelId: String) {
        chatChannelsCollectionRef.document(channelId)
            .collection("messages")
            .add(messageChat)
    }

    fun addChatMessagesListener(
        channelId: String, context: Context,
        onListen: (List<Item>) -> Unit
    ): ListenerRegistration {
        return chatChannelsCollectionRef.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {
                    if (it["type"] == MessageType.TEXT) {
                        items.add(TextMessageItem(it.toObject(TextMessageChat::class.java)!!, context))
                    } else {
                        items.add(
                            ImageMessageItem(
                                it.toObject(ImageMessageChat::class.java)!!,
                                context
                            )
                        )
                    }

                    return@forEach
                }
                onListen(items)
            }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()


}