package com.example.adrianweniger.sleepinspector.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.adrianweniger.sleepinspector.ArticleLoader
import com.example.adrianweniger.sleepinspector.R
import kotlinx.android.synthetic.main.activity_article.*
import android.widget.LinearLayout

/**
 *used to display articles from the guide and the legal/privacy disclaimer
 * gets an id via intent
*/

class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val inflater = LayoutInflater.from(this)
        val inflatedActionBar = inflater.inflate(R.layout.action_bar, null)
        //Center the textview in the ActionBar
        val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER)
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setCustomView(inflatedActionBar, params)


        // load article associated with the id given by the intent.
        var id = intent.extras.get("id")
        var article = ArticleLoader.articleList!!.find { it.id == id.toString().toInt() }!!
        headline.text = article.headline
        article_text.text = article.text
        link.text = "Quelle: ${article.link}"

        //if id == 0 hide source and also text as article with id == 0 ist the legal/privacy disclaimer
        if(id == 0){
            link.visibility = View.GONE
            also_holder.visibility = View.GONE
        }

        //set link to the source
        link.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
            startActivity(browserIntent)
        }

        //set links to related articles
        if(article.also.size == 0){
            also_holder.removeAllViews()
        } else {
            for (alsoId in article.also){
                var textView = TextView(this)
                textView.text = ArticleLoader.articleList!!.find { it.id == alsoId }!!.headline
                textView.setOnClickListener{
                    val intent = Intent(this, ArticleActivity::class.java)
                    intent.putExtra("id", alsoId)
                    this.startActivity(intent)
                }
                val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.marginStart = 50
                textView.layoutParams = params
                also_holder.addView(textView)
            }
        }



    }


}
