package com.primex.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.primex.preferences.*
import com.primex.preferences.Key.Key1
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import androidx.datastore.preferences.core.Preferences as StorePreference

private const val TAG = "Preferences"


internal class PreferencesImpl(context: Context, name: String) : Preferences {
    /**
     * As this instance has a lifespan of [Application], hence using [GlobalScope] is no issue.
     */
    @DelicateCoroutinesApi
    private val scope = GlobalScope
    private val Context.store by preferencesDataStore(name)

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

    override fun <S> observe(key: Key1<S>): Flow<S?> =
        flow.map { preferences -> preferences[key.value] }

    override fun <S> observe(key: Key.Key2<S>): Flow<S> =
        flow.map { preferences ->
            preferences[key.value] ?: key.default
        }

    override fun <S, O> observe(key: Key.Key3<S, O>): Flow<O?> =
        flow.map { preferences ->
            // return restored value
            preferences[key.value]?.let { key.saver.restore(it) }
        }

    override fun <S, O> observe(key: Key.Key4<S, O>): Flow<O> =
        flow.map { preferences ->
            // return restored or default.
            preferences[key.value]?.let { key.saver.restore(it) } ?: key.default
        }

    private fun <T> set(key: StoreKey<T>, value: T) {
        scope.launch {
            store.edit {
                it[key] = value
            }
        }
    }

    override fun <S> set(key: Key1<S>, value: S) = set(key.value, value)

    override fun <S> set(key: Key.Key2<S>, value: S) =
        set(key.value, value)

    override fun <S, O> set(key: Key.Key3<S, O>, value: O) =
        set(key.value, key.saver.save(value))

    override fun <S, O> set(key: Key.Key4<S, O>, value: O) =
        set(key.value, key.saver.save(value))

    override fun minusAssign(key: Key) {
        scope.launch {
            store.edit {
                it -= key.storeKey
            }
        }
    }

    override fun contains(key: Key): Boolean {
        return runBlocking {
            flow.map { preference -> key.storeKey in preference }.first()
        }
    }

    override fun clear(x: MutablePreferences) {
        scope.launch {
            store.edit {
                it.clear()
            }
        }
    }

    override fun remove(key: Key) {
        scope.launch {
            store.edit {
                it.remove(key.storeKey)
            }
        }
    }
}

private inline val Key.storeKey
    inline get() = when (this) {
        is Key1<*> -> value
        is Key.Key2<*> -> value
        is Key.Key3<*, *> -> value
        is Key.Key4<*, *> -> value
    }