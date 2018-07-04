package com.example.adrianweniger.sleepinspector.CustomViews

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.adrianweniger.sleepinspector.R
import kotlinx.android.synthetic.main.view_times_item_input.view.*


/**
 * Custom view used to handle input data in the form of "<count>x <item_type>"
 * outputs a list of all items and their count
 */

class TimesInputView: LinearLayout{
    //initialize data
    private var lineNumber = -1
    var itemList = mutableListOf<String>()

    // listener interface
    var onChangeListener = {itemArray :MutableList<String> -> Unit}


    constructor(context: Context, attrs: AttributeSet):super(context, attrs){
        init(context, attrs)
    }

    //initialuze the view
    private fun init(context: Context, attrs: AttributeSet){
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_times_item_input, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.view_times_item_input)
        val hint = typedArray.getString(R.styleable.view_times_item_input_input_hint)

        addLine(context, hint)

        add_button.setOnClickListener({
            addLine(context, hint)
        })
    }

    // add a line to input a new item
    private fun addLine(context: Context, hint:String){
        lineNumber += 1

        var linearLayout = LinearLayout(context)
        linearLayout.id = lineNumber
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        var timesInput = createTimeInput(context, lineNumber)
        var timesSign = createTimesSign(context)
        var typeInput = createTypeInput(context, lineNumber, hint)

        linearLayout.addView(timesInput)
        linearLayout.addView(timesSign)
        linearLayout.addView(typeInput)

        times_type_holder.addView(linearLayout, times_type_holder.childCount - 1)
    }

    // create input field for the <item_type>
    private fun createTypeInput(context: Context, line:Int, hint:String): EditText {
        var typeInput = EditText(context)
        typeInput.tag = "type_input_$line"
        typeInput.inputType = InputType.TYPE_CLASS_TEXT
        typeInput.textAlignment = View.TEXT_ALIGNMENT_CENTER
        typeInput.textSize = 30f
        typeInput.hint = hint
        typeInput.layoutParams = ViewGroup.LayoutParams(dpToPx(150), dpToPx(55))

        typeInput.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(type: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateItem(line, type.toString(), null)
            }

        })
        return typeInput
    }

    //create the "x" symbol
    private fun createTimesSign(context: Context): TextView {
        var timesSign = TextView(context)
        timesSign.text = "x"
        timesSign.textAlignment = View.TEXT_ALIGNMENT_CENTER
        timesSign.textSize = 30f
        timesSign.layoutParams = ViewGroup.LayoutParams(dpToPx(15), dpToPx(55))
        return timesSign
    }

    // create the <count> input field
    private fun createTimeInput(context: Context, line:Int): EditText{
        var timesInput = EditText(context)
        timesInput.tag = "times_input_$line"
        timesInput.hint = "0"
        timesInput.inputType = InputType.TYPE_CLASS_NUMBER
        timesInput.textAlignment = View.TEXT_ALIGNMENT_CENTER
        timesInput.textSize = 30f
        timesInput.layoutParams = ViewGroup.LayoutParams(dpToPx(50), dpToPx(55))
        timesInput.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(times: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateItem(line, null, times.toString())
            }

        })
        return timesInput
    }

    //Helper function | tranforms px value to androids fluid/reactive dp value
    private fun dpToPx(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return Math.round(dp.toFloat() * density)
    }

    // update the item list on input
    // calls onChangeListener implemented in class using this view
    private fun updateItem(line:Int, type: String?, times:String?){
        val timesInput = findViewWithTag("times_input_$line") as EditText
        val typeInput = findViewWithTag("type_input_$line") as EditText

        if(times != null){
            val timesTypeString = times + "x " + typeInput.text.toString()
            if(itemList.size <= line ){ itemList.add(line, timesTypeString) } else itemList[line] = timesTypeString


        } else if (type != null){
            val timesTypeString = timesInput.text.toString() + "x" + type
            if(itemList.size <= line ){ itemList.add(line, timesTypeString) } else itemList[line] = timesTypeString
        }

        // listener function implementet in parent class
        // listener function gets updatet item list as parameter and returns nothing
        // see lambda in initialization block at the top of this class
        onChangeListener(itemList)
    }
}