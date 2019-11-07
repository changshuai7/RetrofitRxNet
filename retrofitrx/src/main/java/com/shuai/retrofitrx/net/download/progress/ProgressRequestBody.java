package com.shuai.retrofitrx.net.download.progress;

import com.shuai.retrofitrx.config.NetConfig;
import com.shuai.retrofitrx.utils.Logger;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by liuqing on 17/10/9.
 */

public class ProgressRequestBody extends RequestBody {
    private static final String TAG = ProgressRequestBody.class.getSimpleName();
    private String filePath;
    private RequestBody body;
    private UploadRequestListener listener;
    //    private BufferedSink bufferedSink;
    private boolean isClone2RealRequestBody = true;//第一次发生writeTo时，并没有实际发生上传操作，第二次writeTo才是实际上传

    public ProgressRequestBody(String filePath, RequestBody body, UploadRequestListener listener) {
        this.filePath = filePath;
        this.body = body;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return this.body.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return this.body.contentLength();
    }

    @Override
    public void writeTo(BufferedSink orginSink) throws IOException {
        BufferedSink newSink = Okio.buffer(createNewSink(orginSink));
        body.writeTo(newSink);
        newSink.flush();
        if (NetConfig.getConfig().getBaseConfig().isDebug()){
            Logger.d(TAG, "writeTo: " + isClone2RealRequestBody);
        }
        //第一次发生writeTo时，并没有实际发生上传操作，是把内容从第一个requestbody复制到了第二个requestbody，第二次writeTo才是实际上传
        if (isClone2RealRequestBody) {
            isClone2RealRequestBody = false;
        }
    }

    private Sink createNewSink(BufferedSink sink) {
        return new ForwardingSink(sink) {
            long bytesWritten = 0L;
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                if (NetConfig.getConfig().getBaseConfig().isDebug()){
                    Logger.d(TAG, "write: isClone2RealRequestBody:" + isClone2RealRequestBody);
                }
                if (listener != null && !isClone2RealRequestBody) {
                    if (bytesWritten >= contentLength) {
                        listener.onUploadDone(filePath);
                    } else {
                        float p = (float) bytesWritten / contentLength;
                        listener.onProgress(filePath, p);
                    }
                }
            }
        };
    }
}
