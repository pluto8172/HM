package com.arch.demo.common.views.course;

import android.content.Context;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.arch.demo.common.R;
import com.arch.demo.common.databinding.CourseListItemViewBinding;
import com.wl.pluto.arch.mvvm2.customview.BaseCustomView;

/**
 * Created by plout on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class CourseListItemView extends BaseCustomView<CourseListItemViewBinding, CourseListItemViewData> {

    public CourseListItemView(Context context) {
        super(context);
    }

    @Override
    public int setViewLayoutId() {
        return R.layout.course_list_item_view;
    }

    @Override
    public void setDataToView(CourseListItemViewData data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    public void onRootClick(View view) {

        // 2. 跳转并携带参数
        ARouter.getInstance().build("/course/studyclasslistactivity").withString("intent_extra_title_text", getViewModel().title).withString("intent_extra_level_id", getViewModel().id).navigation();

    }
}
