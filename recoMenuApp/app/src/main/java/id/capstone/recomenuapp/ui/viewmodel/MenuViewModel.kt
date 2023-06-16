package id.capstone.recomenuapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.capstone.recomenuapp.data.Repository
import id.capstone.recomenuapp.model.PredictModel
import id.capstone.recomenuapp.model.ProductModel
import id.capstone.recomenuapp.model.ResponseSession
import kotlinx.coroutines.launch

class MenuViewModel(private val repository: Repository) : ViewModel() {

    val responsePredict: LiveData<PredictModel> = repository.responsePredict
    val responseProduct: LiveData<List<ProductModel>> = repository.responseProducts
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun getSession(): LiveData<ResponseSession> {
        val liveData = MutableLiveData<ResponseSession>()

        viewModelScope.launch {
            repository.getSession().collect { responseSession ->
                liveData.value = responseSession
            }
        }

        return liveData
    }

    fun getPredict(
        id: String,
        name: String,
        spicyLevel: Int,
        likeIngredient: Array<String>
    ) {
        viewModelScope.launch {
            repository.getPredict(id, name, spicyLevel, likeIngredient)
        }
    }

    fun getProduct() {
        viewModelScope.launch {
            repository.getProducts()
        }
    }
}