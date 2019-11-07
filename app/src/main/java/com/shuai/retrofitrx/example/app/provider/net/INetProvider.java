package com.shuai.retrofitrx.example.app.provider.net;


import com.shuai.retrofitrx.example.app.provider.net.core.HttpInterface;
import com.shuai.retrofitrx.example.app.ui.bean.CheckRomBean;

/**
 * 网络请求的ProviderInterface
 *
 * @author changshuai
 */

public interface INetProvider {

    /**
     * 测试网络请求。
     *
     * @param dataCallback
     */
    void requestCheckRom(String app_source, boolean isCustomizeRom, String versionname, HttpInterface.DataCallback<CheckRomBean> dataCallback);


}
