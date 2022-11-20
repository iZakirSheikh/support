package com.primex.preferences

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.compose.runtime.*
import androidx.datastore.preferences.core.*
import com.primex.preferences.Key.*
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

sealed interface Key {

    /**
     * The name of the key.
     */
    val name: String


    /**
     * Represents the basic [StoreKey]
     */
    @JvmInline
    value class Key1<S> internal constructor(internal val value: StoreKey<S>) : Key {
        override val name: String
            get() = value.name
    }

    /**
     * Represents  akey with default value.
     */
    data class Key2<S> internal constructor(
        internal val value: StoreKey<S>,
        internal val default: S
    ) : Key {
        override val name: String
            get() = value.name
    }

    /**
     * [Key] with [Saver]
     */
    data class Key3<S, O> internal constructor(
        internal val value: StoreKey<S>,
        internal val saver: Saver<S, O>
    ) : Key {
        override val name: String
            get() = value.name
    }

    /**
     * [Key] with [Saver] and [default] value.
     */
    data class Key4<S, O> internal constructor(
        internal val value: StoreKey<S>,
        internal val default: O,
        internal val saver: Saver<S, O>
    ) : Key {
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
    fun <S> observe(key: Key1<S>): Flow<S?>

    /**
     * Note: emits [default] if original not exists.
     * @see observe
     */
    fun <S> observe(key: Key2<S>): Flow<S>

    /**
     * [Flow] of type Any Type.
     *
     * The key contains [Saver], which helps in conversion from [O] Original to Savable
     * Note: Savable are of Type ([String], [Float], [Double], [Int], [Long], String [Set]).
     * This can be nullable if [key] doesn't exist.
     */
    fun <S, O> observe(key: Key3<S, O>): Flow<O?>

    /**
     * **Note returns defaultValue if null**
     *
     * @see observe
     */
    fun <S, O> observe(key: Key4<S, O>): Flow<O>

    /**
     * Set a key value pair in MutablePreferences.
     */
    operator fun <S> set(key: Key1<S>, value: S)

    /**
     * @see [set]
     */
    operator fun <S> set(key: Key2<S>, value: S)

    /**
     * @see [set]
     */
    operator fun <S, O> set(key: Key3<S, O>, value: O)

    /**
     * @see [set]
     */
    operator fun <S, O> set(key: Key4<S, O>, value: O)

    /** Removes the preference with the given key from this MutablePreferences. If this
     * Preferences does not contain the key, this is a no-op.
     *
     * Example usage:
     * mutablePrefs -= COUNTER_KEY
     *
     * @param key the key to remove from this MutablePreferences
     */
    operator fun minusAssign(key: Key)

    /**
     * Returns true if this Preferences contains the specified key.
     *
     * @param key the key to check for
     */
    @WorkerThread
    operator fun contains(key: Key): Boolean

    /** Removes all preferences from this MutablePreferences. */
    fun clear(x: MutablePreferences)

    /**
     * Remove a preferences from this MutablePreferences.
     */
    fun remove(key: Key)

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
 * Collects blocking [runBlocking] the [Flow.first] emitted by [Flow].
 * * Note: The terminal operator that returns the first element emitted by the flow and then cancels flow's collection.
 */
inline fun <S> Preferences.value(key: Key1<S>) =
    runBlocking { observe(key).first() }

inline fun <S> Preferences.value(key: Key2<S>) =
    runBlocking { observe(key).first() }

inline fun <S, O> Preferences.value(key: Key3<S, O>) =
    runBlocking { observe(key).first() }

inline fun <S, O> Preferences.value(key: Key4<S, O>) =
    runBlocking { observe(key).first() }

@Composable
inline operator fun <S> Preferences.get(key: Key1<S>) =
    observe(key).collectAsState(initial = value(key))

@Composable
inline operator fun <S> Preferences.get(key: Key2<S>) =
    observe(key).collectAsState(initial = value(key))

@Composable
inline operator fun <S, O> Preferences.get(key: Key3<S, O>) =
    observe(key).collectAsState(initial = value(key))

@Composable
inline operator fun <S, O> Preferences.get(key: Key4<S, O>) =
    observe(key).collectAsState(initial = value(key))

/**
 * Saves value to/from original.
 */
typealias IntSaver<O> = Saver<Int, O>

/**
 * @see [intPreferencesKey]
 */
fun intPreferenceKey(name: String) = Key1(intPreferencesKey(name))

/**
 * @see [intPreferencesKey]
 */
fun intPreferenceKey(name: String, defaultValue: Int): Key2<Int> =
    Key2(intPreferencesKey(name), defaultValue)

/**
 * @see [intPreferencesKey]
 */
fun <O> intPreferenceKey(name: String, saver: IntSaver<O>) =
    Key3(intPreferencesKey(name), saver)

/**
 * @see [intPreferencesKey]
 */
fun <O> intPreferenceKey(
    name: String,
    defaultValue: O,
    saver: IntSaver<O>
) = Key4(
    intPreferencesKey(name),
    defaultValue,
    saver
)

/**
 * Saves value to/from original.
 */
typealias DoubleSaver<O> = Saver<Double, O>

/**
 * @see [doublePreferencesKey]
 */
fun doublePreferenceKey(name: String) = Key1(doublePreferencesKey(name))

/**
 * @see [doublePreferencesKey]
 */
fun doublePreferenceKey(name: String, defaultValue: Double): Key2<Double> =
    Key2(doublePreferencesKey(name), defaultValue)

/**
 * @see [doublePreferencesKey]
 */
fun <O> doublePreferenceKey(name: String, saver: DoubleSaver<O>) =
    Key3(doublePreferencesKey(name), saver)

/**
 * @see [doublePreferencesKey]
 */
fun <O> doublePreferenceKey(
    name: String,
    defaultValue: O,
    saver: DoubleSaver<O>
) = Key4(
    doublePreferencesKey(name),
    defaultValue,
    saver
)


/**
 * Saves value to/from original.
 */
typealias FloatSaver<O> = Saver<Float, O>

/**
 * @see [floatPreferencesKey]
 */
fun floatPreferenceKey(name: String) = Key1(floatPreferencesKey(name))

/**
 * @see [floatPreferencesKey]
 */
fun floatPreferenceKey(name: String, defaultValue: Float): Key2<Float> =
    Key2(floatPreferencesKey(name), defaultValue)

/**
 * @see [floatPreferencesKey]
 */
fun <O> floatPreferenceKey(name: String, saver: FloatSaver<O>) =
    Key3(floatPreferencesKey(name), saver)

/**
 * @see [floatPreferencesKey]
 */
fun <O> floatPreferenceKey(
    name: String,
    defaultValue: O,
    saver: FloatSaver<O>
) = Key4(
    floatPreferencesKey(name),
    defaultValue,
    saver
)


/**
 * Saves value to/from original.
 */
typealias LongSaver<O> = Saver<Long, O>

/**
 * @see [longPreferencesKey]
 */
fun longPreferenceKey(name: String) = Key1(longPreferencesKey(name))

/**
 * @see [longPreferencesKey]
 */
fun longPreferenceKey(name: String, defaultValue: Long): Key2<Long> =
    Key2(longPreferencesKey(name), defaultValue)

/**
 * @see [longPreferencesKey]
 */
fun <O> longPreferenceKey(name: String, saver: LongSaver<O>) =
    Key3(longPreferencesKey(name), saver)

/**
 * @see [longPreferencesKey]
 */
fun <O> longPreferenceKey(
    name: String,
    defaultValue: O,
    saver: LongSaver<O>
) = Key4(
    longPreferencesKey(name),
    defaultValue,
    saver
)

typealias BooleanSaver<O> = Saver<Boolean, O>

/**
 * @see [booleanPreferencesKey]
 */
fun booleanPreferenceKey(name: String) = Key1(booleanPreferencesKey(name))

/**
 * @see [booleanPreferencesKey]
 */
fun booleanPreferenceKey(name: String, defaultValue: Boolean): Key2<Boolean> =
    Key2(booleanPreferencesKey(name), defaultValue)

/**
 * @see [booleanPreferencesKey]
 */
fun <O> booleanPreferenceKey(name: String, saver: BooleanSaver<O>) =
    Key3(booleanPreferencesKey(name), saver)

/**
 * @see [booleanPreferencesKey]
 */
fun <O> booleanPreferenceKey(
    name: String,
    defaultValue: O,
    saver: BooleanSaver<O>
) = Key4(
    booleanPreferencesKey(name),
    defaultValue,
    saver
)

typealias StringSaver<O> = Saver<String, O>

/**
 * @see [stringPreferencesKey]
 */
fun stringPreferenceKey(name: String) =
    Key1(stringPreferencesKey(name))

/**
 * @see [stringPreferencesKey]
 */
fun stringPreferenceKey(name: String, defaultValue: String): Key2<String> =
    Key2(stringPreferencesKey(name), defaultValue)

/**
 * @see [stringPreferencesKey]
 */
fun <O> stringPreferenceKey(name: String, saver: StringSaver<O>) =
    Key3(stringPreferencesKey(name), saver)

/**
 * @see [stringPreferencesKey]
 */
fun <O> stringPreferenceKey(
    name: String,
    defaultValue: O,
    saver: StringSaver<O>
) = Key4(
    stringPreferencesKey(name),
    defaultValue,
    saver
)

///String set
fun stringSetPreferenceKey(name: String) = Key1(stringSetPreferencesKey(name))

fun stringSetPreferenceKey(name: String, defaultValue: Set<String>) =
    Key2(stringSetPreferencesKey(name), defaultValue)

val LocalPreferenceStore = staticCompositionLocalOf<Preferences> {
    error("No Preferences Store provided.")
}