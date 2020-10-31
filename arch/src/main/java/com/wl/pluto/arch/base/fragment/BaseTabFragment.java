package com.wl.pluto.arch.base.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
public abstract class BaseTabFragment<T extends BaseTabBean, F extends Fragment> extends BaseFragment {


    protected TabLayout mTabLayout = null;
    protected ViewPager mViewPage = null;
    protected List<T> mTabBeanList = null;
    protected BaseViewPagerAdapter mPagerAdapter = null;

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
        return R.layout.fragment_common_tab_layout;
    }

    @Override
    public void initView() {


        mTabLayout = findView(R.id.arch_home_tab_layout);

        mViewPage = findView(R.id.arch_vp_view_pager);

        initLayout();
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
                textView.setTextAppearance(requireContext(), R.style.TabLayoutStyleLarge);

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
                textView.setTextAppearance(requireContext(), R.style.TabLayoutStyleNormal);

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
        mPagerAdapter = new BaseViewPagerAdapter(getChildFragmentManager());

        if (mTabBeanList != null) {
            //循环初始化tab和fragment
            for (BaseTabBean tabBean : mTabBeanList) {
                mPagerAdapter.addPage(getFragment((T) tabBean));
            }
        }
        mViewPage.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPage);
    }

}
