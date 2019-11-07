package com.shuai.retrofitrx.net.download.progress;

/**
 * @hide Created by liuqing on 17/10/9.
 */
public interface DownloadManagerResponseListener {
    void onStart(String url, DownloadHandler cancellable, long contentLength);

    void onProgress(String url, long totleLength, long downloadedLength);

    void onDownloadSuccess(String url, String cacheFilePath);

    void onDownloadFail(String url, Throwable e);

    void onCancelled(String url);

    void onPauseed(String url, long downloadedLength);


}
