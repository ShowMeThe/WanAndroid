package com.showmethe.wanandroid.entity;

import java.util.ArrayList;

/**
 * com.ken.materialwanandroid.entity
 * <p>
 * 2019/9/9
 **/
public class Collect {


    /**
     * curPage : 2
     * datas : [{"author":"包牙齿","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85020,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857315000,"title":"最近给团队新同学分享的git markdown","userId":30077,"visible":0,"zan":0},{"author":"xjz729827161","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85019,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857314000,"title":"Gradle工作原理全面了解","userId":30077,"visible":0,"zan":0},{"author":"ZYLAB","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85018,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857313000,"title":"【Android 修炼手册】Gradle 篇 -- Android Gradle Plugin 主要 Task 分析","userId":30077,"visible":0,"zan":0},{"author":"heqiangfly","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85016,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857278000,"title":"Gradle 使用指南 -- Gradle 生命周期","userId":30077,"visible":0,"zan":0},{"author":"包牙齿","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85015,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857275000,"title":"最近给团队新同学分享的git markdown","userId":30077,"visible":0,"zan":0},{"author":"xjz729827161","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85014,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857272000,"title":"Gradle工作原理全面了解","userId":30077,"visible":0,"zan":0},{"author":"ZYLAB","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85013,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857269000,"title":"【Android 修炼手册】Gradle 篇 -- Android Gradle Plugin 主要 Task 分析","userId":30077,"visible":0,"zan":0},{"author":"ZYLAB","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85012,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857255000,"title":"【Android 修炼手册】Gradle 篇 -- Android Gradle Plugin 主要 Task 分析","userId":30077,"visible":0,"zan":0},{"author":"包牙齿","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85009,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857152000,"title":"最近给团队新同学分享的git markdown","userId":30077,"visible":0,"zan":0},{"author":"包牙齿","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85008,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857131000,"title":"最近给团队新同学分享的git markdown","userId":30077,"visible":0,"zan":0}]
     * offset : 20
     * over : true
     * pageCount : 2
     * size : 20
     * total : 30
     */

    private int curPage;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;
    private ArrayList<DatasBean> datas;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * author : 包牙齿
         * chapterId : 0
         * chapterName :
         * courseId : 13
         * desc :
         * envelopePic :
         * id : 85020
         * link :
         * niceDate : 1天前
         * origin :
         * originId : -1
         * publishTime : 1567857315000
         * title : 最近给团队新同学分享的git markdown
         * userId : 30077
         * visible : 0
         * zan : 0
         */

        private int imgWidth = 0;
        private int imgHeight = 0;
        private String author;
        private int chapterId;
        private String chapterName;
        private int courseId;
        private String desc;
        private String envelopePic;
        private int id;
        private String link;
        private String niceDate;
        private String origin;
        private int originId;
        private long publishTime;
        private String title;
        private int userId;
        private int visible;
        private int zan;

        public int getImgWidth() {
            return imgWidth;
        }

        public void setImgWidth(int imgWidth) {
            this.imgWidth = imgWidth;
        }

        public int getImgHeight() {
            return imgHeight;
        }

        public void setImgHeight(int imgHeight) {
            this.imgHeight = imgHeight;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getChapterId() {
            return chapterId;
        }

        public void setChapterId(int chapterId) {
            this.chapterId = chapterId;
        }

        public String getChapterName() {
            return chapterName;
        }

        public void setChapterName(String chapterName) {
            this.chapterName = chapterName;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getEnvelopePic() {
            return envelopePic;
        }

        public void setEnvelopePic(String envelopePic) {
            this.envelopePic = envelopePic;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getNiceDate() {
            return niceDate;
        }

        public void setNiceDate(String niceDate) {
            this.niceDate = niceDate;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public int getOriginId() {
            return originId;
        }

        public void setOriginId(int originId) {
            this.originId = originId;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getVisible() {
            return visible;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }

        public int getZan() {
            return zan;
        }

        public void setZan(int zan) {
            this.zan = zan;
        }
    }
}
