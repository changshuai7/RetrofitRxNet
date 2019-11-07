package com.shuai.retrofitrx.net.download;

import com.shuai.retrofitrx.net.download.progress.DownloadListener;

/**
 * Created by liuqing on 18/2/6.
 * DownloadManager操作库，包含所有操作下载任务的函数
 */

public interface IDownloadHandle {
    /**
     * 添加下载任务/恢复断点下载（需要传入相同的url以及filepath）
     *
     * @param url      下载任务的url
     * @param filePath 下载文件的缓存文件的路径，即是最终的文件存储路径
     * @param tag      下载任务的tag，预留字段
     * @param listener 下载过程回调 {@link DownloadListener}
     * @return true 为添加下载任务成功
     * false 为添加下载任务失败，失败现在只有一个原因，就是已经存在相同下载url的任务
     */
    boolean addDownloadTask(String url, String filePath, String tag, DownloadListener listener);
    /**
     * 添加下载任务/恢复断点下载（需要传入相同的url以及filepath）
     *
     * @param url      下载任务的url
     * @param filePath 下载文件的缓存文件的路径，即是最终的文件存储路径
     * @param listener 下载过程回调 {@link DownloadListener}
     * @return true 为添加下载任务成功
     * false 为添加下载任务失败，失败现在只有一个原因，就是已经存在相同下载url的任务
     */
    boolean addDownloadTask(String url, String filePath, DownloadListener listener);
    /**
     * 判断当前是否有这个url的下载任务正在执行
     * @return boolean 是否存在正在执行的这个url
     * */
    boolean isDownloading(String url);
    /**
     * 取消url的下载任务
     * @return boolean 是否成功取消
     * */
    boolean cancelTask(String url);
    /**
     * 暂停url的下载任务
     * @return boolean 是否成功暂停
     * */
    boolean pauseTask(String url);
    /**
     * 设置最大请求并发数，以及每个host的并发数
     * @param maxRequestCount 最大的请求并发数
     * @param maxRequestCountForPerHost 每个域名最大的并发数
     * */
    void setMaxRequestCount(int maxRequestCount, int maxRequestCountForPerHost);

}
