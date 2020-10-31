package com.wl.pluto.component.main.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wl.pluto.component.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerview;

    private List<com.wl.pluto.component.main.ui.ModelMainItem> mData;

    private int mColumnCount = 3;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerview = findViewById(R.id.rv_recycler_view);

        initData();
        initView();
    }

    private void initData() {

        mData = new ArrayList<>();

       /* mData.add(new ModelMainItem("常用的Dialog展示", R.mipmap.circle_dialog, ActivityDialog.class));
        mData.add(new ModelMainItem("PopupView的使用", R.mipmap.circle_bullet, ActivityPopupView.class));
        mData.add(new ModelMainItem("加载的艺术", R.mipmap.circle_loading_icon, ActivityLoading.class));

        mData.add(new ModelMainItem("RxPhotoTool操作UZrop裁剪图片", R.mipmap.circle_elves_ball, ActivityRxPhoto.class));
        mData.add(new ModelMainItem("二维码与条形码的扫描与生成", R.mipmap.circle_dynamic_generation_code, ActivityCodeTool.class));*/
        //mData.add(new ModelMainItem("数据库", R.mipmap.circle_qr_code, DataBaseActivityTest.class));

       /*

        mData.add(new ModelMainItem("动态生成码", R.drawable.circle_qr_code, ActivityCreateQRCode.class));

        mData.add(new ModelMainItem("WebView的封装可播放视频", R.drawable.circle_webpage, ActivityWebView.class));
        mData.add(new ModelMainItem("图片的缩放艺术", R.drawable.circle_scale_icon, ActivityRxScaleImageView.class));

        mData.add(new ModelMainItem("RxDataTool操作Demo", R.drawable.circle_data, ActivityRxDataTool.class));
        mData.add(new ModelMainItem("设备信息", R.drawable.circle_device_info, ActivityDeviceInfo.class));
        mData.add(new ModelMainItem("RxTextTool操作Demo", R.drawable.circle_text, ActivityTextTool.class));

        mData.add(new ModelMainItem("进度条的艺术", R.drawable.circle_bar, ActivityProgressBar.class));

        mData.add(new ModelMainItem("点赞控件", R.drawable.circle_heart_circle, ActivityLike.class));

        mData.add(new ModelMainItem("旋转引擎View", R.drawable.circle_rotate, ActivityRxRotateBar.class));
        mData.add(new ModelMainItem("蛛网等级View", R.drawable.circle_cobweb, ActivityCobweb.class));
        mData.add(new ModelMainItem("添加购物车控件", R.drawable.circle_shop_cart, ActivityShoppingView.class));

        mData.add(new ModelMainItem("网速控件", R.drawable.circle_net_speed, ActivityNetSpeed.class));
        mData.add(new ModelMainItem("验证码", R.drawable.circle_captcha, ActivityRxCaptcha.class));
        mData.add(new ModelMainItem("横向滑动选择控件", R.drawable.circle_bookshelf, ActivityWheelHorizontal.class));

        mData.add(new ModelMainItem("横向左右自动滚动的ImageView", R.drawable.circle_two_way, ActivityAutoImageView.class));
        mData.add(new ModelMainItem("SlidingDrawerSingle使用", R.drawable.circle_up_down, ActivitySlidingDrawerSingle.class));
        mData.add(new ModelMainItem("RxSeekBar", R.drawable.circle_seek, ActivitySeekBar.class));

        mData.add(new ModelMainItem("登录界面", R.drawable.circle_clound, ActivityLoginAct.class));

        mData.add(new ModelMainItem("RxToast的使用", R.drawable.circle_toast, ActivityRxToast.class));

        mData.add(new ModelMainItem("RunTextView的使用", R.drawable.circle_wrap_text, ActivityRunTextView.class));
        mData.add(new ModelMainItem("选座控件", R.drawable.circle_seat, ActivitySeat.class));
        mData.add(new ModelMainItem("银行卡组堆叠控件", R.drawable.circle_credit_card, ActivityCardStack.class));

        mData.add(new ModelMainItem("联系人侧边快速导航", R.drawable.circle_phone, ActivityContact.class));
        mData.add(new ModelMainItem("GPS原生定位", R.drawable.circle_gps_icon, ActivityLocation.class));
        mData.add(new ModelMainItem("震动的艺术", R.drawable.circle_vibrate, ActivityVibrate.class));

        mData.add(new ModelMainItem("压缩与加密的艺术", R.drawable.circle_zip, ActivityZipEncrypt.class));
        mData.add(new ModelMainItem("图片添加经纬度信息", R.drawable.circle_picture_location, ActivityRxExifTool.class));
        mData.add(new ModelMainItem("RxWaveView", R.drawable.circle_wave, ActivityRxWaveView.class));
        mData.add(new ModelMainItem("app检测更新与安装", R.mipmap.ic_launcher, ActivitySplash.class));
        mData.add(new ModelMainItem("PULL解析XML", R.mipmap.ic_launcher, ActivityXmlParse.class));
        mData.add(new ModelMainItem("支付宝支付Demo", R.drawable.circle_alipay, ActivityAliPay.class));*/
    }

    private void initView() {
        if (mData.size() <= 1) {
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerview.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }

        //recyclerview.addItemDecoration(new RxRecyclerViewDividerTool(RxSizeTool.dp2px(5f)));
        // MainRecyclerViewAdapter recyclerViewMain = new MainRecyclerViewAdapter(mData);

        //recyclerview.setAdapter(recyclerViewMain);
    }
}
