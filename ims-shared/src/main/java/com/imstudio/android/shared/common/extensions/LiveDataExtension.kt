/*
 * Created by IMStudio on 5/11/21 10:37 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common.extensions

import androidx.lifecycle.*
import com.imstudio.android.shared.common.functional.unit

/**
 * Combines this [LiveData] with another [LiveData] using the specified [combiner] and returns the
 * result as a [LiveData].
 */
fun <A, B, Result> LiveData<A>.combine(
    other: LiveData<B>,
    combiner: (A, B) -> Result
): LiveData<Result> {
    val result = MediatorLiveData<Result>()
    result.addSource(this) { a ->
        val b = other.value
        if (b != null) {
            result.postValue(combiner(a, b))
        }
    }
    result.addSource(other) { b ->
        val a = this@combine.value
        if (a != null) {
            result.postValue(combiner(a, b))
        }
    }
    return result
}

fun <T1, T2> combine(f1: LiveData<T1>, f2: LiveData<T2>) =
    MediatorLiveData<Pair<T1?, T2?>>().also { mediator ->
        mediator.value = Pair(f1.value, f2.value)

        mediator.addSource(f1) { t1: T1? ->
            val (_, t2) = mediator.value!!
            mediator.value = Pair(t1, t2)
        }

        mediator.addSource(f2) { t2: T2? ->
            val (t1, _) = mediator.value!!
            mediator.value = Pair(t1, t2)
        }
    }

fun validateBy(f1: LiveData<Boolean>, f2: LiveData<Boolean>) =
    Transformations.map(combine(f1, f2)) { (t1, t2) ->
        (t1 ?: false) && (t2 ?: false)
    }

/*
* Combines this [LiveData] with other two [LiveData]s using the specified [combiner] and returns
* the result as a [LiveData].
*/
fun <A, B, C, Result> LiveData<A>.combine(
    other1: LiveData<B>,
    other2: LiveData<C>,
    combiner: (A, B, C) -> Result
): LiveData<Result> {
    val result = MediatorLiveData<Result>()
    result.addSource(this) { a ->
        val b = other1.value
        val c = other2.value
        if (b != null && c != null) {
            result.postValue(combiner(a, b, c))
        }
    }
    result.addSource(other1) { b ->
        val a = this@combine.value
        val c = other2.value
        if (a != null && c != null) {
            result.postValue(combiner(a, b, c))
        }
    }
    result.addSource(other2) { c ->
        val a = this@combine.value
        val b = other1.value
        if (a != null && b != null) {
            result.postValue(combiner(a, b, c))
        }
    }
    return result
}

fun <X, Y> LiveData<X>.map(func: (X?) -> Y?): MutableLiveData<Y?> {
    return MediatorLiveData<Y>().apply {
        addSource(this@map) { x -> value = func(x) }
    }
}

fun <X, Y> LiveData<X>.mapSkipNulls(func: (X) -> Y): MutableLiveData<Y> {
    return MediatorLiveData<Y>().apply {
        addSource(this@mapSkipNulls) { x ->
            x ?: return@addSource
            value = func(x)
        }
    }
}

fun <A, B> LiveData<A>.combineLatest(b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        addSource(this@combineLatest) {
            if (it == null && value != null) value = null
            lastA = it
            if (lastA != null && lastB != null) value = lastA!! to lastB!!
        }

        addSource(b) {
            if (it == null && value != null) value = null
            lastB = it
            if (lastA != null && lastB != null) value = lastA!! to lastB!!
        }
    }
}

fun <A, B, C> combineLatest(
    a: LiveData<A>,
    b: LiveData<B>,
    c: LiveData<C>
): LiveData<Triple<A?, B?, C?>> {

    fun Triple<A?, B?, C?>?.copyWithFirst(first: A?): Triple<A?, B?, C?> {
        if (this@copyWithFirst == null) return Triple<A?, B?, C?>(first, null, null)
        return this@copyWithFirst.copy(first = first)
    }

    fun Triple<A?, B?, C?>?.copyWithSecond(second: B?): Triple<A?, B?, C?> {
        if (this@copyWithSecond == null) return Triple<A?, B?, C?>(null, second, null)
        return this@copyWithSecond.copy(second = second)
    }

    fun Triple<A?, B?, C?>?.copyWithThird(third: C?): Triple<A?, B?, C?> {
        if (this@copyWithThird == null) return Triple<A?, B?, C?>(null, null, third)
        return this@copyWithThird.copy(third = third)
    }

    return MediatorLiveData<Triple<A?, B?, C?>>().apply {
        addSource(a) { value = value.copyWithFirst(it) }
        addSource(b) { value = value.copyWithSecond(it) }
        addSource(c) { value = value.copyWithThird(it) }
    }
}

/**
 * observeOnce
 * */
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

inline fun <T> LifecycleOwner.observeOnce(
    liveData: LiveData<T>,
    crossinline observer: unit<T>
) {
    liveData.observeOnce(this, {
        it?.let { t -> observer(t) }
    })
}

/**
 * Adds the given observer to the observers list within the lifespan of the given
 * owner. The events are dispatched on the main thread. If LiveData already has data
 * set, it will be delivered to the observer.
 *
 * @param liveData The liveData to observe.
 * @param observer The observer that will receive the events.
 * @see LiveData.observe
 */
inline fun <T> LifecycleOwner.observe(liveData: LiveData<T>, crossinline observer: unit<T>) {
    liveData.observe(this, Observer {
        it?.let { t -> observer(t) }
    })
}

/**
 * Adds the given observer to the observers list within the lifespan of the given
 * owner. The events are dispatched on the main thread. If LiveData already has data
 * set, it will be delivered to the observer.
 *
 * @param liveData The mutableLiveData to observe.
 * @param observer The observer that will receive the events.
 * @see MutableLiveData.observe
 */
inline fun <T> LifecycleOwner.observe(
    liveData: MutableLiveData<T>,
    crossinline observer: unit<T>
) {
    liveData.observe(this, Observer {
        it?.let { t -> observer(t) }
    })
}

fun <T> MutableLiveData<T>.triggerObservers() {
    this.value = this.value
}

