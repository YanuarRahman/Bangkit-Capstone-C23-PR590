package id.capstone.recomenuapp.data.request

data class PredictRequest(
    val _id: String? = "",
    val name: String? = "",
    val spicyLevel: Int? = 0,
    val likeIngredient: Array<String> = emptyArray()
)