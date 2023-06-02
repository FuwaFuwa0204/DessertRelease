package com.example.dessertrelease.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

//key-value
class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        //key정의
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
        const val TAG = "UserPreferencesRepo"
    }
    suspend fun saveLayoutPreference(isLinearLayout: Boolean) {
        dataStore.edit {
            //MutablePreferences
                preferences ->
            preferences[IS_LINEAR_LAYOUT] = isLinearLayout

        }
    }
    //data는 flow 객체.
    val isLinearLayout: Flow<Boolean> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
        preferences[IS_LINEAR_LAYOUT] ?: true
    }
}