package com.plout.hm.splash.ui;import android.content.Context;import android.os.Handler;import android.os.Message;import android.util.AttributeSet;import androidx.annotation.NonNull;import androidx.appcompat.widget.AppCompatTextView;import java.lang.ref.WeakReference;public class CountDownView extends AppCompatTextView {    private static final int MSG_COUNT_DOWN = 1000;    private CountDownHandler mHandler;    private int count = 5;    private OnCompleteListener completeListener;    public CountDownView(Context context) {        this(context, null);    }    public CountDownView(Context context, AttributeSet attrs) {        this(context, attrs, 0);    }    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);        init();    }    public void setCompleteListener(OnCompleteListener completeListener) {        this.completeListener = completeListener;    }    public interface OnCompleteListener {        void onComplete();    }    private static class CountDownHandler extends Handler {        private WeakReference<CountDownView> weakReference;        CountDownHandler(CountDownView view) {            weakReference = new WeakReference<>(view);        }        @Override        public void handleMessage(@NonNull Message msg) {            super.handleMessage(msg);            CountDownView downView = weakReference.get();            switch (msg.what) {                case MSG_COUNT_DOWN:                    if (downView != null) {                        downView.updateUI();                    }                    break;                case 0:                    break;            }        }    }    private void init() {        mHandler = new CountDownHandler(this);    }    private void updateUI() {        if (count > 0) {           // setText(String.format(getContext().getResources().getString(R.string.text_count_down), count--));            mHandler.sendEmptyMessageDelayed(MSG_COUNT_DOWN, 1000);        } else {            if (completeListener != null) {                completeListener.onComplete();            }            mHandler.removeMessages(MSG_COUNT_DOWN);        }    }    public void start(int duration){        count = duration;        start();    }    private void start() {        if(mHandler !=null){            mHandler.sendEmptyMessage(MSG_COUNT_DOWN);        }    }    public void cancel() {        if (mHandler != null) {            mHandler.removeMessages(MSG_COUNT_DOWN);            mHandler = null;        }    }}