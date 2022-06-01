package com.fsoc.sheathcare.domain.repository

import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
interface AuthRepo {
    fun login(name: String, password: String): Single<FirebaseUser>
    fun register(email: String,name: String, password: String,age: Int): Single<FirebaseUser>

}