package id.capstone.recomenuapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.capstone.recomenuapp.Injection
import id.capstone.recomenuapp.data.Repository

class Factory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> {
                MenuViewModel(repository) as T
            }
//            modelClass.isAssignableFrom(PerfumeDetailViewModel::class.java) -> {
//                PerfumeDetailViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
//                HomeViewModel(repository) as T
//            }
//
//            modelClass.isAssignableFrom(BrandDetailViewModel::class.java) -> {
//                BrandDetailViewModel(repository) as T
//            }
//
//            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
//                FavoriteViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
//                SearchViewModel(repository) as T
//            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: Factory? = null
        fun getInstance(context: Context): Factory {
            return instance ?: synchronized(this) {
                instance ?: Factory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}
