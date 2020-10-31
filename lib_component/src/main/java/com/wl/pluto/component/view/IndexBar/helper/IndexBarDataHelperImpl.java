package com.wl.pluto.component.view.IndexBar.helper;


import com.github.promeg.pinyinhelper.Pinyin;
import com.wl.pluto.component.view.IndexBar.bean.BaseIndexPinyinBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;


/**
 * 介绍：IndexBar 的 数据相关帮助类 实现
 * * 1 将汉语转成拼音(利用tinyPinyin)
 * 2 填充indexTag (取拼音首字母)
 * 3 排序源数据源
 * 4 根据排序后的源数据源->indexBar的数据源
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * 主页：http://blog.csdn.net/zxt0601
 * 时间： 2016/11/28.
 */

public class IndexBarDataHelperImpl implements IIndexBarDataHelper {
    /**
     * 如果需要，
     * 字符->拼音，
     *
     * @param datas
     */
    @Override
    public IIndexBarDataHelper convert(List<? extends BaseIndexPinyinBean> datas) {
        if (null == datas || datas.isEmpty()) {
            return this;
        }
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            BaseIndexPinyinBean indexPinyinBean = datas.get(i);
            StringBuilder pySb = new StringBuilder();
            //add by zhangxutong 2016 11 10 如果不是top 才转拼音，否则不用转了
            if (indexPinyinBean.isNeedToPinyin()) {
                String target = indexPinyinBean.getTargetA();//取出需要被拼音化的字段
                //遍历target的每个char得到它的全拼音
                for (int i1 = 0; i1 < target.length(); i1++) {
                    //利用TinyPinyin将char转成拼音
                    //查看源码，方法内 如果char为汉字，则返回大写拼音
                    //如果c不是汉字，则返回String.valueOf(c)
                    //如果是数字不作处理
                    if (target.equals("近三天") || target.equals("三天前")) {
                        pySb.append(target.charAt(i1));
                    } else if (isInteger(target)) {
                        pySb.append(target.charAt(i1));
                    } else if (String.valueOf(target.charAt(i1)).equals("重")) {
                        pySb.append("C");
                    } else if (String.valueOf(target.charAt(i1)).equals("长")) {
                        pySb.append("C");
                    } else {
                        pySb.append(Pinyin.toPinyin(target.charAt(i1)).toUpperCase());
                    }
                }
                indexPinyinBean.setBaseIndexPinyin(pySb.toString());//设置城市名全拼音
            } else {
                //pySb.append(indexPinyinBean.getBaseIndexPinyin());
            }
        }
        return this;
    }

    /**
     * 判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 如果需要取出，则
     * 取出首字母->tag,或者特殊字母 "#".
     * 否则，用户已经实现设置好
     *
     * @param datas
     */
    public IIndexBarDataHelper fillIndexTag(List<? extends BaseIndexPinyinBean> datas) {
        if (null == datas || datas.isEmpty()) {
            return this;
        }
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            BaseIndexPinyinBean indexPinyinBean = datas.get(i);
            if (indexPinyinBean.isNeedToPinyin()) {
                //以下代码设置城市拼音首字母
                if (indexPinyinBean.getBaseIndexPinyin().trim().length() == 0) {
                    indexPinyinBean.setBaseIndexTag("#");
                } else {
                    String tagString = indexPinyinBean.getBaseIndexPinyin().trim().substring(0, 1);
                    if (indexPinyinBean.getBaseIndexPinyin().equals("近三天") || indexPinyinBean.getBaseIndexPinyin().equals("三天前")) {// 做特殊处理
                        indexPinyinBean.setBaseIndexTag(indexPinyinBean.getBaseIndexPinyin());
                    } else if (isInteger(indexPinyinBean.getBaseIndexPinyin())) {//如果是数字

                        if (indexPinyinBean.getBaseIndexPinyin().startsWith("20") && indexPinyinBean.getBaseIndexPinyin().length() == 4) {
                            indexPinyinBean.setBaseIndexTag(indexPinyinBean.getBaseIndexPinyin() + "年");
                        } else {
                            indexPinyinBean.setBaseIndexTag(indexPinyinBean.getBaseIndexPinyin().substring(0, 1));
                        }

                    } else if (tagString.matches("[A-Z]")) {//如果是A-Z字母开头
                        indexPinyinBean.setBaseIndexTag(tagString);
                    } else {//特殊字母这里统一用#处理
                        indexPinyinBean.setBaseIndexTag("#");
                    }
                }
            }
        }
        return this;
    }

    private void convertInteger2Char(BaseIndexPinyinBean indexPinyinBean, String str) {

        switch (str) {
            case "0":
            case "6":
                indexPinyinBean.setBaseIndexTag("L");
                break;
            case "1":
                indexPinyinBean.setBaseIndexTag("Y");
                break;
            case "2":
                indexPinyinBean.setBaseIndexTag("E");
                break;
            case "3":
            case "4":
                indexPinyinBean.setBaseIndexTag("S");
                break;
            case "5":
                indexPinyinBean.setBaseIndexTag("W");
                break;
            case "7":
                indexPinyinBean.setBaseIndexTag("Q");
                break;
            case "8":
                indexPinyinBean.setBaseIndexTag("B");
                break;
            case "9":
                indexPinyinBean.setBaseIndexTag("J");
                break;
        }

    }

    @Override
    public IIndexBarDataHelper sortSourceDatas(List<? extends BaseIndexPinyinBean> datas) {
        if (null == datas || datas.isEmpty()) {
            return this;
        }
        convert(datas);
        fillIndexTag(datas);
        //对数据源进行排序
        Collections.sort(datas, new Comparator<BaseIndexPinyinBean>() {
            @Override
            public int compare(BaseIndexPinyinBean lhs, BaseIndexPinyinBean rhs) {
                //近三天在前三天之前
                if (lhs.getBaseIndexPinyin().equals("近三天") && rhs.getBaseIndexPinyin().equals("三天前")) {// 做特殊处理
                    return -1;
                } else if (lhs.getBaseIndexPinyin().equals("三天前") || rhs.getBaseIndexPinyin().equals("近三天")) {
                    return 1;
                } else if (isInteger(lhs.getBaseIndexPinyin()) && isInteger(rhs.getBaseIndexPinyin())) {//如果比对的两个参数都是数字
                    int bef = Integer.parseInt(lhs.getBaseIndexPinyin());
                    int aft = Integer.parseInt(rhs.getBaseIndexPinyin());
                    return bef < aft ? 1 : -1;
                } else if (!lhs.isNeedToPinyin()) {
                    return 0;
                } else if (!rhs.isNeedToPinyin()) {
                    return 0;
                } else if (lhs.getBaseIndexTag().equals("#")) {
                    return 1;
                } else if (rhs.getBaseIndexTag().equals("#")) {
                    return -1;
                } else {
                    return lhs.getBaseIndexPinyin().compareTo(rhs.getBaseIndexPinyin());
                }
            }
        });
        return this;
    }

    @Override
    public IIndexBarDataHelper getSortedIndexDatas(List<? extends BaseIndexPinyinBean> sourceDatas, List<String> indexDatas) {
        if (null == sourceDatas || sourceDatas.isEmpty()) {
            return this;
        }
        //按数据源来 此时sourceDatas 已经有序
        int size = sourceDatas.size();
        String baseIndexTag;
        for (int i = 0; i < size; i++) {
            baseIndexTag = sourceDatas.get(i).getBaseIndexTag();
            if (!indexDatas.contains(baseIndexTag)) {//则判断是否已经将这个索引添加进去，若没有则添加
                indexDatas.add(baseIndexTag);
            }
        }
        return this;
    }
}
