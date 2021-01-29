package com.shuai.retrofitrx.net

import com.shuai.retrofitrx.config.NetConfig
import com.shuai.retrofitrx.net.retrofit.AuthRetrofitFactory
import com.shuai.retrofitrx.net.retrofit.IRetrofitCreator
import java.io.Serializable
import java.util.concurrent.ConcurrentHashMap

/**
 * 获取网络请求的server实例
 *
 */
class ApiFactory {

    companion object {

        private val authRetrofitFactory = AuthRetrofitFactory(NetConfig.app)

        @JvmStatic
        fun <T> getApiService(clazz: Class<T>): T? {
            return getApiService(authRetrofitFactory, clazz)
        }

        private val factoryClsContainer: MutableMap<Int, FactoryContainer> = ConcurrentHashMap()

        /**
         * 获取Retrofit的Api对象
         *
         * @param iRetrofitCreator Retrofit工厂(通过自定义IRetrofitCreator,可以外部传入NetRequestConfigProvider，灵活性会极其高)
         * @param clazz            Api的class对象
         * @param <T>              Api的实例
         * @return
        </T> */
        @Synchronized
        @JvmStatic
        fun <T> getApiService(iRetrofitCreator: IRetrofitCreator, clazz: Class<T>): T? {
            try {
                if (!factoryClsContainer.containsKey(iRetrofitCreator.hashCode())) {
                    val apis: MutableMap<Class<*>, Any?> = ConcurrentHashMap()
                    apis[clazz] = iRetrofitCreator.create()?.create(clazz)
                    val factoryContainer = FactoryContainer(iRetrofitCreator, apis)

                    factoryClsContainer[iRetrofitCreator.hashCode()] = factoryContainer
                } else {
                    val factoryContainer = factoryClsContainer[iRetrofitCreator.hashCode()]
                    if (!factoryContainer!!.apis.containsKey(clazz)) {
                        factoryContainer.apis[clazz] = factoryContainer.factoryIns.create()?.create(clazz)
                    }
                }
                return factoryClsContainer[iRetrofitCreator.hashCode()]!!.apis[clazz] as? T?
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        internal class FactoryContainer(var factoryIns: IRetrofitCreator, var apis: MutableMap<Class<*>, Any?>) : Serializable
    }


}