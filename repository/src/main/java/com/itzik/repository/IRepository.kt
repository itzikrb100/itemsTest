package com.itzik.repository

interface IRepository {
    fun startGetData()
    fun stopGetData()
    fun destroy()
    fun registeredEvent(callbackEvent: CallbackEventItem)
    fun deregisteredEvent(callbackEvent: CallbackEventItem)
}