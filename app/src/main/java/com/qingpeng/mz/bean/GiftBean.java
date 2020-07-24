package com.qingpeng.mz.bean;

public class GiftBean {

    /**
     * data : [{"giftname":"大火箭","id":1,"imgurl":"/img","info":"赠送主播可触发特效","price":10000,"remark":"火"},{"giftname":"棒棒糖","id":2,"imgurl":"/img","info":"1314可触发特效","price":10,"remark":"连"},{"giftname":"端午香粽","id":3,"imgurl":"/img","info":"1314可触发特效","price":10,"remark":"新"},{"giftname":"菜角","id":4,"imgurl":"/img","info":"1314可触发特效","price":10,"remark":"新"},{"giftname":"糖糕","id":5,"imgurl":"/img","info":"1314可触发特效","price":10,"remark":"新"},{"giftname":"秘制小汉堡","id":6,"imgurl":"/img","info":"赠送召唤老八降临特效","price":9999999,"remark":"尊"}]
     * info : 礼物列表
     * status : 1
     */
    /**
     * giftname : 大火箭
     * id : 1
     * imgurl : /img
     * info : 赠送主播可触发特效
     * price : 10000
     * remark : 火
     */

    private String giftname;
    private int id;
    private String imgurl;
    private String info;
    private int price;
    private String remark;

    public String getGiftname() {
        return giftname;
    }

    public void setGiftname(String giftname) {
        this.giftname = giftname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
