package com.wl.pluto.arch.base.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wl.pluto.arch.R;
import com.wl.pluto.arch.base.bean.BaseTabBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 带有tab的fragment
 *
 * @param <T>
 * @param <F>
 */
public abstract class BaseTabActivity<T extends BaseTabBean, F extends Fragment> extends BaseActivity {


    protected List<T> mTabBeanList;

    protected LinearLayout mContentLayout;
    private AppCompatImageView mBackImage;
    protected AppCompatTextView mTitle;
    protected AppCompatImageView mRightText;
    protected TabLayout mTabLayout;
    private ViewPager mViewPager;

    private BaseViewPagerAdapter mPagerAdapter;

    /**
     * 是否需要异步初始化tab，如果需要，那就需要自己手动调用initLayout
     *
     * @return
     */
    protected boolean isSyncInitTabBean() {
        return false;
    }


    //需要子类实现的Tab数据
    public abstract List<T> initTabList();

    //需要子类实现的列表fragment
    abstract F getFragment(T tab);


    public class BaseViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public BaseViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>();
        }


        public void addPage(Fragment fragment) {
            mFragments.add(fragment);
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabBeanList.get(position).tabName;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activityx_tab_layout;
    }

    /**
     * 初始化参数
     */
    public void initParams() {

    }

    @Override
    public void initView() {
        mContentLayout = findViewById(R.id.ll_content_layout);
        mBackImage = findViewById(R.id.action_back);
        mBackImage.setOnClickListener(v -> finish());

        mTitle = findViewById(R.id.tv_top_bar_title);

        mRightText = findViewById(R.id.tv_top_bar_right);

        mTabLayout = findViewById(R.id.base_tab_layout);

        mViewPager = findViewById(R.id.base_vp_view_pager);
        initParams();
        if (!isSyncInitTabBean()) {
            initLayout();
        }
    }


    private void setTabLayoutStyle() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.tab_custom_layout);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(BaseTabActivity.this, R.style.TabLayoutStyleLarge);

                View indicator = tab.getCustomView().findViewById(R.id.tab_item_indicator);
                if (indicator != null) {
                    indicator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.tab_custom_layout);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(BaseTabActivity.this, R.style.TabLayoutStyleNormal);

                View indicator = tab.getCustomView().findViewById(R.id.tab_item_indicator);
                if (indicator != null) {
                    indicator.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void initLayout() {
        setTabLayoutStyle();
        //获取Tab数据
        mTabBeanList = initTabList();
        mPagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager());

        if (mTabBeanList != null) {
            //循环初始化tab和fragment
            for (BaseTabBean tabBean : mTabBeanList) {
                mPagerAdapter.addPage(getFragment((T) tabBean));
            }
        }
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
