package id.capstone.recomenuapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.capstone.recomenuapp.data.local.SessionPreferences
import id.capstone.recomenuapp.data.remote.ApiML
import id.capstone.recomenuapp.data.remote.ApiService
import id.capstone.recomenuapp.data.request.LoginRequest
import id.capstone.recomenuapp.data.request.PredictRequest
import id.capstone.recomenuapp.data.request.RegisterRequest
import id.capstone.recomenuapp.model.LoginModel
import id.capstone.recomenuapp.model.PredictModel
import id.capstone.recomenuapp.model.ProductModel
import id.capstone.recomenuapp.model.RegisterModel
import id.capstone.recomenuapp.model.ResponseSession
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository internal constructor(
    private val preferences: SessionPreferences,
    private val service: ApiService,
    private val ml: ApiML
) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseRegister = MutableLiveData<RegisterModel>()
    val responseRegister: LiveData<RegisterModel> = _responseRegister

    private val _responseLogin = MutableLiveData<LoginModel>()
    val responseLogin: LiveData<LoginModel> = _responseLogin

    private val _responsePredict = MutableLiveData<PredictModel>()
    val responsePredict: LiveData<PredictModel> = _responsePredict

    private val _responseGetAllUsers = MutableLiveData<List<RegisterModel>>()
    val responseGetAllUsers: LiveData<List<RegisterModel>> = _responseGetAllUsers

    private val _responseProducts = MutableLiveData<List<ProductModel>>()
    val responseProducts: LiveData<List<ProductModel>> = _responseProducts

    fun postRegister(
        name: String,
        email: String,
        password: String,
        phone: String,
        likeIngredient: String,
        isAdmin: Boolean
    ) {
        _isLoading.value = true
        service.postRegister(RegisterRequest(name, email, password, phone, likeIngredient, isAdmin))
            .enqueue(object : Callback<RegisterModel> {
                override fun onResponse(
                    call: Call<RegisterModel>,
                    response: Response<RegisterModel>
                ) {
                    _isLoading.postValue(false)
                    val responseBody = response.body()
                    if (responseBody != null && response.isSuccessful) {
                        _responseRegister.value = response.body()
                        Log.d(TAG, "Response Message: ${response.message()}")
                    } else {
                        Log.e(
                            TAG,
                            "onFailure: ${response.message()}, ${response.body().toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<RegisterModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
    }

    fun postLogin(
        email: String,
        password: String,
    ) {
        _isLoading.value = true
        service.postLogin(LoginRequest(email, password))
            .enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    _isLoading.postValue(false)
                    val responseBody = response.body()
                    if (responseBody != null && response.isSuccessful) {
                        _responseLogin.value = response.body()
                        Log.d(TAG, "Response Message: ${response.message()}")
                    } else {
                        Log.e(
                            TAG,
                            "onFailure: ${response.message()}, ${response.body().toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
    }

    suspend fun saveSession(ss: ResponseSession) {
        preferences.saveSession(ss)
    }

    fun getSession(): Flow<ResponseSession> {
        return preferences.getSession() as Flow<ResponseSession>
    }

    suspend fun login() {
        preferences.login()
    }

    fun getPredict(
        id: String,
        name: String,
        spicyLevel: Int,
        likeIngredient: Array<String>
    ) {
        _isLoading.value = true
        ml.getPredict(PredictRequest(id, name, spicyLevel, likeIngredient))
            .enqueue(object : Callback<PredictModel> {
                override fun onResponse(
                    call: Call<PredictModel>,
                    response: Response<PredictModel>
                ) {
                    _isLoading.postValue(false)
                    val responseBody = response.body()
                    if (responseBody != null && response.isSuccessful) {
                        _responsePredict.value = response.body()
                        Log.d(TAG, "Response Message: ${response.message()}")
                    } else {
                        Log.e(
                            TAG,
                            "onFailure: ${response.message()}, ${response.body().toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<PredictModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
    }

    fun getAllUsers(token: String) {
        _isLoading.value = true
        service.getAllUsers("Bearer $token")
            .enqueue(object : Callback<List<RegisterModel>> {
                override fun onResponse(
                    call: Call<List<RegisterModel>>,
                    response: Response<List<RegisterModel>>
                ) {
                    _isLoading.postValue(false)
                    val responseBody = response.body()
                    if (responseBody != null && response.isSuccessful) {
                        _responseGetAllUsers.value = response.body()
                        Log.d(TAG, "Response Message: ${response.message()}")
                    } else {
                        Log.e(
                            TAG,
                            "onFailure: ${response.message()}, ${response.body().toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<List<RegisterModel>>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
    }

    fun getProducts() {
        _isLoading.value = true
        service.getProducts()
            .enqueue(object : Callback<List<ProductModel>> {
                override fun onResponse(
                    call: Call<List<ProductModel>>,
                    response: Response<List<ProductModel>>
                ) {
                    _isLoading.postValue(false)
                    val responseBody = response.body()
                    if (responseBody != null && response.isSuccessful) {
                        _responseProducts.value = response.body()
                        Log.d(TAG, "Response Message: ${response.message()}")
                    } else {
                        Log.e(
                            TAG,
                            "onFailure: ${response.message()}, ${response.body().toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
    }

    companion object {

        private const val TAG = "Repository"

        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            preferences: SessionPreferences,
            service: ApiService,
            ml: ApiML
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(preferences, service, ml)
            }.also { instance = it }
    }

}