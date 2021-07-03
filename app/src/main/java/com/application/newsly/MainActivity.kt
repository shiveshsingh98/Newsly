package com.application.newsly

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.littlemango.stacklayoutmanager.StackLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var adapter:NewsAdapter
    private var article = mutableListOf<Article>()
    var pageNum=1
    var totalRes=-1
    var TAG="mainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter= NewsAdapter(this@MainActivity,article)
        newsList.adapter=adapter
        //newsList.layoutManager= LinearLayoutManager(this@MainActivity)

        var layoutManager=StackLayoutManager(StackLayoutManager.ScrollOrientation.BOTTOM_TO_TOP)
        layoutManager.setPagerMode(true)
        layoutManager.setPagerFlingVelocity(3000)
        layoutManager.setItemChangedListener(object:StackLayoutManager.ItemChangedListener{
            override fun onItemChanged(position: Int) {
                container.setBackgroundColor(Color.parseColor(ColorPicker.getColor()))
                Log.d(TAG,"First visible item - ${layoutManager.getFirstVisibleItemPosition()}")
                Log.d(TAG,"Total Count - ${layoutManager.itemCount}")
                if(totalRes> layoutManager.itemCount && layoutManager.getFirstVisibleItemPosition() >= layoutManager.itemCount-5)
                {
                    pageNum++
                    getNews()
                }
            }

        })
        newsList.layoutManager=layoutManager

        getNews()
    }
    private fun getNews(){
        val news=NewsService.newsInstance.getHeadlines("in",pageNum)
        news.enqueue(object: Callback<News>{
            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("Newsly","Failed")
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news=response.body()
                if(news!=null){
                    totalRes=news.totalResults
                    Log.d("Newsly",news.toString())
                    article.addAll(news.articles)
                    adapter.notifyDataSetChanged()

                }
            }
        })



    }
}
