package com.shuai.retrofitrx.net.download.progress;

/**
 * Created by liuqing on 17/10/30.
 */

public interface DownloadHandler {
     void puase();
     boolean isPuaseed();
     void cancel();
     boolean isCanceled();
}
