// ApiService.kt
package pk.edu.pucit.myapplication

import pk.edu.pucit.myapplication.model.DeleteUserResponse
import pk.edu.pucit.myapplication.model.QuizScoreResponse
import pk.edu.pucit.myapplication.model.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("SignIn/{username}/{password}")
    fun signInUser(
        @Path("username") username: String,
        @Path("password") password: String
    ): Call<UserResponse>

    @GET("get_csrf_token/")
    fun getCsrfToken(): Call<Void>

    @GET("get_quiz_score/{username}")
    fun getQuizScore(@Path("username") username: String): Call<QuizScoreResponse>

    @POST("SignUp/{username}/{password}")
    fun createUser(
        @Path("username") username: String,
        @Path("password") password: String
    ): Call<UserResponse>

    @DELETE("delete_user/{username}")
    fun deleteUser(
        @Path("username") username: String

    ): Call<DeleteUserResponse>

    @POST("add_quiz_score/{username}/{score_value}")
    fun addQuizScore(
        @Path("username") username: String?,
        @Path("score_value") scoreValue: Int

    ): Call<Void>
}
