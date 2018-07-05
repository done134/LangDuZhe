package com.cctv.langduzhe.data.entites;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/1/30.
 * 说明：评论实体类
 */
public class CommandEntity {


    /**
     * code : K-000000
     * data : [{"content":"fe","createDate":"2018-04-17 01:39:46","id":"71598596ba9c473bafcbcf4e732fad3a","mediaId":"57cba63030834ef2b7b06e7a3a7f28bf","readerId":"71cf35e687e54b328238c197cf191ca5","readerImg":"http://p4v9f6w5q.bkt.clouddn.com/mmexport1519805697755.jpg","readerName":"略略略"}]
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
         * content : fe
         * createDate : 2018-04-17 01:39:46
         * id : 71598596ba9c473bafcbcf4e732fad3a
         * mediaId : 57cba63030834ef2b7b06e7a3a7f28bf
         * readerId : 71cf35e687e54b328238c197cf191ca5
         * readerImg : http://p4v9f6w5q.bkt.clouddn.com/mmexport1519805697755.jpg
         * readerName : 略略略
         */

        private String content;
        private String createDate;
        private String id;
        private String mediaId;
        private String readerId;
        private String readerImg;
        private String readerName;

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

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public String getReaderId() {
            return readerId;
        }

        public void setReaderId(String readerId) {
            this.readerId = readerId;
        }

        public String getReaderImg() {
            return readerImg;
        }

        public void setReaderImg(String readerImg) {
            this.readerImg = readerImg;
        }

        public String getReaderName() {
            return readerName;
        }

        public void setReaderName(String readerName) {
            this.readerName = readerName;
        }
    }
}
