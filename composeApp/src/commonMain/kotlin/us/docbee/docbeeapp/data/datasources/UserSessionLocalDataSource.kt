package us.docbee.docbeeapp.data.datasources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import us.docbee.docbeeapp.data.datasources.interfaces.UserSessionDataSource

class UserSessionLocalDataSource(
    private val dataStore: DataStore<Preferences>
) : UserSessionDataSource {

    companion object {
        private val IS_REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")
    }

    override suspend fun saveSessionFlag(value: Boolean) {
        dataStore.edit { preference ->
            preference[IS_REMEMBER_ME_KEY] = value
        }
    }

    override suspend fun getSessionFlag(): Flow<Boolean> = dataStore.data.map { preference ->
        preference[IS_REMEMBER_ME_KEY] ?: false
    }

    override suspend fun clear() {
        dataStore.edit { preference ->
            preference.remove(IS_REMEMBER_ME_KEY)
        }
    }
}