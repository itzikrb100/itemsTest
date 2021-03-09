package com.itzik.mymotivetiontestapp.modelviews

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.Exception

class ViewModelFactory (private val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemModelView::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemModelView(application) as T
        }

        throw Exception("Unknown ViewModel class")
    }
}