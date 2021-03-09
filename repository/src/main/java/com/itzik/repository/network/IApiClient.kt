package com.itzik.repository.network

import android.os.Handler

interface IApiClient<T> {
    fun createApi(): T
    fun setHandler(handlerParent: Handler) {}
}