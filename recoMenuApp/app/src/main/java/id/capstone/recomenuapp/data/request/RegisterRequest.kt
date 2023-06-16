package id.capstone.recomenuapp.data.request

data class RegisterRequest(
    val name: String? = "",
    val email: String? = "",
    val password: String? = "",
    val phone: String? = "",
    val likeIngredient: String? = "",
    val isAdmin: Boolean? = false,
)