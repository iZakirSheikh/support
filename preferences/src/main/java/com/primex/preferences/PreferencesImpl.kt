package com.primex.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.primex.preferences.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException

import androidx.datastore.preferences.core.Preferences as StorePreference

private const val TAG = "Preferences"

private const val PREFERENCE_NAME = "Shared_Preferences"


internal class PreferencesImpl(context: Context) : Preferences {

    /**
     * As this instance has a lifespan of [Application], hence using [GlobalScope] is no issue.
     */
    @DelicateCoroutinesApi
    private val scope = GlobalScope

    private val Context.store: DataStore<StorePreference> by preferencesDataStore(
        name = PREFERENCE_NAME
    )

    private val store = context.store


    private val flow: Flow<StorePreference> = store.data.catch { exception ->
        when (exception) {
            is IOException -> {
                Log.e(TAG, "getString: $exception")
                emit(emptyPreferences())
            }
            else -> throw exception
        }
    }

    override fun <T> get(key: Key<T>): Flow<T?> {
        return flow.map { preferences -> preferences[key.storeKey] }
    }

    override fun <T> get(key: Key1<T>): Flow<T> {
        return flow.map { preferences ->
            preferences[key.storeKey] ?: key.default
        }
    }

    override fun <T, O> get(key: Key2<T, O>): Flow<O?> {
        return flow.map { preferences ->
            // return restored value
            preferences[key.storeKey]?.let { key.saver.restore(it) }
        }
    }

    override fun <T, O> get(key: Key3<T, O>): Flow<O> {
        return flow.map { preferences ->
            // return restored or default.
            preferences[key.storeKey]?.let { key.saver.restore(it) } ?: key.default
        }
    }

    private fun <T> set(key: StoreKey<T>, value: T) {
        scope.launch {
            store.edit {
                it[key] = value
            }
        }
    }

    override fun <T> set(key: Key<T>, value: T) = set(key.storeKey, value)

    // save saveable value
    override fun <T, O> set(key: Key2<T, O>, value: O) =
        set(key.storeKey, key.saver.save(value))

    override fun <T> set(key: Key1<T>, value: T) = set(key.storeKey, value)

    override fun <T, O> set(key: Key3<T, O>, value: O) = set(key.storeKey, key.saver.save(value))

    private fun <T> minusAssign(key: StoreKey<T>) {
        scope.launch {
            store.edit {
                it -= key
            }
        }
    }

    override fun <T> minusAssign(key: Key<T>) = minusAssign(key.storeKey)

    override fun <T, O> minusAssign(key: Key2<T, O>) = minusAssign(key = key.storeKey)

    override fun <T, O> minusAssign(key: Key3<T, O>) = minusAssign(key.storeKey)

    override fun <T> minusAssign(key: Key1<T>) = minusAssign(key.storeKey)

    private fun <T> contains(key: StoreKey<T>): Boolean {
        return flow.map { preference -> key in preference }.obtain()
    }

    override fun <T, O> contains(key: Key2<T, O>): Boolean = contains(key.storeKey)

    override fun <T, O> contains(key: Key3<T, O>): Boolean = contains(key.storeKey)

    override fun <T> contains(key: Key1<T>): Boolean = contains(key.storeKey)

    override fun <T> contains(key: Key<T>): Boolean = contains(key.storeKey)
}