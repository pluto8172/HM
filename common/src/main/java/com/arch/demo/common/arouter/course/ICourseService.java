package com.arch.demo.common.arouter.course;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface ICourseService extends IProvider {
    String COURSE_SERVICE =  "/course/course_router";
    Fragment getCourseFragment();
}
