package id.capstone.recomenuapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseSession(
    val email: String,
    val token: String,
    val isLogin: Boolean,
    val id: String,
    val name: String,
    val phone: String,
    val likeIngredient: String
) : Parcelable
