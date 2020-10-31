package com.wl.pluto.arch.base.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.wl.pluto.arch.R;
import com.wl.pluto.arch.glide.GlideEngine;
import com.wl.pluto.arch.listener.OnImageUploadCallBackListener;
import com.wl.pluto.arch.listener.TakePhotoListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import imageloader.libin.com.images.loader.ImageLoader;

/**
 * Created by dingli on 16/6/8.
 */
public abstract class AbsTakePhotoActivity extends BaseActivity implements TakePhotoListener {

    protected LinearLayoutCompat mAddImageLayout;

    protected List<String> mImagePathList = new LinkedList<String>();
    protected int type = 0;

    protected String mCurrentStatus;
    /**
     * 最大上传5张图片
     */
    protected final int MAX_NUM = 5;


    /**
     * 添加额外的图片
     *
     * @param map
     */
    protected void setExtImageUrlList(HashMap<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        if (mImagePathList != null && mImagePathList.size() != 0) {
            for (int i = 0; i < mImagePathList.size(); i++) {
                if (i == 0) {
                    sb.append(mImagePathList.get(i));
                } else {
                    sb.append(",").append(mImagePathList.get(i));
                }
            }
            map.put("imageExtraList", sb.toString());
        }
    }

    /**
     * 展示额外的
     *
     * @param imageUrlList
     */
    protected void showExtImage(String imageUrlList) {
        if (!TextUtils.isEmpty(imageUrlList)) {
            String[] imageArray = imageUrlList.split(",");
            if (imageArray != null && imageArray.length !=0) {
                for (int i = 0; i < imageArray.length; i++) {
                    addImageView(imageArray[i], null);
                }
            }
        }
    }

    protected boolean isCanAddImage() {
        if (mImagePathList.size() >= MAX_NUM) {
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     * @param imagePath 图片的本地地址
     * @param imageUrl  图片的网络地址
     */
    protected void addImageView(final String imagePath,  String imageUrl) {
        View view = View.inflate(AbsTakePhotoActivity.this, R.layout.item_iamge_layout, null);
        QMUIRadiusImageView imageView = view.findViewById(R.id.iv_image);
        ImageView delete = view.findViewById(R.id.iv_delete);

        //认证中
        if("1".equals(mCurrentStatus)){
            delete.setVisibility(View.GONE);
        }else {
            delete.setVisibility(View.VISIBLE);
        }
        delete.setOnClickListener(v -> {
            if (mAddImageLayout != null) {
                mAddImageLayout.removeView(view);

                if(TextUtils.isEmpty(imageUrl)){
                    mImagePathList.remove(imagePath);
                }else {
                    mImagePathList.remove(imageUrl);
                }

            }
        });
        ImageLoader.with(getBaseContext()).url(imagePath).into(imageView);

        if (mAddImageLayout != null) {
            mAddImageLayout.addView(view);
            if(TextUtils.isEmpty(imageUrl)){
                mImagePathList.add(imagePath);
            }else {
                mImagePathList.add(imageUrl);
            }

        }
    }

    private void deleteImagePath(String path) {
        Iterator<String> it = mImagePathList.iterator();
        while (it.hasNext()) {
            String imagePath = it.next();

            if (TextUtils.equals(path, imagePath)) {
                it.remove();
            }
        }
    }


    public void onOpenGallery(int maxSelectNum, boolean isCropImage, List<LocalMedia> localMedia, boolean isOriginalImage) {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .selectionData(localMedia)
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(maxSelectNum)
                .isEnableCrop(isCropImage)
                .circleDimmedLayer(isCropImage)
                .showCropFrame(false)
                .showCropGrid(false)
                .isAndroidQTransform(true)
                .freeStyleCropEnabled(isCropImage)
                .isCompress(true)
                .compressQuality(60)
                .isOriginalImageControl(isOriginalImage)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        onSelectImageCallback(result);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
        ;
    }

    public void onOpenGallery(int maxSelectNum, List<LocalMedia> localMedia) {
        onOpenGallery(maxSelectNum, false, localMedia, false);
    }


    public void onOpenCamera(final boolean isCropImage) {
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .isWeChatStyle(true)
                .theme(R.style.picture_WeChat_style)
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .selectionMode(PictureConfig.SINGLE)
                .isEnableCrop(isCropImage)
                .isSingleDirectReturn(true)
                //.circleDimmedLayer(isCropImage)
                .isAndroidQTransform(true)
                .showCropFrame(false)
                .showCropGrid(false)
                .freeStyleCropEnabled(isCropImage)
                .isCompress(true)
                .compressQuality(60)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                               @Override
                               public void onResult(List<LocalMedia> result) {
                                   LocalMedia localMedia = result.get(0);
                                   if (isCropImage) {
                                       onSelectImageCallback(TextUtils.isEmpty(localMedia.getCutPath()) ? (TextUtils.isEmpty(localMedia.getCompressPath()) ? localMedia.getRealPath() : localMedia.getCompressPath()) : localMedia.getCutPath());
                                   } else {
                                       onSelectImageCallback(TextUtils.isEmpty(localMedia.getCompressPath()) ? (TextUtils.isEmpty(localMedia.getOriginalPath()) ? localMedia.getRealPath() : localMedia.getOriginalPath()) : localMedia.getCutPath());
                                   }
                               }

                               @Override
                               public void onCancel() {

                               }
                           }
                );
    }


    public void onOpenPhoto(final boolean isCropImage) {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .isWeChatStyle(true)
                .theme(R.style.picture_WeChat_style)
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .selectionMode(PictureConfig.SINGLE)
                .isAndroidQTransform(true)
                .isEnableCrop(isCropImage)
                // .circleDimmedLayer(isCropImage) //控制中间的圆形图层
                .isSingleDirectReturn(true)
                // .showCropFrame(true)
                .showCropGrid(false)
                .freeStyleCropEnabled(isCropImage)
                .isCompress(true)
                .compressQuality(60)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        LocalMedia localMedia = result.get(0);
                        if (isCropImage) {
                            onSelectImageCallback(TextUtils.isEmpty(localMedia.getCutPath()) ? (TextUtils.isEmpty(localMedia.getCompressPath()) ? localMedia.getRealPath() : localMedia.getCompressPath()) : localMedia.getCutPath());
                        } else {
                            onSelectImageCallback(TextUtils.isEmpty(localMedia.getCompressPath()) ? (TextUtils.isEmpty(localMedia.getOriginalPath()) ? localMedia.getRealPath() : localMedia.getOriginalPath()) : localMedia.getCutPath());
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    @Override
    public void onSelectImageCallback(String imagePath) {
    }

    @Override
    public void onSelectImageCallback(List<LocalMedia> mediaList) {

    }

    /**
     * 上传图片
     *
     * @param imagePath                     图片地址
     * @param onImageUploadCallBackListener 上传回调
     */
    public void publishPhoto(String imagePath, OnImageUploadCallBackListener onImageUploadCallBackListener) {

//        showLoadingDialog("正在上传");
//        UploadFileReq uploadFileReq = new UploadFileReq();
//        uploadFileReq.setFile(new File(imagePath));
//        uploadFileReq.setFileName(imagePath);
//        AZFile.getSession().uploadImage(uploadFileReq, (req, resp) -> {
//            if (resp.isSuccess()) {
//                if (resp instanceof UploadFileResp) {
//                    UploadFileResp uploadFileResp = (UploadFileResp) resp;
//                    if (uploadFileResp.getCode() == 200) {
//                        hindLoadingDialog();
//                        onImageUploadCallBackListener.onImageUploadSuccess(uploadFileResp.getData());
//                    } else {
//                        onImageUploadCallBackListener.onImageUploadError(uploadFileResp.getMessage());
//                    }
//                }
//            }
//        });
    }


}
