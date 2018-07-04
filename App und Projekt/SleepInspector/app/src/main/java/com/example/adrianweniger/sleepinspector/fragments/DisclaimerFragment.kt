package com.example.adrianweniger.sleepinspector.fragments

import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.example.adrianweniger.sleepinspector.R
import com.example.adrianweniger.sleepinspector.activities.ArticleActivity
import com.example.adrianweniger.sleepinspector.activities.PersonalInfoEditActivity
import kotlinx.android.synthetic.main.fragment_disclaimer.*
import kotlinx.android.synthetic.main.fragment_disclaimer.view.*

/**
 * Fragment showing disclaimer on first start
 * user has to accept privacy disclaimer to continue
 */
class DisclaimerFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // load view
        var inflated = inflater!!.inflate(R.layout.fragment_disclaimer, null)

        //get colors
        val activeColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, R.color.colorPrimaryDark)    } else {
            resources.getColor(R.color.colorPrimaryDark)
        }
        val inactiveColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, R.color.colorPrimary)    } else {
            resources.getColor(R.color.colorPrimary)
        }

        //set up "los gehts" button
        inflated.lets_go_btn.paintFlags = inflated.lets_go_btn.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        inflated.lets_go_btn.setTextColor(inactiveColor)

        // listeners || show "los gehts" button on privacy accept
        inflated.opt_in.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                inflated.lets_go_btn.setTextColor(activeColor)
                inflated.lets_go_btn.setOnClickListener{
                    val intent = Intent(context, PersonalInfoEditActivity::class.java)
                    startActivity(intent)
                }
            } else {
                inflated.lets_go_btn.setTextColor(inactiveColor)
                inflated.lets_go_btn.setOnClickListener(null)
            }
        }

        inflated.privacy_disclaimer.setOnClickListener {
            val intent = Intent(context, ArticleActivity::class.java)
            intent.putExtra("id", 0)
            context.startActivity(intent)
        }
        return inflated
    }
}