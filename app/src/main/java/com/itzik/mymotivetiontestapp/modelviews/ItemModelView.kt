package com.itzik.mymotivetiontestapp.modelviews

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.itzik.mymotivetiontestapp.modelviews.datamodel.ItemData
import com.itzik.repository.CallbackEventItem
import com.itzik.repository.IRepository
import com.itzik.repository.RepositoryDataImpl
import com.itzik.repository.Status

class ItemModelView(private val app: Application ) : AndroidViewModel (app){

    private val callback = object : CallbackEventItem {
        override fun onResponseItem(res: Status) {

            when(res) {
                is Status.SUCCESSES -> {
                    Log.d(TAG,"onResponseItem: SUCCESSES res [${res.item}]")
                    observerItems.postValue(ItemData(false, res.item))
                }

                is Status.FAILED -> {
                    Log.w(TAG,"onResponseItem: FAILED response [${res.cause}]")
                    observerItems.postValue(ItemData(true))
                }
            }
        }
    }

    private var observerItems = MutableLiveData<ItemData>()
    private  var repository: IRepository

    init {
        repository = RepositoryDataImpl()
        repository.registeredEvent(callback)
    }



    override fun onCleared() {
        super.onCleared()
        repository.stopGetData()
        repository.deregisteredEvent(callback)
        repository.destroy()
    }

    fun getObserverItems(): LiveData<ItemData> {
        return observerItems
    }

    fun onStartRequestItems() {
        repository.startGetData()
    }

    fun onStopRequestItems() {
        repository.stopGetData()
    }

    companion object {
        private val TAG = "itzik-${ItemModelView::class.java.simpleName}"
    }
}