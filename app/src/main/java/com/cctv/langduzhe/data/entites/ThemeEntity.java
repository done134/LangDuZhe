package com.cctv.langduzhe.data.entites;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gentleyin
 * on 2018/3/19.
 * 说明：
 */
public class ThemeEntity {


    /**
     * code : K-000000
     * data : [{"createDate":"2018-03-19 11:16:08","id":"1","img":"http://pic.jj20.com/up/allimg/1011/121416110642/161214110642-2.jpg","title":"大家好"}]
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

    public static class DataBean implements Serializable {
        /**
         * createDate : 2018-03-19 11:16:08
         * id : 1
         * img : http://pic.jj20.com/up/allimg/1011/121416110642/161214110642-2.jpg
         * title : 大家好
         */

        private String createDate;
        private String id;
        private String img;
        private String title;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
