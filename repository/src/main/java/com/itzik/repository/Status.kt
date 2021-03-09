package com.itzik.repository

import com.itzik.repository.network.Item
import java.lang.Exception

sealed class Status {
    data class SUCCESSES(val item: Item) : Status()
    data class FAILED(val cause: Exception) : Status()
}