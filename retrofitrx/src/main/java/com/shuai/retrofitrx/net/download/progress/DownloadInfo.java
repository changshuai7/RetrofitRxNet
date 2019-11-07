package com.shuai.retrofitrx.net.download.progress;

/**
 * Created by liuqing on 18/2/6.
 */

/**
 * 下载信息类
 * 包含下载的url,缓存目录，下载回掉对象，取消回掉
 */
public class DownloadInfo {
    private String url;
    private String filePath;
    private DownloadListener listener;
    private DownloadHandler handler;

    public DownloadInfo(String url, String filePath, DownloadListener listener) {
        this.url = url;
        this.filePath = filePath;
        this.listener = listener;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DownloadListener getListener() {
        return listener;
    }

    public void setListener(DownloadListener listener) {
        this.listener = listener;
    }

    public DownloadHandler getHandler() {
        return handler;
    }

    public void setHandler(DownloadHandler handler) {
        this.handler = handler;
    }
}
