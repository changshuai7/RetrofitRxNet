package com.shuai.retrofitrx.net.download;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuqing on 18/2/6.
 */

public class DownloadRecord {
    public static final String DB_FILEPATH = "localFilePath";//本地文件路径
    public static final String DB_DOWNLOADURL = "downloadUrl";//下载地址
    public static final String DB_STATUS = "downloadStatus";//下载状态
    public static final String DB_CONTENTLENGTH = "fileTotelLength";//文件总的大小
    public static final String DB_DOWNLOADEDLENGTH = "fileDownloadedLength";//文件已下载的大小
    public static final String DB_UPDATETIME = "updateTime";//最近一次操作的时间
    public static final String DB_TAG = "tag";//预留字段
    public static final String DB_ID = "id";

    public static final int STATUS_UNKNOW = 0;
    public static final int STATUS_WAITING = 1;
    public static final int STATUS_CONNECTING = 2;
    public static final int STATUS_DOWNLOADING = 3;
    public static final int STATUS_PAUSE = 4;
    public static final int STATUS_CANCEL = 5;
    public static final int STATUS_ERROR = 6;
    public static final int STATUS_FINISH = 7;


    private int id;
    private String localFilePath;
    private String downloadUrl;
    private int downloadStatus;
    private long fileContentLength;
    private long fileDownloadedLength;
    private long updateTime;
    private String tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public long getFileContentLength() {
        return fileContentLength;
    }

    public void setFileContentLength(long fileContentLength) {
        this.fileContentLength = fileContentLength;
    }

    public long getFileDownloadedLength() {
        return fileDownloadedLength;
    }

    public void setFileDownloadedLength(long fileDownloadedLength) {
        this.fileDownloadedLength = fileDownloadedLength;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "DownloadRecord{" +
                "id=" + id +
                ", localFilePath='" + localFilePath + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", downloadStatus=" + downloadStatus +
                ", fileContentLength=" + fileContentLength +
                ", fileDownloadedLength=" + fileDownloadedLength +
                ", updateTime=" + updateTime +
                ", tag='" + tag + '\'' +
                '}';
    }

    public ContentValues asContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_FILEPATH, localFilePath);
        contentValues.put(DB_DOWNLOADURL, downloadUrl);
        contentValues.put(DB_STATUS, downloadStatus);
        contentValues.put(DB_CONTENTLENGTH, fileContentLength);
        contentValues.put(DB_DOWNLOADEDLENGTH, fileDownloadedLength);
        contentValues.put(DB_UPDATETIME,updateTime);
        contentValues.put(DB_TAG,tag);
        return contentValues;
    }

    public static List<DownloadRecord> cur2Records(Cursor cursor) {
        List<DownloadRecord> datas = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    DownloadRecord itemData = new DownloadRecord();
                    itemData.setId(cursor.getInt(cursor.getColumnIndex(DB_ID)));
                    itemData.setLocalFilePath(cursor.getString(cursor.getColumnIndex(DB_FILEPATH)));
                    itemData.setDownloadUrl(cursor.getString(cursor.getColumnIndex(DB_DOWNLOADURL)));
                    itemData.setDownloadStatus(cursor.getInt(cursor.getColumnIndex(DB_STATUS)));
                    itemData.setFileContentLength(Long.valueOf(cursor.getString(cursor.getColumnIndex(DB_CONTENTLENGTH))));
                    itemData.setFileDownloadedLength(Long.valueOf(cursor.getString(cursor.getColumnIndex(DB_DOWNLOADEDLENGTH))));
                    itemData.setUpdateTime(Long.valueOf(cursor.getString(cursor.getColumnIndex(DB_UPDATETIME))));
                    itemData.setTag(cursor.getString(cursor.getColumnIndex(DB_TAG)));
                    datas.add(itemData);
                } while (cursor.moveToNext());
            }
        }
        return datas;
    }

    public static DownloadRecord cur2Record(Cursor cursor) {
        DownloadRecord record = null;
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                record = new DownloadRecord();
                record.setId(cursor.getInt(cursor.getColumnIndex(DB_ID)));
                record.setLocalFilePath(cursor.getString(cursor.getColumnIndex(DB_FILEPATH)));
                record.setDownloadUrl(cursor.getString(cursor.getColumnIndex(DB_DOWNLOADURL)));
                record.setDownloadStatus(cursor.getInt(cursor.getColumnIndex(DB_STATUS)));
                record.setFileContentLength(Long.valueOf(cursor.getString(cursor.getColumnIndex(DB_CONTENTLENGTH))));
                record.setFileDownloadedLength(Long.valueOf(cursor.getString(cursor.getColumnIndex(DB_DOWNLOADEDLENGTH))));
                record.setUpdateTime(Long.valueOf(cursor.getString(cursor.getColumnIndex(DB_UPDATETIME))));
                record.setTag(cursor.getString(cursor.getColumnIndex(DB_TAG)));
            }
        }
        return record;
    }
}
