package com.arch.demo.common.api;


import com.arch.demo.common.bean.ShareBean;
import com.pluto.network.beans.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * ================================================
 *
 * @function <p>
 * Created by szy on 2019-08-03
 * ================================================
 */
public interface ShareApi {

    // 获取分享信息
    @POST("/v1/api/userShare/get")
    @FormUrlEncoded
    Observable<BaseResponse<ShareBean>> getShareInfo(
            @Field("userId") String userId,
            @Field("itemId") String itemId,
            @Field("type") String type
    );


    // 获取分享信息
    @GET("/v2/user/userShare/userShareCheckIn")
    Observable<BaseResponse<ShareBean>> getStudyReportShareInf(@Query("lessonId") String lessonId, @Header("Authorization")String auth);

    // 绘本解锁
    @POST("/v1/api/userBookUnlock/unlock")
    @FormUrlEncoded
    Observable<BaseResponse<Object>> unlockBook(
            @Field("userId") String userId,
            @Field("bookId") String bookId,
            @Field("type") String type
    );

    // 获取二维码图片地址
    @POST("/v1/api/assemble/makeShareRQ")
    @FormUrlEncoded
    Observable<BaseResponse<String>> getQRCodeImagePath(@Field("orderId") String orderId);

    // 获取分享信息
    @GET("/v2/user/userShare/userShareCheckIn")
    Observable<BaseResponse<ShareBean>> getShareDataById(@Query("lessonId") String lessonId, @Header("Authorization")String auth);
}
