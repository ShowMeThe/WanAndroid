package com.showmethe.wanandroid.entity;

/**
 * com.ken.materialwanandroid.entity
 * <p>
 * 2019/9/4
 **/
public class Banner {

    /**
     * desc : 腾讯、百度、京东面试真题，你能答对几道？
     * id : 27
     * imagePath : https://www.wanandroid.com/blogimgs/9f40797e-4bf0-42fd-bcc6-9df7fdbda16a.jpeg
     * isVisible : 1
     * order : 1
     * title : 腾讯、百度、京东面试真题，你能答对几道？
     * type : 0
     * url : https://mp.weixin.qq.com/s/yPeMJUw5k3MPg36-wVZzdA
     */

    private String desc;
    private int id;
    private String imagePath;
    private int isVisible;
    private int order;
    private String title;
    private int type;
    private String url;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
