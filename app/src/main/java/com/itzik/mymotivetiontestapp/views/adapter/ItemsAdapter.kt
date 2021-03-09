package com.itzik.mymotivetiontestapp.views.adapter

import android.content.res.ColorStateList.*
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.itzik.mymotivetiontestapp.R
import com.itzik.mymotivetiontestapp.modelviews.datamodel.ItemData
import kotlin.collections.ArrayList

class ItemsAdapter(items: ArrayList<ItemData>) : RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {


     private var itemList: List<ItemData> = items


     override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
            val data = itemList[position]
            holder.bindView(data, position)
            Log.d(TAG,"onBindViewHolder: data = ${data.item}")
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
         val view = LayoutInflater.from(parent.context).inflate(R.layout.item_raw, parent, false)
         return ItemsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    fun setItemAdapter(items: ItemData) {
        (itemList as ArrayList).add(items)
        notifyDataSetChanged()
        Log.d(TAG,"setItemAdapter")
    }



     inner class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

          fun bindView(itemData: ItemData, pos: Int) = with(itemView){
               findViewById<MaterialButton>(R.id.circle).setBackgroundColor(Color.parseColor(itemData.item?.bagColor))
               findViewById<TextView>(R.id.title).text = itemData.item?.name
               findViewById<TextView>(R.id.wight).text = itemData.item?.weight
          }
     }


      companion object {
          private  val TAG = "itzik-${ItemsAdapter::class.java.simpleName}"
      }
}