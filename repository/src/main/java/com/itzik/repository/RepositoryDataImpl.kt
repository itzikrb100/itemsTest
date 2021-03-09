package com.itzik.repository

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.itzik.repository.network.ApiWsClient
import com.itzik.repository.network.Item
import okhttp3.WebSocket
import java.util.concurrent.CopyOnWriteArrayList


class RepositoryDataImpl() : IRepository {

    private var apiWebSocketItem: WebSocket? = null
    private var handlerThread: HandlerThread
    private var handler: HandlerRequest?
    private var lockRequest: Any
    private var canStartRequest: Boolean
    private var listCallbacks: List<CallbackEventItem>



    init {
        canStartRequest = false
        listCallbacks = CopyOnWriteArrayList()
        lockRequest = Any()
        handlerThread = HandlerThread("RepositoryDataImpl")
        handlerThread.start()
        handler = HandlerRequest(handlerThread.looper)
        ApiWsClient.getInstance()?.setHandler(handler as Handler)
    }

    override fun deregisteredEvent(callbackEvent: CallbackEventItem) {
        if (!listCallbacks.isEmpty()) {
            (listCallbacks as CopyOnWriteArrayList).remove(callbackEvent)
        }
    }

    override fun registeredEvent(callbackEvent: CallbackEventItem) {
        if (!listCallbacks.contains(callbackEvent)) {
            (listCallbacks as CopyOnWriteArrayList).add(callbackEvent)
        }
    }

    override fun destroy() {
        synchronized(lockRequest) {
            handlerThread.quitSafely()
            handler?.removeCallbacksAndMessages(null)
        }
        handler = null
    }

    override fun startGetData() {
        synchronized(lockRequest) {
            if (!canStartRequest) {
                canStartRequest = true
                apiWebSocketItem = ApiWsClient.getInstance()?.createApi()
            }
        }
    }

    override fun stopGetData() {
        synchronized(lockRequest) {
            stopGetDataInternal()
        }
    }

    private fun stopGetDataInternal() {
        if (canStartRequest) {
            canStartRequest = false
            apiWebSocketItem?.cancel()
        }
    }

    private fun getDataInternal(jsonResponse: String): Status {
        val gsonBuilder = GsonBuilder().create()
        var status: Status
        try {
            val item =  gsonBuilder.fromJson(jsonResponse, Item::class.java)
            status = Status.SUCCESSES(item)
        }catch (e: JsonSyntaxException) {
            Log.e(TAG,"Failed to create item, cause: ${e.message}")
            status = Status.FAILED(Exception(e.message))
        }
        return status
    }

    private fun notifyResponse(resStatus: Status) {
        listCallbacks.forEach { it.onResponseItem(resStatus) }
    }


    inner class HandlerRequest(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when (STATE.values()[msg.what]) {
                STATE.GET_MESSAGE -> {
                    synchronized(lockRequest) {
                        internalHandle(msg.obj as String)
                    }
                }
                else -> {Log.w(TAG,"Not recognize msg handle")}
            }

        }

        private fun internalHandle(responseJson: String) {
            val response = getDataInternal(responseJson)
            notifyResponse(response)
        }
    }

    enum class STATE {
        GET_MESSAGE
    }


    companion object {
        private val TAG = "itzik-${RepositoryDataImpl::class.java.simpleName}"
    }
}