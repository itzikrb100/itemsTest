package com.itzik.mymotivetiontestapp.views


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.itzik.mymotivetiontestapp.R
import com.itzik.mymotivetiontestapp.databinding.ItemListMainBinding
import com.itzik.mymotivetiontestapp.modelviews.ItemModelView
import com.itzik.mymotivetiontestapp.modelviews.ViewModelFactory
import com.itzik.mymotivetiontestapp.views.adapter.ItemsAdapter


class ItemsListActivity : AppCompatActivity() {


    private var viewModel: ItemModelView? = null
    private var  itemsActivity : ItemListMainBinding? = null
    private var itemAdapter: ItemsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemsActivity =  DataBindingUtil.setContentView(this, R.layout.item_list_main)
        initView()
        viewModel = ViewModelProvider(this, ViewModelFactory(application))[ItemModelView::class.java]
        viewModel?.getObserverItems()?.observe(this, Observer {
            Log.d(TAG,"change items data = [${it}]")
            if(!it.isError) {
                itemAdapter?.setItemAdapter(it)
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        when(id) {
            R.id.start_action -> {
                viewModel?.onStartRequestItems()
            }

            R.id.stop_action -> {
                viewModel?.onStopRequestItems()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        setSupportActionBar(itemsActivity?.myToolbar)
        itemAdapter = ItemsAdapter(ArrayList())
        itemAdapter?.registerAdapterDataObserver(object : AdapterDataObserver() {

            override fun onChanged() {
                super.onChanged()
                val sizeItems = itemAdapter?.itemCount ?: -1
                Log.d(TAG,"onChanged itemCount= ${sizeItems}")
                itemsActivity?.recyclerView?.smoothScrollToPosition(sizeItems)
            }

        })
        val lm = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true).also {
            it.stackFromEnd = true
        }
        itemsActivity?.recyclerView?.layoutManager = lm
        //itemsActivity?.recyclerView?.setHasFixedSize(true)
        itemsActivity?.recyclerView?.adapter = itemAdapter
    }


    companion object {
        private val TAG = "itzik-${ItemsListActivity::class.java.simpleName}"
    }
}

