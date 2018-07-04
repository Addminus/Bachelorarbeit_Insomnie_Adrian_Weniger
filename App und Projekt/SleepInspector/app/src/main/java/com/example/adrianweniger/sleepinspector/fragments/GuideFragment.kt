package com.example.adrianweniger.sleepinspector.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.adrianweniger.sleepinspector.ArticleLoader
import com.example.adrianweniger.sleepinspector.DataClasses.Article
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.activities.ArticleActivity
import com.example.adrianweniger.sleepinspector.activities.MainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_guide.view.*
import kotlinx.android.synthetic.main.guide_list_row.view.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Fragment showing Guide
 * populates the list with articles
 * opens articleActivity to show article
 */
class GuideFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //load view
        var inflated = inflater!!.inflate(R.layout.fragment_guide, null)

        inflated.list.adapter = ListAdapter(context, ArticleLoader.articleList!!)

        return inflated
    }

    // list adapter to populate List
    private class ListAdapter(context: Context, articleList: ArrayList<Article>): BaseAdapter(){

        private var mContext: Context = context
        private var articleList: ArrayList<Article> = articleList

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
            var inflated = LayoutInflater.from(mContext).inflate(R.layout.guide_list_row, viewGroup, false)

            //ignore article with id == 0 as it is the legal/privacy disclaimer
            if(articleList[position].id != 0){
                inflated.title.text = articleList[position].headline
                inflated.description.text = articleList[position].description
                inflated.row.setOnClickListener{
                    val intent = Intent(mContext, ArticleActivity::class.java)
                    intent.putExtra("id", articleList[position].id)
                    mContext.startActivity(intent)
                }
            }

            return inflated
        }

        override fun getItem(position: Int): Any {
            return ""
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return articleList.size-1
        }


    }

}