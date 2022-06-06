package com.tardigrade.capstonebangkit.misc

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>() {
        private var hasBeenHandled = false

        fun getContentIfNotHandled(): T? {
            return if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                data
            }
        }
    }
    data class Error(val error: String) : Result<Nothing>() {
        private var errorHasBeenHandled = false

        fun getErrorIfNotHandled(): String? {
            return if (errorHasBeenHandled) {
                null
            } else {
                errorHasBeenHandled = true
                error
            }
        }
    }
    object Loading : Result<Nothing>()
}
