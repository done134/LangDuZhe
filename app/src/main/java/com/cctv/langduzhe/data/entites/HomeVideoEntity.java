package com.cctv.langduzhe.data.entites;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gentleyin
 * on 2018/1/20.
 * 说明：
 */
public class HomeVideoEntity {

    /**
     * code : K-000000
     * data : [{"id":"1","createDate":"2018-01-30 12:44:09","updateDate":"2018-02-15 17:25:23","readerId":"1","readerName":"wwwww","readerType":"system","packageId":"1","title":"第一次表演哈哈","type":"video","path":"http://p3iptouqo.bkt.clouddn.com/testfile.mp4","img":"http://p3ip42qne.bkt.clouddn.com/testfile.jpg","publish":"1","isCollect":1,"watchSum":12,"commentSum":123,"collectSum":457},{"id":"2","createDate":"2018-01-30 12:44:09","updateDate":"2018-02-10 17:45:23","readerId":"1","readerName":"wwwww","readerType":"system","packageId":"1","termId":"1","seasonId":"1","termNum":1,"seasonNum":1,"title":"第2次表演哈哈","type":"video","path":"http://p3iptouqo.bkt.clouddn.com/testfile.mp4","img":"http://p3ip42qne.bkt.clouddn.com/testfile","publish":"1","isCollect":0,"watchSum":12,"commentSum":123,"collectSum":456}]
     */

    private String code;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * createDate : 2018-01-30 12:44:09
         * updateDate : 2018-02-15 17:25:23
         * readerId : 1
         * readerName : wwwww
         * readerType : system
         * packageId : 1
         * title : 第一次表演哈哈
         * type : video
         * path : http://p3iptouqo.bkt.clouddn.com/testfile.mp4
         * img : http://p3ip42qne.bkt.clouddn.com/testfile.jpg
         * publish : 1
         * isCollect : 1
         * watchSum : 12
         * commentSum : 123
         * collectSum : 457
         * termId : 1
         * seasonId : 1
         * termNum : 1
         * seasonNum : 1
         * "isLike": 0,
         * "likeSum": 20,
         */

        private String id;
        private String createDate;
        private String updateDate;
        private String readerId;
        private String readerName;
        private String readerType;
        private String packageId;
        private String readerImg;
        private String title;
        private String type;
        private String path;
        private String img;
        private String publish;
        private int isCollect;
        private int watchSum;
        private int commentSum;
        private int collectSum;
        private String termId;
        private String seasonId;
        private int termNum;
        private int seasonNum;
        private int duration;
        private int isLike;
        private int likeSum;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getReaderId() {
            return readerId;
        }

        public void setReaderId(String readerId) {
            this.readerId = readerId;
        }

        public String getReaderName() {
            return readerName;
        }

        public void setReaderName(String readerName) {
            this.readerName = readerName;
        }

        public String getReaderType() {
            return readerType;
        }

        public void setReaderType(String readerType) {
            this.readerType = readerType;
        }

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public String getReaderImg() {
            return readerImg;
        }

        public void setReaderImg(String readerImg) {
            this.readerImg = readerImg;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getPublish() {
            return publish;
        }

        public void setPublish(String publish) {
            this.publish = publish;
        }

        public int getIsCollect() {
            return isCollect;
        }

        public void setIsCollect(int isCollect) {
            this.isCollect = isCollect;
        }

        public int getWatchSum() {
            return watchSum;
        }

        public void setWatchSum(int watchSum) {
            this.watchSum = watchSum;
        }

        public int getCommentSum() {
            return commentSum;
        }

        public void setCommentSum(int commentSum) {
            this.commentSum = commentSum;
        }

        public int getCollectSum() {
            return collectSum;
        }

        public void setCollectSum(int collectSum) {
            this.collectSum = collectSum;
        }

        public String getTermId() {
            return termId;
        }

        public void setTermId(String termId) {
            this.termId = termId;
        }

        public String getSeasonId() {
            return seasonId;
        }

        public void setSeasonId(String seasonId) {
            this.seasonId = seasonId;
        }

        public int getTermNum() {
            return termNum;
        }

        public void setTermNum(int termNum) {
            this.termNum = termNum;
        }

        public int getSeasonNum() {
            return seasonNum;
        }

        public void setSeasonNum(int seasonNum) {
            this.seasonNum = seasonNum;
        }


        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getIsLike() {
            return isLike;
        }

        public void setIsLike(int isLike) {
            this.isLike = isLike;
        }

        public int getLikeSum() {
            return likeSum;
        }

        public void setLikeSum(int likeSum) {
            this.likeSum = likeSum;
        }
    }
}
