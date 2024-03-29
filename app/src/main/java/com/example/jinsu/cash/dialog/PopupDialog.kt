package com.example.jinsu.cash.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import com.example.jinsu.cash.R
import kotlinx.android.synthetic.main.dialog_pop_up.*



class PopupDialog(context: Context?, text: String) : Dialog(context) {

    private var msg = text

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }


    private fun init()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_pop_up)

        dialog_pop_txt_pos.text = msg;
    }
    fun setText(etext: String){
        Log.d("팝업",etext)
        dialog_pop_txt_pos.text = etext
    }
    fun setClick(callback : () -> Unit)
    {
        dialog_pop_btn_rec.setOnClickListener()
        {
            callback.invoke()
        }

    }
}