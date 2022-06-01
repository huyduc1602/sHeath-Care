package com.fsoc.sheathcare.presentation.main.auth

import androidx.lifecycle.MutableLiveData
import com.fsoc.sheathcare.common.extension.applyIoScheduler
import com.fsoc.sheathcare.domain.entity.User
import com.fsoc.sheathcare.domain.usecase.AuthUseCase
import com.fsoc.sheathcare.presentation.main.MainViewModel
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthViewModel @Inject constructor() : MainViewModel() {
    @Inject
    lateinit var authUseCase: AuthUseCase

    val firebaseUser = MutableLiveData<FirebaseUser>()

    fun login(email: String, password: String) {
        authUseCase.login(email, password)
            .applyIoScheduler(mLoading)
            .subscribe({ user ->
                firebaseUser.value = user
            }, { error -> mError.value = error }).track()
    }

    fun register(email: String, name: String, password: String, age: Int) {
        authUseCase.register(email, name, password, age)
            .applyIoScheduler(mLoading)
            .subscribe({ user ->
                firebaseUser.value = user
            }, { error -> mError.value = error }).track()
    }
}