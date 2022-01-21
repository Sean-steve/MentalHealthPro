package com.sean_steve.mentalhealthpro.data


import com.sean_steve.mentalhealthpro.data.Results.Success

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Results<out R> {

    data class Success<out T>(val data: T) : Results<T>()
    data class Error(val exception: Exception) : Results<Nothing>()
    object Loading : Results<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Success<*> && data != null
