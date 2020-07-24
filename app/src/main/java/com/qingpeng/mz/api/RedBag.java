package com.qingpeng.mz.api;


import com.qingpeng.mz.bean.AvaterBean;
import com.qingpeng.mz.bean.CoverBean;
import com.qingpeng.mz.bean.FollowListBean;
import com.qingpeng.mz.bean.GiftBean;
import com.qingpeng.mz.bean.GiftListBean;
import com.qingpeng.mz.bean.GiftRecordBean;
import com.qingpeng.mz.bean.GuanLiBean;
import com.qingpeng.mz.bean.HuaTiBean;
import com.qingpeng.mz.bean.LoginBean;
import com.qingpeng.mz.bean.RoomGameInfoBean;
import com.qingpeng.mz.bean.RoomInfoBean;
import com.qingpeng.mz.bean.RoomInfoListBean;
import com.qingpeng.mz.bean.ToUserInfoBean;
import com.qingpeng.mz.bean.UserBetsBean;
import com.qingpeng.mz.bean.UserInfoBean;
import com.qingpeng.mz.bean.WebUrlBean;
import com.qingpeng.mz.bean.ZhuBoRoomInfoBean;
import com.qingpeng.mz.okhttp.APIResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RedBag {


    @FormUrlEncoded
    @POST("/login")
    Call<APIResponse<LoginBean>> login(@Field("account") String account, @Field("password") String password);//登录

    @FormUrlEncoded
    @POST("/register")
    Call<APIResponse> register(@Field("account") String account, @Field("nickname") String nickname, @Field("password") String password);//注册

    @FormUrlEncoded
    @POST("/RoomTypelistinfo")
    Call<APIResponse<List<RoomInfoBean>>> RoomTypelistinfo(@Field("desk_id") String desk_id);//房间信息


    @FormUrlEncoded
    @POST("/RoomTypelist")
    Call<APIResponse<List<RoomInfoBean>>> RoomTypelist(@Field("game_id") String game_id);//大厅游戏列表


    @FormUrlEncoded
    @POST("/UserBetsFee")
    Call<APIResponse<UserBetsBean>> UserBetsFee(@Field("game_id") String game_id);//倍率


    @FormUrlEncoded
    @POST("/{url}")
    Call<APIResponse> cancel(@Path("url") String url, @FieldMap Map<String, String> maps);//下注取消.


    @FormUrlEncoded
    @POST("/{url}")
    Call<APIResponse> xiazhu(@Path("url") String url, @FieldMap Map<String, String> maps);//下注.


    @FormUrlEncoded
    @POST("/{url}")
    Call<APIResponse> xiugaibangding(@Path("url") String url, @FieldMap Map<String, String> maps);//修改绑定


    @FormUrlEncoded
    @POST("/gift_record")
    Call<APIResponse<List<GiftRecordBean>>> gift_record(@Field("type") String type);//收送礼物记录


    @POST("/{url}")
    Call<APIResponse<List<HuaTiBean>>> huatipindao(@Path("url") String url);//频道 话题


    @FormUrlEncoded
    @POST("/SetCover")
    Call<APIResponse<CoverBean>> SetCover(@FieldMap Map<String, String> maps);//开播.


    @FormUrlEncoded
    @POST("/RoomInfo")
    Call<APIResponse<ZhuBoRoomInfoBean>> RoomInfo(@Field("room_id") String room_id);//主播房间信息.


    @FormUrlEncoded
    @POST("/ChanneRoomList")
    Call<APIResponse<List<RoomInfoListBean>>> RoomList(@Field("channe_id") String channe_id, @Field("last_time") String room_id);//大厅直播列表


    @FormUrlEncoded
    @POST("/LiveOver")
    Call<APIResponse> LiveOver(@Field("room_id") String room_id);//直播关闭


    @FormUrlEncoded
    @POST("/{url}")
    Call<APIResponse> do_follow_live(@Path("url") String url, @Field("live_uid") String live_uid);//关注主播


    @POST("/follow_list")
    Call<APIResponse<List<FollowListBean>>> follow_list();//关注主播的列表


    @FormUrlEncoded
    @POST("/GetGiftRankList")
    Call<APIResponse<GiftListBean>> GetGiftRankList(@Field("room_id") String room_id);//礼物排行


    @FormUrlEncoded
    @POST("/change_nickname")
    Call<APIResponse> change_nickname(@Field("nickname") String nickname);//修改昵称


    @FormUrlEncoded
    @POST("/ToUserinfo")
    Call<APIResponse<ToUserInfoBean>> ToUserinfo(@Field("room_id") String room_id, @Field("touid") String touid);//查看其它用户信息


    @FormUrlEncoded
    @POST("/{url}")
    Call<APIResponse> SetRoomManager(@Path("url") String url, @FieldMap Map<String, String> maps);//设置管理/踢人/禁言


    @FormUrlEncoded
    @POST("/RoomGameInfo")
    Call<APIResponse> RoomGameInfo(@FieldMap Map<String, String> maps);//获取房间游戏信息


    @POST("/GetGift")
    Call<APIResponse<List<GiftBean>>> GetGift();//礼物列表


    @FormUrlEncoded
    @POST("/GetRoomGameInfo")
    Call<APIResponse<RoomGameInfoBean>> GetRoomGameInfo(@Field("room_id") String room_id);//礼物排行


    @FormUrlEncoded
    @POST("/send_gift")
    Call<APIResponse> send_gift(@FieldMap Map<String, String> maps);//获取房间游戏信息


    @POST("/Userinfo")
    Call<APIResponse<UserInfoBean>> Userinfo();//用户信息


    @POST("/weburl")
    Call<APIResponse<WebUrlBean>> weburl();//视讯地址


    @POST("/code/Mycenter/upavater")
    @Multipart
    Call<APIResponse<AvaterBean>> upavater(@Part MultipartBody.Part MultipartFile); //上传头像

    @FormUrlEncoded
    @POST("/LiveLeaveRoom")
    Call<APIResponse> LiveLeaveRoom(@Field("room_id") String room_id);//离开房间


    @FormUrlEncoded
    @POST("/LiveReturnRoom")
    Call<APIResponse> LiveReturnRoom(@Field("room_id") String room_id);//回到房间


    @FormUrlEncoded
    @POST("/RoomLiveManager")
    Call<APIResponse<GuanLiBean>> RoomLiveManager(@Field("room_id") String room_id);//管理员列表

    @FormUrlEncoded
    @POST("/RelieveRoomManager")
    Call<APIResponse> RelieveRoomManager(@Field("room_id") String room_id, @Field("manager_id") String manager_id);//取消管理员

}
