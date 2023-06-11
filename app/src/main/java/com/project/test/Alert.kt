package com.project.test

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.TypedValue
import android.view.View
import android.view.View.OnClickListener
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class Alert(private val context: Activity, private val text: String?, private val text2:SpannableString?, private val text4: SpannableStringBuilder?, private val text1:String,private val text7:String?,private val text6:String) {
    private lateinit var dialog: Dialog;
    private var clk: OnClickListener? = null
    private var clk1: OnClickListener? = null
    fun alert() {
        dialog = Dialog(context, R.style.my_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_dialog_customize);
        dialog.setCancelable(false);
        dialog.show();

        val textView = dialog.findViewById<TextView>(R.id.text_alert)
        when{
            text!=null->{
                textView.text = text.toString()
            }
            text2!=null->{
                textView.text = text2
            }
            text4!=null->{
                textView.text = text4
            }
        }
        val textView1 = dialog.findViewById<TextView>(R.id.title_alert)
        val textBtn = dialog.findViewById<Button>(R.id.alert_dialog)
        val textBtn1 = dialog.findViewById<Button>(R.id.alert_dialog_1)
        val textBtn2 = dialog.findViewById<Button>(R.id.alert_dialog_2)

        textView1.text = text6
        if(text6=="هشدار"){
            dialog.findViewById<ImageView>(R.id.icon_alert_1).visibility= View.GONE
            dialog.findViewById<ImageView>(R.id.icon_alert_2).visibility= View.VISIBLE
            textBtn.visibility= View.GONE
            textBtn1.visibility= View.VISIBLE
            textBtn2.visibility= View.VISIBLE
            textBtn1.text = text1
            textBtn2.text = text7

            textBtn1.setOnClickListener {
                clk?.onClick(it)
                dialog.dismiss()
            }
            textBtn2.setOnClickListener {
                clk1?.onClick(it)
                dialog.dismiss()
            }
        }
        if(text6=="خطا"){
            dialog.findViewById<ImageView>(R.id.icon_alert_1).visibility= View.VISIBLE
            dialog.findViewById<ImageView>(R.id.icon_alert_2).visibility= View.GONE
            textBtn.visibility= View.VISIBLE
            textBtn1.visibility= View.GONE
            textBtn2.visibility= View.GONE
            textBtn.text = text1

            textBtn.setOnClickListener {
                clk?.onClick(it)
                dialog.dismiss()
            }
        }
        val fontSizeTitle = Size(context).fontSize(0.044f)
        val fontSizeContent = Size(context).fontSize(0.03f)
        val fontSizeBtn = Size(context).fontSize(0.027f)

        textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
        textBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeBtn)
        textBtn1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeBtn)
        textBtn2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeBtn)



    }
    fun setOnClick(clk: OnClickListener) {
        this.clk = clk;
    }
    fun setOnClick1(clk1: OnClickListener) {
        this.clk1 = clk1;
    }
}