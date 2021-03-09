package com.itzik.repository.network

import android.os.Handler
import android.os.Message
import android.util.Log
import com.itzik.repository.RepositoryDataImpl
import okhttp3.*

class ApiWsClient(private val baseUrl: String): IApiClient<WebSocket?> {

    private var handlerParent: Handler? = null
    private var client: OkHttpClient? = null
    private var socketListener: ItemWebSocketListener? = null

    init {
        client = OkHttpClient()
        socketListener = ItemWebSocketListener()
    }


    override fun createApi(): WebSocket? {
        val request: Request = Request.Builder().url(baseUrl).build()
        val listener = ItemWebSocketListener()
        val ws = client?.newWebSocket(request, listener)
        return ws
    }

    inner class ItemWebSocketListener(): WebSocketListener() {
        override fun onOpen(webSocket: WebSocket?, response: Response?) {
            super.onOpen(webSocket, response)
            Log.d(TAG,"ItemWebSocketListener")
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            Log.d(TAG,"onMessage: TEXT = ${text}")
            val message = Message.obtain()
            message.what = RepositoryDataImpl.STATE.GET_MESSAGE.ordinal
            message.obj = text
            handlerParent?.sendMessage(message)
        }

        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            Log.d(TAG,"onClosing")
            super.onClosing(webSocket, code, reason)
        }

        override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
            Log.d(TAG,"onFailure: cause = ${t}")
            super.onFailure(webSocket, t, response)
        }
    }

    companion object {

        private  var  TAG = "itzik-${ApiWsClient::class.java.simpleName}"

        private  var  HOLDER: ApiWsClient? = null
        private  val   BASE_URL = "ws://superdo-groceries.herokuapp.com/receive"

        @Synchronized
        fun  getInstance(): ApiWsClient? {
            if(HOLDER == null) {
                HOLDER = ApiWsClient(BASE_URL)
            }
            return HOLDER
        }
    }

    override fun setHandler(handler: Handler) {
        handlerParent = handler
    }
}