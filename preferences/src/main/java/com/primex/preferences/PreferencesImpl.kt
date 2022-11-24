package com.primex.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.PreferencesProto.StringSet
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.primex.preferences.*
import com.primex.preferences.Key.Key1
import com.primex.preferences.Key.Key2
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


    override fun <S, O> get(key: Key1<S, O>): Flow<O?> {
        return flow.map { preferences ->
            if (key.saver == null) // must not be null if not provided values.
                preferences[key.value] as O // possible only if basic values.
            else
                preferences[key.value]?.let { key.saver.restore(it) }
        }
    }

    override fun <S, O> get(key: Key2<S, O>): Flow<O> {
        return flow.map { preferences ->
            if (key.saver == null) // must not be null if not provided values.
                (preferences[key.value] ?: key.default) as O // possible only if basic values.
            else
                preferences[key.value]?.let { key.saver.restore(it) } ?: key.default
        }
    }

    override fun <S, O> set(key: Key<S, O>, value: O) {
        scope.launch {
            store.edit {
                val saver = key.saver
                // if saver is null in that case the savable value is simple like int.
                // else use saver to save the value.
                it[key.storeKey] = if (saver == null) (value as S) else saver.save(value)
            }
        }
    }

    override fun minusAssign(key: Key<*, *>) {
        scope.launch {
            store.edit {
                it -= key.storeKey
            }
        }
    }

    override fun contains(key: Key<*, *>): Boolean {
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

    override fun remove(key: Key<*, *>) {
        scope.launch {
            store.edit {
                it.remove(key.storeKey)
            }
        }
    }
}



private inline  val <S, O> Key<S, O>.storeKey get() = when(this){
    is Key1 -> value
    is Key2 -> value
}

private inline val <S, O> Key<S, O>.saver: Saver<S, O>? get() = when(this){
    is Key1 -> saver
    is Key2 -> saver
}
