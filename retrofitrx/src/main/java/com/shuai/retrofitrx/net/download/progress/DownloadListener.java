package com.shuai.retrofitrx.net.download.progress;

/**
 * Created by liuqing on 17/10/12.
 */

public interface DownloadListener {
    void onStart();

    void onProgress(long totleLength, long downloadedLength);

    void onFail(Throwable e);

    void onSuccess(String filePath);

    void onCanceled();

    void onPauseed();
}
