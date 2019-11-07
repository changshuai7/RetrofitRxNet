package com.shuai.retrofitrx.net.download;

import android.content.Context;
import android.text.TextUtils;

import com.shuai.retrofitrx.config.NetConfig;
import com.shuai.retrofitrx.net.client.DownloadClient;
import com.shuai.retrofitrx.net.download.progress.DownloadHandler;
import com.shuai.retrofitrx.net.download.progress.DownloadInfo;
import com.shuai.retrofitrx.net.download.progress.DownloadListener;
import com.shuai.retrofitrx.net.download.progress.DownloadManagerResponseListener;
import com.shuai.retrofitrx.utils.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;


/**
 * Created by liuqing on 17/10/12.
 */
public class DownloadManager implements IDownloadHandle {
    private static final String TAG = DownloadManager.class.getSimpleName();
    public static final int BUFFER_SIZE = 1024 * 8;

    /**
     * 下载类的统一回掉管理参数，会将下载的任务状态统一回掉在这儿，然后通过这儿分发到具体的任务回调
     */
    private DownloadManagerResponseListener downloadResponseListener = new DownloadManagerResponseListener() {
        @Override
        public void onStart(String url, DownloadHandler handler, long contentLength) {
            DownloadInfo info = listenerHashMap.get(url);
            if (info != null) {
                info.setHandler(handler);
                if (info.getListener() != null) {
                    info.getListener().onStart();
                }
                DownloadRecord record = downloadDB.getDownloadRecord(info.getFilePath(), info.getUrl());
                if (record != null) {
                    record.setDownloadStatus(DownloadRecord.STATUS_DOWNLOADING);
                    record.setFileContentLength(contentLength);
                    record.setUpdateTime(System.currentTimeMillis());
                    downloadDB.updateDownloadRecord(record);
                } else {
                    Logger.d(TAG, "onStart: =====不应该出现的假如任务了record＝＝null");
                }
                needCancelOrPause(url, info);//进行在连接过程中添加进来的取消任务执行，这时候的任务取消存在tempcancelUrls中
            } else {
                if (NetConfig.getConfig().getBaseConfig().isDebug()) {
                    Logger.e(TAG, "download onStart ,can't find task ,url :" + url);
                }
            }
        }

        @Override
        public void onProgress(String url, long totleLength, long downloadedLength) {
            DownloadInfo info = listenerHashMap.get(url);
            //这块避免做更新数据库的操作，因为频繁的开启关闭更新数据库会阻塞下载进程，造成下载整体缓慢，正确的做法应该是在下载完成/出错/暂停/取消的时候去更新数据库
            if (info != null && info.getListener() != null) {
                info.getListener().onProgress(totleLength, downloadedLength);
            } else {
                if (NetConfig.getConfig().getBaseConfig().isDebug()) {
                    Logger.e(TAG, "download progress ,can't find task ,url :" + url);
                }
            }
        }

        @Override
        public void onDownloadSuccess(String url, String path) {
            DownloadInfo info = listenerHashMap.remove(url);
            if (info != null && info.getListener() != null) {
                info.getListener().onSuccess(path);
                DownloadRecord record = downloadDB.getDownloadRecord(info.getFilePath(), info.getUrl());
                if (record != null) {
                    record.setDownloadStatus(DownloadRecord.STATUS_FINISH);
                    record.setFileDownloadedLength(new File(path).length());
                    record.setUpdateTime(System.currentTimeMillis());
                    int rowCount = downloadDB.updateDownloadRecord(record);
                    Logger.d(TAG, "onDownloadSuccess: 下载完成，更新记录完成：" + rowCount);
                } else {
                    Logger.d(TAG, "onDownloadSuccess: =====不应该出现的假如任务了record＝＝null");
                }
            } else {
                if (NetConfig.getConfig().getBaseConfig().isDebug()) {
                    Logger.e(TAG, "download success ,remove download task error ,url :" + url);
                }
            }
        }

        @Override
        public void onDownloadFail(String url, Throwable e) {
            DownloadInfo info = listenerHashMap.remove(url);
            if (info != null) {
                if (info.getListener() != null) {
                    if (needCancelOrPause(url, info)) {
                        info.getListener().onCanceled();
                        return;
                    }
                    DownloadRecord record = downloadDB.getDownloadRecord(info.getFilePath(), info.getUrl());
                    if (record != null) {
                        record.setDownloadStatus(DownloadRecord.STATUS_ERROR);
                        record.setFileDownloadedLength(0);
                        record.setFileContentLength(0);
                        record.setUpdateTime(System.currentTimeMillis());
                        int rowCount = downloadDB.updateDownloadRecord(record);
                        Logger.d(TAG, "onDownloadFail: 下载失败，更新记录完成：" + rowCount);
                    } else {
                        Logger.d(TAG, "onDownloadFail: =====不应该出现的假如任务了record＝＝null");
                    }
                    info.getListener().onFail(e);
                }
            } else {
                e.printStackTrace();
                if (NetConfig.getConfig().getBaseConfig().isDebug()) {
                    Logger.e(TAG, "download fail ,remove download task error ,url :" + url);
                }
            }
        }

        @Override
        public void onCancelled(String url) {
            DownloadInfo info = listenerHashMap.remove(url);
            if (info != null) {
                if (info.getListener() != null) {
                    info.getListener().onCanceled();
                }
                tempCancelUrls.remove(url);
                DownloadRecord record = downloadDB.getDownloadRecord(info.getFilePath(), info.getUrl());
                if (record != null) {
                    record.setDownloadStatus(DownloadRecord.STATUS_CANCEL);
                    record.setFileDownloadedLength(0);
                    record.setFileContentLength(0);
                    record.setUpdateTime(System.currentTimeMillis());
                    int rowCount = downloadDB.updateDownloadRecord(record);
                    Logger.d(TAG, "onCancelled: 取消下载，更新记录完成：" + rowCount);
                } else {
                    Logger.d(TAG, "onCancelled: =====不应该出现的假如任务了record＝＝null");
                }
            } else {
                if (NetConfig.getConfig().getBaseConfig().isDebug()) {
                    Logger.e(TAG, "download cancle fail ,remove download task error ,url :" + url);
                }
            }
        }

        @Override
        public void onPauseed(String url, long downloadedLength) {
            DownloadInfo info = listenerHashMap.remove(url);
            if (info != null) {
                if (info.getListener() != null) {
                    info.getListener().onPauseed();
                }
                tempCancelUrls.remove(url);
                DownloadRecord record = downloadDB.getDownloadRecord(info.getFilePath(), info.getUrl());
                if (record != null) {
                    record.setDownloadStatus(DownloadRecord.STATUS_PAUSE);
                    record.setFileDownloadedLength(downloadedLength);
                    record.setUpdateTime(System.currentTimeMillis());
                    int rowCount = downloadDB.updateDownloadRecord(record);
                    Logger.d(TAG, "onPauseed: 下载暂停，更新记录完成：" + rowCount);
                } else {
                    Logger.d(TAG, "onPauseed: =====不应该出现的假如任务了record＝＝null");
                }
            } else {
                if (NetConfig.getConfig().getBaseConfig().isDebug()) {
                    Logger.e(TAG, "download pause fail ,remove download task error ,url :" + url);
                }
            }
        }
    };


    /**
     * 存放下载过程中的下载信息以及对应的回掉对象
     */
    private HashMap<String, DownloadInfo> listenerHashMap = new HashMap<>();
    /**
     * 存放已经开始连接下载资源，没有真正进入到下载过程中的url，会在连接完成之后cancel掉任务
     */
    private List<String> tempCancelUrls = new ArrayList<>();
    /**
     * 存放已经开始连接下载资源，没有真正进入到下载过程中的url，会在连接完成之后cancel掉任务
     */
    private List<String> tempPauseUrls = new ArrayList<>();
    /**
     * 下载api
     */
    private DownloadApi downloadApi;
    /**
     * 单例对象
     */
    private static volatile IDownloadHandle instance;

    private DownloadDBDao downloadDB;

    private Context ctx;

    /**
     * 单例对象的构造方法，初始化下载专用的retrofit,并且加载对应的api
     * 这儿存在单独的downloadClient是担心过多下载任务占用app主要app请求网络的连接数
     */
    private DownloadManager(Context ctx) {
        this.downloadApi = createRetrofit().create(DownloadApi.class);
        this.ctx = ctx.getApplicationContext();
        this.downloadDB = new DownloadDBDao(this.ctx);
    }

    /**
     * 单例方法
     */
    public static IDownloadHandle getInstance(Context ctx) {
        if (null == instance) {
            synchronized (DownloadManager.class) {
                if (null == instance) {
                    instance = new DownloadManager(ctx);
                }
            }
        }
        return instance;
    }

    /**
     * 是否需要cancel，针对还没有开始连接上下载资源的任务，此时是无法终止连接，只有等待连接完成之后在start时去cancel掉任务，此时先把需要cancel的任务url缓存在 tempCancelUrls中
     * 调用在连接完成之后，检查是否需要直接cancel掉
     *
     * @param url  下载地址
     * @param info 下载信息
     */
    private boolean needCancelOrPause(String url, DownloadInfo info) {
        if (tempCancelUrls.remove(url)) {
            if (info.getHandler() != null) {
                info.getHandler().cancel();
            }
            if (info.getListener() != null) {
                info.getListener().onCanceled();
            }
            return true;
        } else if (tempPauseUrls.remove(url)) {
            if (info.getHandler() != null) {
                info.getHandler().puase();
            }
            if (info.getListener() != null) {
                info.getListener().onPauseed();
            }
            return true;
        } else {
            return false;
        }
    }

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
    @Override
    public boolean addDownloadTask(String url, String filePath, String tag, DownloadListener listener) {
        url = urlEncode(url);
        if (url == null){
            return false;
        }
        if (isDownloading(url)) {
            return false;
        }
        if (isExistRecordAndFile(url, filePath)) {
            listener.onSuccess(filePath);
            return true;
        }
        listenerHashMap.put(url, new DownloadInfo(url, filePath, listener));
        download(url, tag, filePath);
        return true;
    }

    /**
     * 判断时候有存在过的下载任务
     * 需要检测本地数据库状态以及检测文件是否存在，文件长度是否一致
     * */
    private boolean isExistRecordAndFile(String url, String filePath) {
        DownloadRecord record = downloadDB.getDownloadRecord(filePath, url);
        if (record != null && record.getFileDownloadedLength() == record.getFileContentLength() && record.getDownloadStatus() == DownloadRecord.STATUS_FINISH) {
            File downloadFile = new File(filePath);
            if (downloadFile.exists() && downloadFile.length() == record.getFileContentLength()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加下载任务
     *
     * @param url      下载任务的url
     * @param filePath 下载文件的缓存文件的路径，即是最终的文件存储路径
     * @param listener 下载过程回调 {@link DownloadListener}
     * @return true 为添加下载任务成功
     * false 为添加下载任务失败，失败现在只有一个原因，就是已经存在相同下载url的任务
     */
    @Override
    public boolean addDownloadTask(String url, String filePath, DownloadListener listener) {
        return addDownloadTask(url, filePath, null, listener);
    }


    /**
     * 根据url判断是否存在相同url的下载中的任务
     * 只有下载过程中或是准备下载中的url才是下载中状态
     *
     * @param url 下载Url
     * @return true 存在相同url的下载任务
     * false 不存在相同url的下载任务
     */
    @Override
    public boolean isDownloading(String url) {
        url = urlEncode(url);
        if (url == null) {
            return false;
        }
        return listenerHashMap.containsKey(url) || tempCancelUrls.contains(url) || tempPauseUrls.contains(url);
    }

    /**
     * 根据Url取消下载任务
     * 取消时，先判断是否存在这样的任务，如果正在下载中直接中断下载，可能下载任务还有没有连接完成会临时添加到tempCancelUrls中，待连接上时再中断下载
     *
     * @param url 下载url
     * @return true 取消下载成功
     * false 取消下载失败，下载任务没有这个Url的对应任务
     */
    @Override
    public boolean cancelTask(String url) {
        url = urlEncode(url);
        if (url == null){
            return false;
        }
        if (listenerHashMap.containsKey(url)) {
            DownloadHandler handler = listenerHashMap.get(url).getHandler();
            if (handler == null) {
                tempCancelUrls.add(url);
            } else {
                handler.cancel();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 暂停url的下载任务
     * @return boolean 是否成功暂停
     * */
    @Override
    public boolean pauseTask(String url) {
        url = urlEncode(url);
        if (url == null){
            return false;
        }
        if (listenerHashMap.containsKey(url)) {
            DownloadHandler handler = listenerHashMap.get(url).getHandler();
            if (handler == null) {
                tempPauseUrls.add(url);
            } else {
                handler.puase();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setMaxRequestCount(int maxRequestCount, int maxRequestCountForPerHost) {
        DownloadClient.getInstance(downloadResponseListener).dispatcher().setMaxRequests(maxRequestCount);
        DownloadClient.getInstance(downloadResponseListener).dispatcher().setMaxRequestsPerHost(maxRequestCountForPerHost);
    }


    /**
     * 开始进行retrofit下载
     *
     * @param url      下载url
     * @param filePath 缓存的文件夹路径
     */
    private void download(final String url, final String tag, final String filePath) {
        final long startPos = checkRecord(url, tag, filePath);
        String startPamars = "bytes=" + startPos + "-";
        DownloadRecord record = downloadDB.getDownloadRecord(filePath,url);
        if (record!=null){
            record.setDownloadStatus(DownloadRecord.STATUS_CONNECTING);
            record.setUpdateTime(System.currentTimeMillis());
            downloadDB.updateDownloadRecord(record);
        }
        downloadApi
                .download(url, startPamars)
                .flatMap(new Function<ResponseBody, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(@NonNull final ResponseBody responseBody) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                                saveFile(responseBody, startPos, filePath, url, e);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Boolean isFullFile) {
                        if (!TextUtils.isEmpty(filePath)) {
                            if (isFullFile) {
                                Logger.d(TAG, "onNext: isFullFile:" + isFullFile);
                                downloadResponseListener.onDownloadSuccess(url, filePath);
                            } else {
                                Logger.d(TAG, "onNext: isFullFile:" + isFullFile);
                            }
                        } else {
                            downloadResponseListener.onDownloadFail(url, new RuntimeException("can't create cacheFile"));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        downloadResponseListener.onDownloadFail(url, e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 检查是否有相同的任务存在，
     * 判断依据是url以及filepath,
     * 并且在存在任务的情况下需要检查是否文件存在且文件长度，
     * 且进度以文件为准
     * @return long 返回断点下载的起始位置（新下载的就是0）
     * */
    private long checkRecord(String url, String tag, String filePath) {
        DownloadRecord record = downloadDB.getDownloadRecord(filePath, url);
        File downloadFile = new File(filePath);
        Logger.d(TAG, "checkRecord: filelength:" + downloadFile.length());
        long startPos = 0L;
        if (record != null && downloadFile.exists()) {
            long fileLength = downloadFile.length();
            if (record.getFileDownloadedLength() == fileLength) {
                startPos = fileLength;
            } else {
                startPos = fileLength;
                record.setFileDownloadedLength(fileLength);
                record.setDownloadStatus(DownloadRecord.STATUS_WAITING);
                record.setUpdateTime(System.currentTimeMillis());
                record.setTag(tag);
                downloadDB.updateDownloadRecord(record);
            }
        } else {
            if (downloadFile.exists()) {
                downloadFile.delete();
            }
            if (record != null) {
                int delCount = downloadDB.delRecordByID(record.getId());
                Logger.d(TAG, "checkRecord: delCount:" + delCount);
            }
            DownloadRecord newRecord = new DownloadRecord();
            newRecord.setLocalFilePath(filePath);
            newRecord.setDownloadUrl(url);
            newRecord.setTag(tag);
            newRecord.setDownloadStatus(DownloadRecord.STATUS_WAITING);
            newRecord.setUpdateTime(System.currentTimeMillis());
            long id = downloadDB.insertDownloadRecord(newRecord);
        }
        return startPos;
    }


    /**
     * 下载完成之后会讲对应的输入流缓存到文件中，整个下载流程才算结束
     *
     * @param body     下载response，文件流也在这个body中
     * @param startPos 断点下载开始点
     * @param filePath 下载文件的缓存文件路径
     * @param url      下载文件的下载url
     * @param e        Observalbe的控制器，控制进行到下一步或者抛出错误
     */
    private void saveFile(ResponseBody body, long startPos, String filePath, String url, ObservableEmitter<Boolean> e) {
        DownloadRecord record = downloadDB.getDownloadRecord(filePath, url);
        File downloadFile = new File(filePath);
        if (record != null) {
            RandomAccessFile randomAccessFile = null;
            InputStream inputStream = null;
            BufferedInputStream in = null;
            try {
                randomAccessFile = new RandomAccessFile(downloadFile, "rwd");
                randomAccessFile.seek(startPos);
                inputStream = body.byteStream();
                byte[] buffer = new byte[BUFFER_SIZE];
                in = new BufferedInputStream(inputStream, BUFFER_SIZE);
                int len;
                long downloadLength = startPos;
                while ((len = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    randomAccessFile.write(buffer, 0, len);
                    downloadLength += len;
                }
                record.setFileDownloadedLength(downloadLength);
                record.setUpdateTime(System.currentTimeMillis());
                int rowCount = downloadDB.updateDownloadRecord(record);
                Logger.d(TAG, "saveFile: update rowCount:" + rowCount);
                if (downloadFile.length() == (body.contentLength() + startPos)) {
                    //走到这儿，下载写入数据完成，写入文件长度和流长度一致，下载成功
                    if (NetConfig.getConfig().getBaseConfig().isDebug()) {
                        Logger.d(TAG, "saveFile end,download success,path:" + downloadFile.getAbsolutePath());
                    }
                    e.onNext(true);
                } else {
                    //如果走到这儿，一般是因为取消导致下载中断，依然需要存储文件，为之后断点下载做准备
                    if (NetConfig.getConfig().getBaseConfig().isDebug()) {
                        Logger.d(TAG, "saveFile end,download fail ,filelength != contentlength");
                    }
                    e.onNext(false);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                e.onError(exception);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } else {
            throw new RuntimeException("happen some unbeliveable error,no download record found....");
        }
    }

    /**
     * url 编码
     * @param url
     * @return
     */
    private String urlEncode(String url) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            return null;
        }
        return httpUrl.toString();
    }

    /**
     * 创建接口封装的retrofit类
     * 直接使用DownloadClient.newInst
     *
     * ance获取对应的下载okhttpclient
     */
    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .client(DownloadClient.getInstance(downloadResponseListener))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetConfig.getConfig().getRequestConfigProvider().getBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    /**
     * 下载接口配置
     */
    public interface DownloadApi {
        @GET
        Observable<ResponseBody> download(@Url String downloadUrl, @Header("RANGE") String rangeString);
    }

}
