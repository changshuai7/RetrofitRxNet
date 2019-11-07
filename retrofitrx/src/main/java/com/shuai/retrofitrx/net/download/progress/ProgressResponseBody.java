package com.shuai.retrofitrx.net.download.progress;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by liuqing on 17/10/9.
 */

public class ProgressResponseBody extends ResponseBody implements DownloadHandler {
    private static final String TAG = ProgressResponseBody.class.getSimpleName();
    private ResponseBody body;
    private DownloadManagerResponseListener listener;
    private BufferedSource bufferedSource;
    private String url;
    private long startPos;
    private boolean cancelled = false;
    private boolean pasused = false;

    public ProgressResponseBody(String url, long startPos, ResponseBody responseBody, DownloadManagerResponseListener listener) {
        this.url = url;
        this.startPos = startPos;
        this.body = responseBody;
        this.listener = listener;
    }

    public ProgressResponseBody(String url, ResponseBody body, DownloadManagerResponseListener listener) {
        this(url, 0, body, listener);
    }

    @Override
    public MediaType contentType() {
        return body.contentType();
    }

    @Override
    public long contentLength() {
        return body.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (null == bufferedSource) {
            bufferedSource = Okio.buffer(source(body.source()));
            if (listener != null) {
                listener.onStart(url, this, body.contentLength() + startPos);
            }
        }
        return bufferedSource;

    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = startPos;
            long contentLength = startPos;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                if (!cancelled && !pasused) {
                    long bytesRead = super.read(sink, byteCount);
                    if (listener != null) {
                        if (contentLength == startPos) {
                            contentLength = body.contentLength()+startPos;
                        }
                        totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                        listener.onProgress(url, contentLength, totalBytesRead);
                    }
                    return bytesRead;
                } else {
                    if (cancelled) {
                        if (listener != null) {
                            listener.onCancelled(url);
                        }
                    } else {
                        if (listener != null) {
                            listener.onPauseed(url, totalBytesRead);
                        }
                    }
                    return -1;
                }
            }
        };
    }

    @Override
    public void puase() {
        pasused = true;
    }

    @Override
    public boolean isPuaseed() {
        return pasused;
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public boolean isCanceled() {
        return cancelled;
    }

}

