package com.cctv.langduzhe.data.entites;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/1/30.
 * 说明：评论实体类
 */
public class CommandEntity {

    public String headUrl;
    public String nickName;
    public String commandTime;
    public String commandContent;


    /**
     * code : K-000000
     * data : [{"id":"143a66fadde84e6cb9e7e334a4125339","readerId":"1","mediaId":"1","content":"string"},{"id":"41876a0512b84c56b50f3cab739a998d","readerId":"1","mediaId":"string","content":"string"},{"id":"92a64cb1997945c6b2817b4dab990114","readerId":"1","mediaId":"string","content":"<script type=\"text/javascript\">123<\/script>456"},{"id":"be142d6992324893980e3e61e101e852","readerId":"1","mediaId":"string","content":"<script type=\"text/javascript\">123<\/script>456"}]
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

    public static class DataBean {
        /**
         * id : 143a66fadde84e6cb9e7e334a4125339
         * readerId : 1
         * mediaId : 1
         * content : string
         */

        private String id;
        private String readerId;
        private String mediaId;
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getReaderId() {
            return readerId;
        }

        public void setReaderId(String readerId) {
            this.readerId = readerId;
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
