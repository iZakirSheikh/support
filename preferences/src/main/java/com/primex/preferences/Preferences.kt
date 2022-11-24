package com.primex.preferences

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.compose.runtime.*
import androidx.datastore.preferences.core.*
import com.primex.preferences.Key.Key1
import com.primex.preferences.Key.Key2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.datastore.preferences.core.Preferences as StorePreference

internal typealias StoreKey<T> = StorePreference.Key<T>

/**
 * The [Saver] describes how the object of [Original] class can be simplified and converted into
 * something which is [Saveable].
 */
interface Saver<S, O> {
    /**
     * Convert the value into a saveable one.
     */
    fun save(value: O): S

    /**
     * Convert the restored value back to the original Class.
     */
    fun restore(value: S): O
}

sealed interface Key<S, O> {
    /**
     * The name of the key.
     */
    val name: String

    /**
     * This constructs a [StoreKey] wrapper with [Saver].
     */
    class Key1<S, O> internal constructor(
        internal val value: StoreKey<S>,
        internal val saver: Saver<S, O>?
    ) : Key<S, O> {
        override val name: String
            get() = value.name
    }

    /**
     * This constructs a [StoreKey] wrapper with [Saver] and [default].
     */
    class Key2<S, O> internal constructor(
        internal val value: StoreKey<S>,
        internal val default: O,
        internal val saver: Saver<S, O>?
    ) : Key<S, O> {
        override val name: String
            get() = value.name
    }
}

interface Preferences {

    /**
     * Returns [Flow] of type T ([String], [Float], [Double], [Int], [Long], String [Set]).
     *
     * Note: This can be nullable if [key] doesn't exist.
     */
    operator fun <S, O> get(key: Key1<S, O>): Flow<O?>


    /**
     * Note: emits [default] if original not exists.
     * @see observe
     */
    operator fun <S, O> get(key: Key2<S, O>): Flow<O>

    /**
     * Set a key value pair in MutablePreferences.
     */
    operator fun <S, O> set(key: Key<S, O>, value: O)

    /** Removes the preference with the given key from this MutablePreferences. If this
     * Preferences does not contain the key, this is a no-op.
     *
     * Example usage:
     * mutablePrefs -= COUNTER_KEY
     *
     * @param key the key to remove from this MutablePreferences
     */
    operator fun minusAssign(key: Key<*, *>)

    /**
     * Returns true if this Preferences contains the specified key.
     *
     * @param key the key to check for
     */
    @WorkerThread
    operator fun contains(key: Key<*, *>): Boolean

    /** Removes all preferences from this MutablePreferences. */
    fun clear(x: MutablePreferences)

    /**
     * Remove a preferences from this MutablePreferences.
     */
    fun remove(key: Key<*, *>)

    companion object {

        // Singleton prevents multiple instances of repository opening at the
        // same time.
        private const val TAG = "Preferences"

        private const val DEFAULT_NAME = "Shared_Preferences"

        @Volatile
        private var INSTANCE: Preferences? = null

        @Deprecated("Create instance and use with Hilt")
        fun get(context: Context): Preferences {

            // if the INSTANCE is not null, then return it,
            // if it is, then create the repository
            return INSTANCE ?: synchronized(this) {
                val instance =
                    PreferencesImpl(context.applicationContext as Application, DEFAULT_NAME)
                INSTANCE = instance
                instance
            }
        }

        operator fun invoke(context: Context, name: String = DEFAULT_NAME): Preferences =
            PreferencesImpl(context.applicationContext, name)
    }
}

/**
 * Saves value to/from original.
 */
typealias IntSaver<O> = Saver<Int, O>

/**
 * @see [intPreferencesKey]
 */
fun intPreferenceKey(name: String) =
    Key1<Int, Int>(intPreferencesKey(name), null)

/**
 * @see [intPreferencesKey]
 */
fun intPreferenceKey(name: String, defaultValue: Int) =
    Key2(intPreferencesKey(name), defaultValue, null)

/**
 * @see [intPreferencesKey]
 */
fun <O> intPreferenceKey(name: String, saver: IntSaver<O>) =
    Key1(intPreferencesKey(name), saver)

/**
 * @see [intPreferencesKey]
 */
fun <O> intPreferenceKey(name: String, defaultValue: O, saver: IntSaver<O>) =
    Key2(intPreferencesKey(name), defaultValue, saver)


/**
 * Saves value to/from original.
 */
typealias FloatSaver<O> = Saver<Float, O>

/**
 * @see [floatPreferencesKey]
 */
fun floatPreferenceKey(name: String) =
    Key1<Float, Float>(floatPreferencesKey(name), null)

/**
 * @see [floatPreferencesKey]
 */
fun floatPreferenceKey(name: String, defaultValue: Float) =
    Key2(floatPreferencesKey(name), defaultValue, null)

/**
 * @see [floatPreferencesKey]
 */
fun <O> floatPreferenceKey(name: String, saver: FloatSaver<O>) =
    Key1(floatPreferencesKey(name), saver)

/**
 * @see [floatPreferencesKey]
 */
fun <O> floatPreferenceKey(name: String, defaultValue: O, saver: FloatSaver<O>) =
    Key2(floatPreferencesKey(name), defaultValue, saver)


/**
 * Saves value to/from original.
 */
typealias DoubleSaver<O> = Saver<Double, O>

/**
 * @see [doublePreferencesKey]
 */
fun doublePreferenceKey(name: String) =
    Key1<Double, Double>(doublePreferencesKey(name), null)

/**
 * @see [doublePreferencesKey]
 */
fun doublePreferenceKey(name: String, defaultValue: Double) =
    Key2(doublePreferencesKey(name), defaultValue, null)

/**
 * @see [doublePreferencesKey]
 */
fun <O> doublePreferenceKey(name: String, saver: DoubleSaver<O>) =
    Key1(doublePreferencesKey(name), saver)

/**
 * @see [doublePreferencesKey]
 */
fun <O> doublePreferenceKey(name: String, defaultValue: O, saver: DoubleSaver<O>) =
    Key2(doublePreferencesKey(name), defaultValue, saver)

/**
 * Saves value to/from original.
 */
typealias LongSaver<O> = Saver<Long, O>

/**
 * @see [longPreferencesKey]
 */
fun longPreferenceKey(name: String) =
    Key1<Long, Long>(longPreferencesKey(name), null)

/**
 * @see [longPreferencesKey]
 */
fun longPreferenceKey(name: String, defaultValue: Long) =
    Key2(longPreferencesKey(name), defaultValue, null)

/**
 * @see [longPreferencesKey]
 */
fun <O> longPreferenceKey(name: String, saver: LongSaver<O>) =
    Key1(longPreferencesKey(name), saver)

/**
 * @see [longPreferencesKey]
 */
fun <O> longPreferenceKey(name: String, defaultValue: O, saver: LongSaver<O>) =
    Key2(longPreferencesKey(name), defaultValue, saver)

/**
 * @see [booleanPreferencesKey]
 */
fun booleanPreferenceKey(name: String) =
    Key1<Boolean, Boolean>(booleanPreferencesKey(name), null)

/**
 * @see [booleanPreferencesKey]
 */
fun booleanPreferenceKey(name: String, defaultValue: Boolean) =
    Key2(booleanPreferencesKey(name), defaultValue, null)

/**
 * Saves value to/from original.
 */
typealias StringSaver<O> = Saver<String, O>

/**
 * @see [stringPreferencesKey]
 */
fun stringPreferenceKey(name: String) =
    Key1<String, String>(stringPreferencesKey(name), null)

/**
 * @see [stringPreferencesKey]
 */
fun stringPreferenceKey(name: String, defaultValue: String) =
    Key2(stringPreferencesKey(name), defaultValue, null)

/**
 * @see [stringPreferencesKey]
 */
fun <O> stringPreferenceKey(name: String, saver: StringSaver<O>) =
    Key1(stringPreferencesKey(name), saver)

/**
 * @see [stringPreferencesKey]
 */
fun <O> stringPreferenceKey(name: String, defaultValue: O, saver: StringSaver<O>) =
    Key2(stringPreferencesKey(name), defaultValue, saver)

fun stringSetPreferenceKey(name: String) =
    Key1<Set<String>, Set<String>>(stringSetPreferencesKey(name), null)

fun stringSetPreferenceKey(name: String, defaultValue: Set<String>) =
    Key2(stringSetPreferencesKey(name), defaultValue, null)


@Composable
private inline fun <S, O> Preferences.observe(key: Key<S, O>): State<O?> {
    val flow = when(key){
        is Key1 -> this[key]
        is Key2 -> this[key]
    }

    val first = remember(key.name) {
        runBlocking { flow.first() }
    }
    return flow.collectAsState(initial = first)
}

@Composable
@NonRestartableComposable
fun <S, O> Preferences.observeAsState(key: Key1<S, O>): State<O?> = observe(key = key)

@Composable
@NonRestartableComposable
fun <S, O> Preferences.observeAsState(key: Key2<S, O>): State<O> = observe(key = key) as State<O>


/**
 * @return the raw value from the [Preferences] linked with [key]
 */
@WorkerThread
fun <S, O> Preferences.value(key: Key1<S, O>): O? = runBlocking { this@value[key].first() }

/**
 * @see [value]
 */
@WorkerThread
fun <S, O> Preferences.value(key: Key2<S, O>): O = runBlocking { this@value[key].first() }

