package com.wl.pluto.component.loading;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wl.pluto.component.R;
import com.wl.pluto.component.view.RxTitle;

/**
 * @author vondear
 */
public class ActivityLoading extends AppCompatActivity {


    RxTitle mRxTitle;
    TabLayout mTabs;
    ViewPager mViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mRxTitle = findViewById(R.id.rx_title);
        mRxTitle.setLeftFinish(this);

        mTabs = findViewById(R.id.tabs);
        mViewpager = findViewById(R.id.viewpager);
        mViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            String[] titles = new String[]{"加载的方式", "加载的例子"};

            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return FragmentLoadingWay.newInstance();
                } else {
                    return FragmentLoadingDemo.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        mTabs.setupWithViewPager(mViewpager);
    }
}
