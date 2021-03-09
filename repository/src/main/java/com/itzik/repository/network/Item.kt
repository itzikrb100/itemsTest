package com.itzik.repository.network

import com.google.gson.annotations.SerializedName

data class Item (@SerializedName("weight") val weight: String,
                 @SerializedName("name") val name: String,
                 @SerializedName("bagColor") val bagColor: String){

     override fun toString(): String {
         return "weight = [${weight}], name = [${name}], bagColor = [${bagColor}]"
     }
}