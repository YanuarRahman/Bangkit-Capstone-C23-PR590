package id.capstone.recomenuapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    val description: String? = "",
    val name: String? = "",
    val price: Long? = 0L,
    val ingredient: String? = ""
) : Parcelable