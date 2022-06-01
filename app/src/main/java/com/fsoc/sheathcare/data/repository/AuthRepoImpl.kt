package com.fsoc.sheathcare.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.fsoc.sheathcare.domain.entity.User
import com.fsoc.sheathcare.domain.repository.AuthRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Single
import javax.inject.Inject

class AuthRepoImpl @Inject constructor() : AuthRepo {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var token: String

    @Inject
    lateinit var fireStore: FirebaseFirestore

    init {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
            token = it.result.toString()
        })
    }

    override fun login(name: String, password: String): Single<FirebaseUser> {
        return Single.create {
            firebaseAuth.signInWithEmailAndPassword(name, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fireStore.collection("users")
                        .document(name)
                        .update(mapOf("token" to token))
                    firebaseAuth.currentUser?.let { it1 -> it.onSuccess(it1) }
                } else {
//                    it.serialize()
                    Log.e("Tag", "Login Failure: " + task.exception?.message)
                }
            }.addOnFailureListener { exception ->
                it.onError(exception)
            }
        }
    }

    override fun register(
        email: String,
        name: String,
        password: String,
        age: Int
    ): Single<FirebaseUser> {
        return Single.create {
            val user = User(email, name, null, age, 0, token)
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        fireStore.collection("users").document(email).set(user)
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot written")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                        firebaseAuth.currentUser?.let { it1 -> it.onSuccess(it1) }
                    } else {
                        Log.e("Tag", "Login Failure: " + task.exception?.message)
                    }
                }.addOnFailureListener { exception ->
                    it.onError(exception)
                }
        }
    }

}

