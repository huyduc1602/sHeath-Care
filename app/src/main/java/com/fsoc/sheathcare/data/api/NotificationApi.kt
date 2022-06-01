package com.fsoc.sheathcare.data.api

import com.fsoc.sheathcare.domain.entity.Message
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAAp9hNoaw:APA91bEf4MHN9DP_U4SL3WexHmXBMsajwU0MqbGkA0aU5vPVAY6UgYJDiOU2XV8epvlNCvNV7L37eVHIgVjFtLAHIToFDqgcYbFz_wYX9exnvwjmlSy8ZqAeBHPLU_B2SgpQ923a5vap"
    )
    //AAAAp9hNoaw:APA91bEf4MHN9DP_U4SL3WexHmXBMsajwU0MqbGkA0aU5vPVAY6UgYJDiOU2XV8epvlNCvNV7L37eVHIgVjFtLAHIToFDqgcYbFz_wYX9exnvwjmlSy8ZqAeBHPLU_B2SgpQ923a5vap
    @POST("fcm/send")
    fun sendNotifcation(@Body message: Message): Call<Message>
}