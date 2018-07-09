package com.cctv.langduzhe.data.entites;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/7/5.
 * 说明：查询媒体包列表接口公用View层 type 参数值为 t=期,s=季

 */
public class MediaPackageEntity {

    /**
     * code : K-000000
     * data : [{"createDate":"2018-06-28 18:10:13","id":"2","img":"","name":"第二季","readerId":"1","sortNum":2,"type":"season","updateDate":"2018-06-28 18:10:13"},{"createDate":"2018-04-24 22:49:03","id":"3ee107fa47ef4903934f9105014437af","img":"/userfiles/1/images/packagecover/2018/04/theme_yujian.png","name":"第一季","readerId":"1","sortNum":1,"type":"season","updateDate":"2018-04-24 22:49:03"}]
     */

    private String code;
    /**
     * createDate : 2018-06-28 18:10:13
     * id : 2
     * img :
     * name : 第二季
     * readerId : 1
     * sortNum : 2
     * type : season
     * updateDate : 2018-06-28 18:10:13
     */

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

    public static class DataBean {
        private String createDate;
        private String id;
        private String img;
        private String name;
        private String readerId;
        private int sortNum;
        private String type;
        private String updateDate;

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getReaderId() {
            return readerId;
        }

        public void setReaderId(String readerId) {
            this.readerId = readerId;
        }

        public int getSortNum() {
            return sortNum;
        }

        public void setSortNum(int sortNum) {
            this.sortNum = sortNum;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }
    }
}
