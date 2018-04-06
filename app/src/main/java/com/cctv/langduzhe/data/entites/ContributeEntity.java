package com.cctv.langduzhe.data.entites;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/3/18.
 * 说明：
 */
public class ContributeEntity {

    /**
     * code : K-000000
     * data : [{"content":"sdfsdfsvewewewfwefwefwefwefwefw","createDate":"2018-03-18 23:29:52","id":"10ab744951d5418ca1cf25cffe4bd386","shortContent":"sdfsdfsvewewewfwefwefwefw","title":"cews"}]
     */

    private String code;
    private String message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * content : sdfsdfsvewewewfwefwefwefwefwefw
         * createDate : 2018-03-18 23:29:52
         * id : 10ab744951d5418ca1cf25cffe4bd386
         * shortContent : sdfsdfsvewewewfwefwefwefw
         * title : cews
         */

        private String content;
        private String createDate;
        private String id;
        private String shortContent;
        private String title;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

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

        public String getShortContent() {
            return shortContent;
        }

        public void setShortContent(String shortContent) {
            this.shortContent = shortContent;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
