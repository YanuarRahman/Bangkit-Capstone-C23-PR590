package id.capstone.recomenuapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import id.capstone.recomenuapp.model.ResponseSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionPreferences private constructor(
    private val ds: DataStore<Preferences>
) {


    fun getSession(): Flow<ResponseSession?> {
        return ds.data.map { preferences ->
            ResponseSession(
                preferences[KEY_EMAIL] ?: "",
                preferences[KEY_TOKEN]?.trim() ?: "",
                preferences[KEY_STATE] ?: false,
                preferences[KEY_ID] ?: "",
                preferences[KEY_NAME] ?: "",
                preferences[KEY_PHONE] ?: "",
                preferences[KEY_INGREDIENT] ?: "",

            )
        }
    }

    suspend fun saveSession(ss: ResponseSession) {
        ds.edit { preferences ->
            preferences[KEY_EMAIL] = ss.email
            preferences[KEY_TOKEN] = ss.token
            preferences[KEY_STATE] = ss.isLogin
            preferences[KEY_ID] = ss.id
            preferences[KEY_NAME] = ss.name
            preferences[KEY_PHONE] = ss.phone
            preferences[KEY_INGREDIENT] = ss.likeIngredient
        }
    }

    suspend fun login() {
        ds.edit { preferences ->
            preferences[KEY_STATE] = true
        }
    }

    suspend fun logout() {
        ds.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreferences? = null
        private val KEY_EMAIL = stringPreferencesKey("email")
        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_STATE = booleanPreferencesKey("state")
        private val KEY_ID = stringPreferencesKey("id")
        private val KEY_NAME = stringPreferencesKey("name")
        private val KEY_PHONE = stringPreferencesKey("phone")
        private val KEY_INGREDIENT = stringPreferencesKey("ingredient")

        fun getInstance(ds: DataStore<Preferences>): SessionPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreferences(ds)
                INSTANCE = instance
                instance
            }
        }
    }
}