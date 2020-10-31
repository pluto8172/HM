package com.arch.demo.common.arouter.book;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface IBookService extends IProvider {
    String BOOK_SERVICE =  "/book/book_router";
    Fragment getBookFragment();
}
