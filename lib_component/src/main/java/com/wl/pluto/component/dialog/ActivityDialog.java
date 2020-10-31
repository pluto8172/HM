package com.wl.pluto.component.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.wl.pluto.component.R;
import com.wl.pluto.component.view.RxTitle;
import com.wl.pluto.component.view.dialog.RxDialog;
import com.wl.pluto.component.view.dialog.RxDialogAcfunVideoLoading;
import com.wl.pluto.component.view.dialog.RxDialogEditSureCancel;
import com.wl.pluto.component.view.dialog.RxDialogLoading;
import com.wl.pluto.component.view.dialog.RxDialogScaleView;
import com.wl.pluto.component.view.dialog.RxDialogShapeLoading;
import com.wl.pluto.component.view.dialog.RxDialogSure;
import com.wl.pluto.component.view.dialog.RxDialogSureCancel;
import com.wl.pluto.component.view.dialog.RxDialogWheelYearMonthDay;

import rxtool.RxBarTool;


/**
 * @author vondear
 */
public class ActivityDialog extends AppCompatActivity {

    ////(R.id.button_tran)
    Button mButtonTran;


    ////(R.id.button_DialogSure)
    Button mButtonDialogSure;

    ////(R.id.button_DialogSureCancle)
    Button mButtonDialogSureCancle;

    ////(R.id.button_DialogEditTextSureCancle)
    Button mButtonDialogEditTextSureCancle;

    ////(R.id.button_DialogWheelYearMonthDay)
    Button mButtonDialogWheelYearMonthDay;

    ////(R.id.button_DialogShapeLoading)
    Button mButtonDialogShapeLoading;

    //BindView(R.id.button_DialogLoadingProgressAcfunVideo)
    Button mButtonDialogLoadingProgressAcfunVideo;

    ////(R.id.activity_dialog)
    LinearLayout mActivityDialog;

    ////(R.id.rx_title)
    RxTitle mRxTitle;

    ////(R.id.button_DialogLoadingspinkit)
    Button mButtonDialogLoadingspinkit;

    ////(R.id.button_DialogScaleView)
    Button mButtonDialogScaleView;

    private RxDialogWheelYearMonthDay mRxDialogWheelYearMonthDay;

    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        RxBarTool.noTitle(this);
        setContentView(R.layout.activity_dialog);
        RxBarTool.setTransparentStatusBar(this);
        initView();
    }

    private void initView() {

        mButtonTran = findViewById(R.id.button_tran);
        mButtonTran.setOnClickListener(v -> {
            RxDialog rxDialog = new RxDialog(mContext, R.style.tran_dialog);
            View view1 = LayoutInflater.from(mContext).inflate(R.layout.image, null);
            ImageView pageItem = view1.findViewById(R.id.page_item);
            pageItem.setImageResource(R.mipmap.coin);
            rxDialog.setContentView(view1);
            rxDialog.show();
        });


        mButtonDialogSure = findViewById(R.id.button_DialogSure);
        mButtonDialogSure.setOnClickListener(v -> {
            final RxDialogSure rxDialogSure = new RxDialogSure(mContext);
            rxDialogSure.getLogoView().setImageResource(R.mipmap.logo);
            rxDialogSure.getSureView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxDialogSure.cancel();
                }
            });
            rxDialogSure.show();
        });


        mButtonDialogSureCancle = findViewById(R.id.button_DialogSureCancle);
        mButtonDialogSureCancle.setOnClickListener(v -> {
            final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(mContext);
            rxDialogSureCancel.getTitleView().setBackgroundResource(R.mipmap.logo);
            rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxDialogSureCancel.cancel();
                }
            });
            rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxDialogSureCancel.cancel();
                }
            });
            rxDialogSureCancel.show();
        });


        mButtonDialogEditTextSureCancle = findViewById(R.id.button_DialogEditTextSureCancle);
        mButtonDialogEditTextSureCancle.setOnClickListener(v -> {
            final RxDialogEditSureCancel rxDialogEditSureCancel = new RxDialogEditSureCancel(mContext);
            rxDialogEditSureCancel.getTitleView().setBackgroundResource(R.mipmap.logo);
            rxDialogEditSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxDialogEditSureCancel.cancel();
                }
            });
            rxDialogEditSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxDialogEditSureCancel.cancel();
                }
            });
            rxDialogEditSureCancel.show();
        });

        mButtonDialogWheelYearMonthDay = findViewById(R.id.button_DialogWheelYearMonthDay);
        mButtonDialogWheelYearMonthDay.setOnClickListener(v -> {
            if (mRxDialogWheelYearMonthDay == null) {
                initWheelYearMonthDayDialog();
            }
            mRxDialogWheelYearMonthDay.show();
        });

        mButtonDialogShapeLoading = findViewById(R.id.button_DialogShapeLoading);
        mButtonDialogShapeLoading.setOnClickListener(v -> {
            RxDialogShapeLoading rxDialogShapeLoading = new RxDialogShapeLoading(ActivityDialog.this);
            rxDialogShapeLoading.show();
        });

        mButtonDialogLoadingProgressAcfunVideo = findViewById(R.id.button_DialogLoadingProgressAcfunVideo);
        mButtonDialogLoadingProgressAcfunVideo.setOnClickListener(v -> {
            new RxDialogAcfunVideoLoading(ActivityDialog.this).show();
        });


        mButtonDialogLoadingspinkit = findViewById(R.id.button_DialogLoadingspinkit);
        mButtonDialogLoadingspinkit.setOnClickListener(v -> {
            RxDialogLoading rxDialogLoading = new RxDialogLoading(mContext);
            rxDialogLoading.show();
        });

        mButtonDialogScaleView = findViewById(R.id.button_DialogScaleView);
        mButtonDialogScaleView.setOnClickListener(v -> {
            RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(mContext);
            rxDialogScaleView.setImage("squirrel.jpg", true);
            rxDialogScaleView.show();
        });

        mRxTitle = findViewById(R.id.rx_title);

        mRxTitle.setLeftFinish(this);
    }

    private void initWheelYearMonthDayDialog() {
        // ------------------------------------------------------------------选择日期开始
        mRxDialogWheelYearMonthDay = new RxDialogWheelYearMonthDay(this, 1994, 2018);
        mRxDialogWheelYearMonthDay.getSureView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mRxDialogWheelYearMonthDay.getCheckBoxDay().isChecked()) {
                    mButtonDialogWheelYearMonthDay.setText(mRxDialogWheelYearMonthDay.getSelectorYear() + "年" + mRxDialogWheelYearMonthDay.getSelectorMonth() + "月" + mRxDialogWheelYearMonthDay.getSelectorDay() + "日");
                } else {
                    mButtonDialogWheelYearMonthDay.setText(mRxDialogWheelYearMonthDay.getSelectorYear() + "年" + mRxDialogWheelYearMonthDay.getSelectorMonth() + "月");
                }
                mRxDialogWheelYearMonthDay.cancel();
            }
        });
        mRxDialogWheelYearMonthDay.getCancleView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mRxDialogWheelYearMonthDay.cancel();
            }
        });

    }

}
