package id.capstone.recomenuapp.data.remote

import id.capstone.recomenuapp.data.request.PredictRequest
import id.capstone.recomenuapp.model.PredictModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface ApiML {

    @GET("predict")
    fun getPredict(
        @Body request: PredictRequest
    ): Call<PredictModel>
}