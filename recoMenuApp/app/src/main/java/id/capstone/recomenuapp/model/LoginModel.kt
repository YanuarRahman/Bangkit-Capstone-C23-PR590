package id.capstone.recomenuapp.model

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("user")
    val email: String? = "",
    val token: String? = "",
)