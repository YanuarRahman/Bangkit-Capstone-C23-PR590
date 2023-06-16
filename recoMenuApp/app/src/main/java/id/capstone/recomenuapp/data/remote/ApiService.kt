package id.capstone.recomenuapp.data.remote

import id.capstone.recomenuapp.data.request.LoginRequest
import id.capstone.recomenuapp.data.request.RegisterRequest
import id.capstone.recomenuapp.model.LoginModel
import id.capstone.recomenuapp.model.ProductModel
import id.capstone.recomenuapp.model.RegisterModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("users/register")
    fun postRegister(
        @Body request: RegisterRequest
    ): Call<RegisterModel>

    @POST("users/login")
    fun postLogin(
        @Body request: LoginRequest
    ): Call<LoginModel>

    @GET("users")
    fun getAllUsers(
        @Header("Authorization") token: String,
    ): Call<List<RegisterModel>>

    @GET("products")
    fun getProducts(): Call<List<ProductModel>>

}