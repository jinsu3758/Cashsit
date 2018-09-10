package com.example.jinsu.cash.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.speech.tts.TextToSpeech
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.jinsu.cash.R
import com.example.jinsu.cash.common.Constant
import com.example.jinsu.cash.dialog.PopupDialog
import com.example.jinsu.cash.util.BluetoothService
import com.example.jinsu.cash.util.CircleAnimation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.navi_header.view.*
import java.lang.ref.WeakReference
import java.util.*




class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var handler: Handler
    lateinit var dialog: PopupDialog
    lateinit var tts : TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onResume() {
        super.onResume()
        setAnimation()
        BluetoothService.get.setHandler(handler)
    }

    @SuppressLint("ResourceAsColor")
    fun setAnimation()
    {
        Constant.right_posture = Constant.Right_time  / 86400 * 360
        Constant.bad_posture = Constant.bad_time *  86400 * 360
        val animation = CircleAnimation(content_main.main_circle,Constant.right_posture, Constant.bad_posture)
        animation.duration = 3000
        content_main.main_circle.startAnimation(animation)
//        main_toolbar.navigationIcon = getDrawable(R.drawable.menu)

    }


    fun init()
    {
        main_toolbar.setNavigationIcon(getDrawable(R.drawable.menu))
        content_main.main_toolbar.title = getString(R.string.main)
        content_main.main_toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(content_main.main_toolbar)
        val toggle = ActionBarDrawerToggle(this,main_layout,content_main.main_toolbar,0,0)
        main_layout.addDrawerListener(toggle)
        toggle.syncState()
        main_navi.setNavigationItemSelectedListener(this)
        Glide.with(this).load(R.drawable.my).apply(RequestOptions().circleCrop())
                .into(main_navi.getHeaderView(0).navi_im_profile)
        dialog = PopupDialog(this@MainActivity," ")

        handler = MyHandler(this)
        tts = TextToSpeech(this, TextToSpeech.OnInitListener {
            tts.language = Locale.KOREAN
        })


        if(Constant.prefs.user_data != null)
        {
            val user = Constant.prefs.user_data
            Log.d("main_at",user!!.nickname)
            main_navi.getHeaderView(0).navi_txt_id.setText(user!!.id.toString())
            content_main.main_txt_point.setText(user!!.point.toString())

        }


    }

    class MyHandler : Handler
    {
        lateinit var weak : WeakReference<MainActivity>

        constructor(activity : MainActivity)
        {
            weak = WeakReference<MainActivity>(activity)
        }

        override fun handleMessage(msg: Message) {
            val activity : MainActivity = weak.get()!!
            activity.handlerMessage(msg)
        }
    }

    fun handlerMessage(msg : Message)
    {

        when(msg.what)
        {
        //바른자세
            1 ->
            {
                main_txt_point.text = Constant.money.toString();
                main_txt_total_hour.text = (Constant.total_time/3600).toString()
                main_txt_total_min.text = ((Constant.total_time/60)%60).toString()
                main_txt_good_hour.text = (Constant.Right_time/3600).toString()
                main_txt_good_min.text = ((Constant.Right_time/60)%60).toString()
                Log.d("MainActivity","바른자세")

            }
        //기댄 자세
            2 ->
            {
                main_txt_total_hour.text = (Constant.total_time/3600).toString()
                main_txt_total_min.text = ((Constant.total_time/60)%60).toString()
                main_txt_bad_hour.text = (Constant.bad_time/3600).toString()
                main_txt_bad_min.text = ((Constant.bad_time/60)%60).toString()
                tts.speak("기대지 마세요",TextToSpeech.QUEUE_FLUSH,null,null)
                //   val dialog = PopupDialog(this@MainActivity, "뒤로 기댄 자세입니다.")
                if(dialog.isShowing()) {
                    dialog.dismiss()

                }
                if (!(this as Activity).isFinishing) {
                    dialog = PopupDialog(this@MainActivity,"뒤로 기댄 자세입니다.")
                    dialog.show()
                    dialog.setClick {
                        dialog.dismiss();
                    }
                }
            }
        //숙인 자세
            3 ->
            {
                main_txt_total_hour.text = (Constant.total_time/3600).toString()
                main_txt_total_min.text = ((Constant.total_time/60)%60).toString()
                main_txt_bad_hour.text = (Constant.bad_time/3600).toString()
                main_txt_bad_min.text = ((Constant.bad_time/60)%60).toString()
                tts.speak("숙이지 마세요!",TextToSpeech.QUEUE_FLUSH,null,null)
                //val dialog = PopupDialog(this@MainActivity, "앞으로 숙인 자세입니다.")
                if(dialog.isShowing()) {
                    dialog.dismiss()
                }
                if (!(this as Activity).isFinishing) {
                    dialog = PopupDialog(this@MainActivity,"앞으로 숙인 자세입니다.")
                    dialog.show()
                    dialog.setClick {
                        dialog.dismiss();
                    }
                }

            }
        //다리 꼰 자세
            4 ->
            {
                main_txt_total_hour.text = (Constant.total_time/3600).toString()
                main_txt_total_min.text = ((Constant.total_time/60)%60).toString()
                main_txt_bad_hour.text = (Constant.bad_time/3600).toString()
                main_txt_bad_min.text = ((Constant.bad_time/60)%60).toString()
                tts.speak("다리 꼬지 마세요",TextToSpeech.QUEUE_FLUSH,null,null)
                //   val dialog = PopupDialog(this@MainActivity, "왼쪽 다리를 꼰 자세입니다.")
                if(dialog.isShowing()) {
                    dialog.dismiss()

                }
                if (!(this as Activity).isFinishing) {
                    dialog = PopupDialog(this@MainActivity,"왼쪽 다리를 꼰 상태입니다.")
                    dialog.show()
                    dialog.setClick {
                        dialog.dismiss();
                    }
                }
            }
        //다리 꼰 자세
            5 ->
            {
                main_txt_total_hour.text = (Constant.total_time/3600).toString()
                main_txt_total_min.text = ((Constant.total_time/60)%60).toString()
                main_txt_bad_hour.text = (Constant.bad_time/3600).toString()
                main_txt_bad_min.text = ((Constant.bad_time/60)%60).toString()
                tts.speak("다리 꼬지 마세요",TextToSpeech.QUEUE_FLUSH,null,null)
                //val dialog = PopupDialog(this@MainActivity, "오른쪽 다리를 꼰 자세입니다.")
                if(dialog.isShowing()) {
                    dialog.dismiss()

                }
                if (!(this as Activity).isFinishing) {
                    dialog = PopupDialog(this@MainActivity,"오른쪽 다리를 꼰 상태입니다.")
                    dialog.show()
                    dialog.setClick {
                        dialog.dismiss();
                    }
                }


            }
        }
    }
    override fun onBackPressed() {
        if (main_layout.isDrawerOpen(GravityCompat.START)) {
            main_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menu_nav_home ->
            {
            }
            R.id.menu_nav_mypage ->
            {
                startActivity(Intent(this,MyPageActivity::class.java))
            }
            R.id.menu_nav_chart ->
            {
                startActivity(Intent(this,ChartActivity::class.java))
            }
            R.id.menu_nav_shopping ->
            {
                startActivity(Intent(this,ShopActivity::class.java))
            }
            R.id.menu_nav_rank ->
            {
                startActivity(Intent(this,RankActivity::class.java))
            }
        }
        main_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        handler.removeMessages(0)
        tts.stop()
        tts.shutdown()
    }
}
