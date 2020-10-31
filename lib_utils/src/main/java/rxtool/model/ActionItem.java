package rxtool.model;

/**
 * @author Vondear
 * 功能描述：弹窗内部子类项（绘制标题和图标）
 */
public class ActionItem {
    /**
     * 定义文本对象
     */
    public CharSequence mTitle;
    public int mResourcesId;
    public int mTextColor;
    public int mLevelId;
    public int mCourseId;

   /* public ActionItem(CharSequence title, int mResourcesId) {
        this.mResourcesId = mResourcesId;
        this.mTitle = title;
    }*/

    public ActionItem(CharSequence title) {
        this.mTitle = title;
    }

    public ActionItem(CharSequence title, int color, int levelId,int courseId) {
        this.mTitle = title;
        this.mTextColor = color;
        this.mLevelId = levelId;
        this.mCourseId = courseId;
    }

}