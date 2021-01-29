package com.shuai.retrofitrx.net;

import com.shuai.retrofitrx.config.NetConfig;
import com.shuai.retrofitrx.net.retrofit.AuthRetrofitFactory;
import com.shuai.retrofitrx.net.retrofit.IRetrofitCreator;
import com.shuai.retrofitrx.utils.Logger;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 获取网络请求的server实例
 *
 * @author changshuai
 */

public class ApiFactory {

    private static final AuthRetrofitFactory authRetrofitFactory = new AuthRetrofitFactory(NetConfig.getApp());

    public static <T> T getApiService(Class<T> clazz) {
        return getApiService(authRetrofitFactory, clazz);
    }


    private static final Map<Integer, FactoryContainer> factoryClsContainer = new ConcurrentHashMap<>();

    /**
     * 获取Retrofit的Api对象
     *
     * @param iRetrofitCreator Retrofit工厂(通过自定义IRetrofitCreator,可以外部传入NetRequestConfigProvider，灵活性会极其高)
     * @param clazz            Api的class对象
     * @param <T>              Api的实例
     * @return
     */
    public synchronized static <T> T getApiService(IRetrofitCreator iRetrofitCreator, Class<T> clazz) {

        try {
            if (iRetrofitCreator != null) {

                if (!factoryClsContainer.containsKey(iRetrofitCreator.hashCode())) {

                    FactoryContainer factoryContainer = new FactoryContainer();

                    Map<Class<?>, Object> apis = new ConcurrentHashMap<>();
                    apis.put(clazz, iRetrofitCreator.create().create(clazz));

                    factoryContainer.setFactoryIns(iRetrofitCreator);
                    factoryContainer.setApis(apis);

                    factoryClsContainer.put(iRetrofitCreator.hashCode(), factoryContainer);
                } else {

                    FactoryContainer factoryContainer = factoryClsContainer.get(iRetrofitCreator.hashCode());
                    if (!factoryContainer.getApis().containsKey(clazz)) {

                        factoryContainer.getApis().put(clazz, factoryContainer.getFactoryIns().create().create(clazz));
                    }

                }
                return (T) factoryClsContainer.get(iRetrofitCreator.hashCode()).getApis().get(clazz);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    static class FactoryContainer implements Serializable {
        private IRetrofitCreator factoryIns;
        private Map<Class<?>, Object> apis;

        public FactoryContainer() {
        }

        public FactoryContainer(IRetrofitCreator factoryIns, Map<Class<?>, Object> apis) {
            this.factoryIns = factoryIns;
            this.apis = apis;
        }

        public IRetrofitCreator getFactoryIns() {
            return factoryIns;
        }

        public void setFactoryIns(IRetrofitCreator factoryIns) {
            this.factoryIns = factoryIns;
        }

        public Map<Class<?>, Object> getApis() {
            return apis;
        }

        public void setApis(Map<Class<?>, Object> apis) {
            this.apis = apis;
        }
    }


}