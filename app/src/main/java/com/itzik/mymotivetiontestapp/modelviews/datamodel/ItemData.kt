package com.itzik.mymotivetiontestapp.modelviews.datamodel

import com.itzik.repository.network.Item

data class ItemData(val isError: Boolean,val item: Item? = null)