package com.arch.demo.common.widgets;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.widget.AppCompatTextView;

import com.arch.demo.common.R;


/**
 * 倒计时帮助类
 * 作者：Rance on 2016/12/1 16:56
 * 邮箱：rance935@163.com
 */
public class TimeTaskHelper {
    private final Context context;
    private Timer timer;
    private int Time;
    private int TIME;
    private AppCompatTextView textView;

    public TimeTaskHelper(Context context,AppCompatTextView textView, int Time) {
        this.textView = textView;
        this.Time = Time;
        this.context = context;
        TIME = Time;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (Time <= 0) {
                textView.setTextColor(context.getResources().getColor(R.color.text_time_blue));
                textView.setEnabled(true);
                textView.setText("重新获取");
                Time = TIME;
                timer.cancel();
            } else {
                if (textView != null) {
                    textView.setText(Time + "s");
                }
            }

        }
    };

    /**
     * 开启计时
     */
    public void Waiting() {
        textView.setTextColor(context.getResources().getColor(R.color.text_time_blue));
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Time--;
                handler.sendEmptyMessage(0);
            }
        }, 1, 1000);
    }

    /**
     * 销毁
     */
    public void cancel() {
        try {
            timer.cancel();
        } catch (Exception e) {

        }
    }
}
