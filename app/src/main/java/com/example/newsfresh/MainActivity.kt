package com.example.newsfresh

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import java.util.*


class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter
    private var windowWidth = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                Toast.makeText(this@MainActivity, "hi", Toast.LENGTH_SHORT).show()
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (layoutManager != null && windowWidth !== 0) {
                    val firstPosition = layoutManager.findFirstVisibleItemPosition()
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (firstPosition == RecyclerView.NO_POSITION || lastPosition == RecyclerView.NO_POSITION) {
                        Log.e(
                            ContentValues.TAG,
                            java.lang.String.format(
                                Locale.US,
                                "onScrolled firstPosition %d lastPosition %d",
                                firstPosition,
                                lastPosition
                            )
                        )
                        return
                    }
                    for (pos in firstPosition..lastPosition) {
                        val child: View? = layoutManager.findViewByPosition(pos)
                        if (child != null) {
//                            val viewHolder: ViewHolder =
//                                recyclerView.getChildViewHolder(child) as ViewHolder
//                            viewHolder.computeAndSetWidth()
                        } else {
                            Log.e(
                                ContentValues.TAG,
                                java.lang.String.format(
                                    Locale.US,
                                    "onScrolled pos %d child == null",
                                    pos
                                )
                            )
                        }
                    }
                } else {
                    Log.e(
                        ContentValues.TAG,
                        java.lang.String.format(
                            Locale.US,
                            "onScrolled layoutManager %s windowWidth %d",
                            layoutManager,
                            windowWidth
                        )
                    )
                }
            }
        })
        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.initialPrefetchItemCount = 5
        recyclerView.layoutManager = layoutManager

//        recyclerView.layoutManager= LinearLayoutManager(this,
//            LinearLayoutManager.HORIZONTAL,
//            false)

        recyclerView.setHasFixedSize(true)
        fetchData()
        mAdapter = NewsListAdapter(this, recyclerView, 8f, 24f)

        recyclerView.adapter = mAdapter
        val helper: SnapHelper = LinearSnapHelper()
        helper.attachToRecyclerView(recyclerView)
        recyclerView.scheduleLayoutAnimation()

    }

    private fun fetchData() {
        val url =
            "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=5a46849416cc43bd9c51076059f5991b"
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val newsJsonArray = response.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newJsonObject.getString("title"),
                        newJsonObject.getString("author"),
                        newJsonObject.getString("url"),
                        newJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)

            },
            Response.ErrorListener { error ->

            }
        )
// overriding function to add Headers.
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    override fun onItemClicked(item: News) {
//        Toast.makeText(this,"Clicked item is $item",Toast.LENGTH_SHORT).show()
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        // customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }


}



