/*
 * Created by IMStudio on 5/11/21 10:37 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common.functional

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Result] are either an instance of [Success] or [Failure].
 * FP Convention dictates that [Success] is used for "failure"
 * and [Failure] is used for "success".
 *
 * @see Success
 * @see Failure
 */
sealed class Result<out L, out R> {

    object Idle : Result<Nothing, Nothing>()

    object Loading : Result<Nothing, Nothing>()

    /** * Represents the left side of [Result] class which by convention is a "Failure". */
    data class Success<out L>(val data: L) : Result<L, Nothing>()

    /** * Represents the right side of [Result] class which by convention is a "Success". */
    data class Failure<out R>(val error: R) : Result<Nothing, R>()

    /**
     * Returns true if this is a Failure, false otherwise.
     * @see Failure
     */
    val isFailure get() = this is Failure<R>

    /**
     * Returns true if this is a Success, false otherwise.
     * @see Success
     */
    val isSuccess get() = this is Success<L>

    /**
     * Returns true if this is a Loading
     * @see Loading
     */
    val isLoading get() = this is Loading

    /**
     * Creates a Success type.
     * @see Success
     */
    fun <L> success(data: L) = Success(data)

    /**
     * Creates a Success type.
     * @see Failure
     */
    fun <R> failure(error: R) = Failure(error)

    /**
     * Applies fnL if this is a Success or fnR if this is a Failure.
     * @see Success
     * @see Failure
     */
    fun fold(
        onSuccess: (L) -> Unit,
        onFailure: ((R) -> Unit)? = null,
        onLoading: (() -> Unit)? = null
    ) {
        when (this) {
            is Success -> onSuccess(data)
            is Failure -> onFailure?.invoke(error)
            is Loading -> onLoading?.invoke()
        }
    }
}

/**
 * Success-biased onSuccess() FP convention dictates that when this class is Success, it'll perform
 * the onSuccess functionality passed as a parameter, but, overall will still return an either
 * object so you chain calls.
 */
inline fun <L, R> Result<L, R>.onSuccess(func: (success: L) -> Unit): Result<L, R> =
    this.apply {
        if (this is Result.Success) func(data)
    }

/**
 * Failure-biased onFailure() FP convention dictates that when this class is Failure, it'll perform
 * the onFailure functionality passed as a parameter, but, overall will still return an either
 * object so you chain calls.
 */
inline fun <L, R> Result<L, R>.onFailure(func: (failure: R) -> Unit): Result<L, R> =
    this.apply { if (this is Result.Failure) func(error) }

/**
 * Success-biased onLoading() FP convention dictates that when this class is Loading, it'll perform
 * the onLoading functionality passed as a parameter, but, overall will still return an either
 * object so you chain calls.
 */
inline fun <L, R> Result<L, R>.onLoading(loading: () -> Unit, dismiss: () -> Unit): Result<L, R> =
    this.apply {
        if (this is Result.Loading)
            loading()
        else
            dismiss()
    }

inline fun <L, R> Result<L, R>.data(func: (success: L?) -> Unit): Result<L, R> =
    this.apply {
        if (this is Result.Success) {
            func(data)
        } else {
            func(null)
        }
    }