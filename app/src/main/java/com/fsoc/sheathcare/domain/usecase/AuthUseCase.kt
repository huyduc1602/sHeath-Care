package com.fsoc.sheathcare.domain.usecase

import com.fsoc.sheathcare.domain.entity.User
import com.fsoc.sheathcare.domain.repository.AuthRepo
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import javax.inject.Inject

class AuthUseCase @Inject constructor() {
    @Inject
    lateinit var authRepo: AuthRepo
    fun login(email: String, password: String) : Single<FirebaseUser>{
        return authRepo.login(email,password)
    }
    fun register(email: String,name: String,password: String,age: Int) : Single<FirebaseUser>{
        return authRepo.register(email, name, password, age)
    }
}