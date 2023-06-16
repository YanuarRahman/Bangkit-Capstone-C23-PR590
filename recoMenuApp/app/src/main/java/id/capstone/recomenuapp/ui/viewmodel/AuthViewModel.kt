package id.capstone.recomenuapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.capstone.recomenuapp.data.Repository
import id.capstone.recomenuapp.model.LoginModel
import id.capstone.recomenuapp.model.RegisterModel
import id.capstone.recomenuapp.model.ResponseSession
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: Repository) : ViewModel() {

    val responseRegister: LiveData<RegisterModel> = repository.responseRegister
    val responseLogin: LiveData<LoginModel> = repository.responseLogin
    val responseGetAllUsers: LiveData<List<RegisterModel>> = repository.responseGetAllUsers
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun postRegister(
        name: String,
        email: String,
        password: String,
        phone: String,
        likeIngredient: String,
        isAdmin: Boolean
    ) {
        viewModelScope.launch {
            repository.postRegister(name, email, password, phone, likeIngredient, isAdmin)
        }
    }

    fun postLogin(
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            repository.postLogin(email, password)
        }
    }

    fun getAllUsers(
        token: String
    ) {
        viewModelScope.launch {
            repository.getAllUsers(token)
        }
    }

    fun saveSession(ss: ResponseSession){
        viewModelScope.launch{
            repository.login()
            repository.saveSession(ss)
        }
    }
}