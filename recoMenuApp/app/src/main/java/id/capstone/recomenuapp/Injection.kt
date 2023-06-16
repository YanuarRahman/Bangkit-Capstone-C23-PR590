package id.capstone.recomenuapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.protobuf.Api
import id.capstone.recomenuapp.data.Repository
import id.capstone.recomenuapp.data.local.SessionPreferences
import id.capstone.recomenuapp.data.remote.ApiConfig

private val Context.ds: DataStore<Preferences> by preferencesDataStore("token")

class Injection {
    companion object {

        fun provideRepository(context: Context): Repository {
            val preferences = SessionPreferences.getInstance(context.ds)
            val service = ApiConfig.getApiService()
            val ml = ApiConfig.getMlService()
            return Repository.getInstance(preferences, service, ml)
        }
    }


}