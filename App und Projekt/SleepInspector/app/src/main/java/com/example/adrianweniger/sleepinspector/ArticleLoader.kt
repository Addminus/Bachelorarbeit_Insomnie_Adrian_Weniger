package com.example.adrianweniger.sleepinspector

import android.content.Context
import com.example.adrianweniger.sleepinspector.DataClasses.Article
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Handles loading of articles from json file in assets folder
 * Holds all articles
 * triggered in MainActivity
 */
object ArticleLoader {


        var articleList:ArrayList<Article>? = null


    fun loadArticles(context: Context) {
        val jsonString = context.assets.open("article.json").bufferedReader().use {
            it.readText()
        }

        val  gson = Gson()

        articleList = gson.fromJson(jsonString, object : TypeToken<ArrayList<Article>>() {}.type)
    }


}