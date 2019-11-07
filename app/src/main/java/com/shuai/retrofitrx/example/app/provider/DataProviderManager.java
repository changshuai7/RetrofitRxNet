package com.shuai.retrofitrx.example.app.provider;


import com.shuai.retrofitrx.example.app.provider.common.model.CommonProvider;
import com.shuai.retrofitrx.example.app.provider.net.model.NetProvider;

/**
 * DataProvider管理类
 *
 * @author changshuai
 */

public class DataProviderManager {

    private IDataProvider mDataProvider;
    private static DataProviderManager mInstance;

    private static DataProviderManager getInstance() {
        if (mInstance == null) {
            synchronized (DataProviderManager.class) {
                if (mInstance == null) {
                    mInstance = new DataProviderManager();
                }
            }
        }
        return mInstance;
    }

    private DataProviderManager() {
        /**
         *
         * 此处具备更强的扩展性和可配置型。
         * I 接口用于表明信息内容，I 接口的实现类表明具体的实现过程。
         * 此处传入任意 I 接口的实现类，可调用此实现类的中的具体实现方法。
         * 例如：INetProvider的实现类为NetProvider。传入new NetProvider()可执行NetProvider中的实现方法。
         *
         *
         */
        mDataProvider = new DataProvider(new NetProvider(), new CommonProvider());
    }


    /**
     * 获取DataProvider
     *
     * @return
     */
    public static IDataProvider getProvider() {
        return DataProviderManager.getInstance().mDataProvider;
    }

}
