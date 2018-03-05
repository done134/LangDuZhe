package com.cctv.langduzhe.data.entites;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by gentleyin
 * on 2018/2/4.
 * 说明：未发布朗读作品实体类
 */
@DatabaseTable(tableName = "not_post")
public class NotPostEntity {

    public static final String ID_FIELD_NAME = "_id";
    public static final String READ_NAME = "name";
    public static final String READ_TYPE = "type";
    public static final String READ_FILE_PATH = "path";
    public static final String COVER_PATH = "cover_path";

    @DatabaseField(columnName = ID_FIELD_NAME, generatedId = true)
    public int id;

    /**
     * voice type==0, video type ==1
     */
    @DatabaseField(columnName = READ_TYPE)
    public int type;

    @DatabaseField(columnName = READ_NAME)
    public String readName;

    @DatabaseField(columnName = READ_FILE_PATH)
    public String readFilepath;

    @DatabaseField(columnName = COVER_PATH)
    public String coverPath;
    public NotPostEntity(int type) {
        this.type = type;
    }

    public NotPostEntity() {

    }
}
