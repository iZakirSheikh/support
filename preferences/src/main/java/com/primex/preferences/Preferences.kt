package com.primex.preferences

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.datastore.preferences.core.Preferences as StorePreference

internal typealias StoreKey<T> = StorePreference.Key<T>

/**
 * The [Key]
 */
data class Key<T> internal constructor(internal val storeKey: StoreKey<T>) {
    @JvmField
    val name = storeKey.name
}

/**
 * [Key] with default value
 */
data class Key1<T> internal constructor(
    internal val storeKey: StoreKey<T>,
    internal val default: T
) {
    @JvmField
    val name = storeKey.name
}

/**
 * [Key] with [Saver]
 */
data class Key2<T, O> internal constructor(
    internal val storeKey: StoreKey<T>,
    internal val saver: Saver<O, T>
) {
    @JvmField
    val name = storeKey.name
}

/**
 * [Key] with default value and [Saver]
 */
data class Key3<T, O> internal constructor(
    internal val storeKey: StoreKey<T>,
    internal val default: O,
    internal val saver: Saver<O, T>
) {
    @JvmField
    val name = storeKey.name
}

/**
 * The [Saver] describes how the object of [Original] class can be simplified and converted into
 * something which is [Saveable].
 */
interface Saver<Original, Saveable> {
    /**
     * Convert the value into a saveable one.
     */
    fun save(value: Original): Saveable

    /**
     * Convert the restored value back to the original Class.
     */
    fun restore(value: Saveable): Original
}

/**
 * Saves value to/from original.
 */
typealias IntSaver<O> = Saver<O, Int>

fun intPreferenceKey(name: String): Key<Int> = Key(intPreferencesKey(name))

fun intPreferenceKey(name: String, defaultValue: Int): Key1<Int> =
    Key1(intPreferencesKey(name), defaultValue)


fun <O> intPreferenceKey(name: String, saver: IntSaver<O>) =
    Key2(intPreferencesKey(name), saver)

fun <O> intPreferenceKey(
    name: String,
    defaultValue: O,
    saver: IntSaver<O>
) = Key3(
    intPreferencesKey(name),
    defaultValue,
    saver
)

/**
 * Maps value to/from [Double]
 */
typealias DoubleSaver<O> = Saver<O, Double>

fun doublePreferenceKey(name: String): Key<Double> = Key(doublePreferencesKey(name))

fun doublePreferenceKey(name: String, defaultValue: Double): Key1<Double> =
    Key1(doublePreferencesKey(name), defaultValue)

fun <O> doublePreferenceKey(name: String, saver: DoubleSaver<O>) =
    Key2(doublePreferencesKey(name), saver)

fun <O> doublePreferenceKey(
    name: String,
    defaultValue: O,
    saver: DoubleSaver<O>
) = Key3(
    doublePreferencesKey(name),
    defaultValue,
    saver
)


/**
 * Maps value to/from [Float].
 */
typealias FloatSaver<O> = Saver<O, Float>

fun floatPreferenceKey(name: String): Key<Float> = Key(floatPreferencesKey(name))

fun floatPreferenceKey(name: String, defaultValue: Float): Key1<Float> =
    Key1(floatPreferencesKey(name), defaultValue)

fun <O> floatPreferenceKey(name: String, saver: FloatSaver<O>) =
    Key2(floatPreferencesKey(name), saver)

fun <O> floatPreferenceKey(
    name: String,
    defaultValue: O,
    saver: FloatSaver<O>
) = Key3(
    floatPreferencesKey(name),
    defaultValue,
    saver
)

/**
 * Maps value to/from [Long].
 */
typealias LongSaver<O> = Saver<O, Long>

fun longPreferenceKey(name: String): Key<Long> = Key(longPreferencesKey(name))

fun longPreferenceKey(name: String, defaultValue: Long): Key1<Long> =
    Key1(longPreferencesKey(name), defaultValue)

fun <O> longPreferenceKey(name: String, saver: LongSaver<O>) =
    Key2(longPreferencesKey(name), saver)

fun <O> longPreferenceKey(
    name: String,
    defaultValue: O,
    saver: LongSaver<O>
) = Key3(
    longPreferencesKey(name),
    defaultValue,
    saver
)

/**
 * Maps value to/from [Boolean].
 */
typealias BooleanSaver<O> = Saver<O, Boolean>

fun booleanPreferenceKey(name: String): Key<Boolean> = Key(booleanPreferencesKey(name))

fun booleanPreferenceKey(name: String, defaultValue: Boolean): Key1<Boolean> =
    Key1(booleanPreferencesKey(name), defaultValue)

fun <O> booleanPreferenceKey(name: String, saver: BooleanSaver<O>) =
    Key2(booleanPreferencesKey(name), saver)

fun <O> booleanPreferenceKey(
    name: String,
    defaultValue: O,
    saver: BooleanSaver<O>
) = Key3(
    booleanPreferencesKey(name),
    defaultValue,
    saver
)

/**
 * Maps value to/from [String].
 */
typealias StringSaver<O> = Saver<O, String>

fun stringPreferenceKey(name: String): Key<String> = Key(stringPreferencesKey(name))

fun stringPreferenceKey(name: String, defaultValue: String): Key1<String> =
    Key1(stringPreferencesKey(name), defaultValue)

fun <O> stringPreferenceKey(name: String, saver: StringSaver<O>) =
    Key2(stringPreferencesKey(name), saver)

fun <O> stringPreferenceKey(
    name: String,
    defaultValue: O,
    saver: StringSaver<O>
) = Key3(
    stringPreferencesKey(name),
    defaultValue,
    saver
)

///String set
fun stringSetPreferenceKey(name: String) = Key(stringSetPreferencesKey(name))

fun stringSetPreferenceKey(name: String, defaultValue: Set<String>): Key1<Set<String>> =
    Key1(stringSetPreferencesKey(name), defaultValue)

interface Preferences {

    /**
     * Returns [Flow] of type T ([String], [Float], [Double], [Int], [Long], String [Set]).
     *
     * Note: This can be nullable if [key] doesn't exist.
     */
    operator fun <T> get(key: Key<T>): Flow<T?>

    /**
     * Note: emits [defaultValue] if orginal not exists.
     * @see get
     */
    operator fun <T> get(key: Key1<T>): Flow<T>

    /**
     * [Flow] of type Any Type.
     *
     * The key contains [TwoWayConverter], which helps in conversion from [O] Original to Savable
     * Note: Savable are of Type ([String], [Float], [Double], [Int], [Long], String [Set]).
     * This can be nullable if [key] doesn't exist.
     */
    operator fun <T, O> get(key: Key2<T, O>): Flow<O?>

    /**
     * **Note returns defaultValue if null**
     *
     * @see get
     */
    operator fun <T, O> get(key: Key3<T, O>): Flow<O>

    // Mutating methods below:

    /**
     * Set a key value pair in MutablePreferences.
     *
     * Example usage:
     * val COUNTER_KEY = intPreferencesKey("counter")
     *
     * // Once edit completes successfully, preferenceStore will contain the incremented counter.
     * preferenceStore.edit { prefs: MutablePreferences ->
     *   prefs\[COUNTER_KEY\] = prefs\[COUNTER_KEY\] :? 0 + 1
     * }
     *
     * @param key the preference to set
     * @param key the value to set the preference to
     */
    operator fun <T> set(key: Key<T>, value: T)

    /**
     * @see [set]
     */
    operator fun <T> set(key: Key1<T>, value: T)

    /**
     * @see [set]
     */
    operator fun <T, O> set(key: Key2<T, O>, value: O)

    /**
     * @see [set]
     */
    operator fun <T, O> set(key: Key3<T, O>, value: O)


    /**
     * Removes the preference with the given key from this MutablePreferences. If this
     * Preferences does not contain the key, this is a no-op.
     *
     * Example usage:
     * mutablePrefs -= COUNTER_KEY
     *
     * @param key the key to remove from this MutablePreferences
     */
    operator fun <T> minusAssign(key: Key<T>)

    operator fun <T> minusAssign(key: Key1<T>)

    operator fun <T, O> minusAssign(key: Key2<T, O>)

    operator fun <T, O> minusAssign(key: Key3<T, O>)


    /**
     * Returns true if this Preferences contains the specified key.
     *
     * @param key the key to check for
     */
    @WorkerThread
    operator fun <T> contains(key: Key<T>): Boolean

    operator fun <T> contains(key: Key1<T>): Boolean

    operator fun <T, O> contains(key: Key2<T, O>): Boolean

    operator fun <T, O> contains(key: Key3<T, O>): Boolean


    /**
     * Collects blocking [runBlocking] the [Flow.first] emitted by [Flow].
     *
     * * Note: The terminal operator that returns the first element emitted by the flow and then cancels flow's collection.*
     */
    fun <T> Flow<T>.obtain(): T = runBlocking { first() }

    /**
     * [collectAsState()]
     * requires
     * @exception NoSuchMethodError if dependency [androidx.compose.runtime] not found.
     */
    @Composable
    fun <T> Flow<T>.observeAsState() = collectAsState(initial = obtain())

    companion object {

        // Singleton prevents multiple instances of repository opening at the
        // same time.
        private const val TAG = "Preferences"

        @Volatile
        private var INSTANCE: Preferences? = null

        @Deprecated("Create instance and use with Hilt")
        fun get(context: Context): Preferences {

            // if the INSTANCE is not null, then return it,
            // if it is, then create the repository
            return INSTANCE ?: synchronized(this) {
                val instance = PreferencesImpl(context.applicationContext as Application)
                INSTANCE = instance
                instance
            }
        }
    }
}


/**
 * Builds the instance of The preference data store.
 */
fun Preferences(context: Context): Preferences =
    PreferencesImpl(context.applicationContext as Application)


val LocalPreferenceStore = compositionLocalOf<Preferences> {
    error("No Preferences Store provided.")
}