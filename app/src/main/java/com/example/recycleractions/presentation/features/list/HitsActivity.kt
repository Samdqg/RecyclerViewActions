package com.example.recycleractions.presentation.features.list

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.recycleractions.R
import com.example.recycleractions.domain.entities.Hit
import com.example.recycleractions.domain.entities.HitInput
import com.example.recycleractions.domain.entities.HitResponse
import com.example.recycleractions.presentation.features.detail.HitDetailActivity
import com.example.recycleractions.presentation.helpers.NetworkManager
import com.example.recycleractions.presentation.helpers.SwipeToDeleteCallback
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_hits.*


class HitsActivity : AppCompatActivity(), HitListener, SwipeRefreshLayout.OnRefreshListener{

    private lateinit var viewModel: HitViewModel
    private lateinit var hitsAdapter: HitsAdapter
    lateinit var sharedPref: SharedPreferences
    private var loading: Boolean = false
    private var currentPage: Int = 0

    companion object{
        const val HIT_URL = "hit_url"
        const val LAST_DATA = "last_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hits)
        viewModel = ViewModelProvider(this).get(HitViewModel::class.java)
        sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        initObservers()
        initRecycler()
        initSwipeContainer()
        showLoading()
        getData(0)

    }

    private fun initObservers() {
        viewModel.getHitResponse().observe(this, Observer {
            hideLoading()
            hideFooterLoading()
            handleResponse(it)
        })

        viewModel.getException().observe(this, Observer {
            loading = false
            hideLoading()
            hideFooterLoading()
            swipeContainer.isRefreshing = false
            showErrorMessage(it)
        })
    }

    private fun getData(page: Int){
        if(NetworkManager.isConnected(this)){
            viewModel.getHits(HitInput(getString(R.string.query), page))
        }else{
            getLocalData()
        }

    }

    private fun getLocalData() {
        val gson = Gson()
        val response =  sharedPref.getString(LAST_DATA, "")
        val data: HitResponse
        if(response!!.isNotEmpty()){
            data = gson.fromJson(response, HitResponse::class.java)
            hitsAdapter.clear()
            hitsAdapter.addItems(getFilteredList(data.hits))

        }else{
            Toast.makeText(this, getString(R.string.no_data_available), Toast.LENGTH_LONG).show()
        }
        hideLoading()
        hideFooterLoading()
        swipeContainer.isRefreshing = false
    }

    private fun handleResponse(response: HitResponse) {
        loading = false
        currentPage = response.page
        swipeContainer.isRefreshing = false
        if(response.page==0){
            hitsAdapter.clear()
            saveLastData(response)
        }
        val hits = getFilteredList(response.hits)
        hitsAdapter.addItems(hits.sortedByDescending {
            it.created_at
        })


    }

    private fun saveLastData(response: HitResponse) {
        val gson = Gson()
        val json = gson.toJson(response)
        savePreference(json)
    }

    private fun initRecycler() {
        hitsAdapter = HitsAdapter(this, ArrayList(), this)
        rvHits.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            this,
            LinearLayoutManager.VERTICAL
        )
        rvHits.addItemDecoration(dividerItemDecoration)
        rvHits.addOnScrollListener(onScrollRecycler())
        rvHits.adapter = hitsAdapter
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(hitsAdapter, this, this))
        itemTouchHelper.attachToRecyclerView(rvHits)
    }

    private fun initSwipeContainer() {
        swipeContainer.setOnRefreshListener(this)
        swipeContainer.setColorSchemeResources(R.color.colorAccent)
        swipeContainer.setProgressBackgroundColorSchemeResource(android.R.color.white)
    }

    private fun onScrollRecycler(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (dy > 0) {
                    val visibleItemCount = linearLayoutManager!!.childCount
                    val offset = linearLayoutManager.itemCount
                    val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()
                    if (!loading && visibleItemCount + pastVisibleItems >= offset) {
                        loading = true
                        showFooterLoading()
                        getData(currentPage+1)
                    }
                }
            }
        }
    }

    private fun showFooterLoading() {
        pbLoadingFooter.visibility = View.VISIBLE
    }

    private fun hideFooterLoading(){
        pbLoadingFooter.visibility = View.GONE
    }

    private fun showErrorMessage(e: Exception){
        Toast.makeText(this, "Error "+e.message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(){
        pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        pbLoading.visibility = View.GONE
    }

    override fun onclick(hit: Hit) {
        val url = getHitUrl(hit)
        if (!url.isNullOrEmpty()){
            val intent = Intent(this, HitDetailActivity::class.java)
            intent.putExtra(HIT_URL, url)
            startActivity(intent)
        }else{
           Toast.makeText(this, getString(R.string.not_available), Toast.LENGTH_LONG).show()
        }
    }

    private fun getHitUrl(hit: Hit): String?{

        return if(hit.url.isNullOrEmpty()){
            hit.story_url
        }else{
            hit.url
        }
    }

    override fun onRefresh() {
        swipeContainer.isRefreshing = true
        getData(0)
    }

    override fun onDelete(hit: Hit) {
        savePreference(hit)
    }

    private fun savePreference(hit: Hit){
        val editor = sharedPref.edit()
        editor.putLong(hit.story_id.toString(), hit.story_id)
        editor.apply()
    }

    private fun savePreference(json: String){
        val editor = sharedPref.edit()
        editor.putString(LAST_DATA, json)
        editor.apply()
    }

    private fun getFilteredList(hits: List<Hit>): List<Hit>{
        val newHits = ArrayList<Hit>()
        var id: Long
        hits.forEach { hit ->
            id = sharedPref.getLong(hit.story_id.toString(), -1L)
            if (id < 0L){
                newHits.add(hit)
            }
        }
        return newHits
    }
}