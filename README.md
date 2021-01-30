# RetrofitRxNet:网络请求库
## 写在前边

本框架集成Retrofit+RxJava整合框架，可以简单便捷地进行网络请求。



## 一、框架结构

略


## 二、集成方式

### 1.引入。

```
dependencies {
        implementation 'com.shuai:retrofit-rx-net:x.x.x'
}

版本号 一般采用Tag中最新的版本。
```


### 2.初始化。
请在Application中初始化如下代码：
```
 NetConfig.init(this)
         .baseConfig(new NetBaseConfigProvider() {

                /**
                 * 配置库的debug状态
                 * 影响debug调试域名，请求日志输出，以及可能影响普通的log日志输出
                 */
                @Override
                public boolean isDebug() {
                    return BuildConfig.DEBUG;
                }

                /**
                 * 配置库的log输出状态
                 * 如果没有配置，随 {@link #isDebug()}状态走
                 */
                @Override
                public boolean isLogDebug() {
                    return super.isLogDebug();
                }

            })
            .requestConfig(new NetRequestConfigProvider() {

                /**
                 * 配置默认Auth：公共请求header。（可选）
                 */
                @NotNull
                @Override
                public Map<String, String> getHeaderMap() {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(MyConstants.HeaderKey.HEADER_1, "header1");
                    map.put(MyConstants.HeaderKey.HEADER_2, "header2");

                    return map;
                }

                /**
                 * 配置默认Auth：公共get/post请求的url拼接参数。（可选）
                 */
                @NotNull
                @Override
                public Map<String, String> getParamsMap() {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(MyConstants.ParamsKey.PARAMS_1, "tom");
                    map.put(MyConstants.ParamsKey.PARAMS_2, "jake");

                    return map;
                }

                 /**
                 * 配置默认Auth：公共post请求的通用参数。（可选）
                 */
                @NotNull
                @Override
                public Map<String, String> getBodyMap() {
                    //code...
                    return super.getBodyMap();
                }

                /**
                 * 配置通用Auth默认baseUrl的domain
                 */
                @NotNull
                @Override
                public String getBaseUrl() {
                    // 建议baseUrl以斜杠结尾，避免Retrofit报错
                    return BuildConfig.DEBUG ? "http://192.168.11.11:1234/" : "http://www.realese.com";
                }

                /**
                 * 可传入多个BaseUrl，通过制定Header中Domain-Host字段来区分。（可选）
                 * @return
                 */
                @NotNull
                @Override
                public Map<String, String> getBaseUrls() {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("url1", BuildConfig.DEBUG ? "http://111.222.333.444:8888": "http://www.a.com");
                    map.put("url2", BuildConfig.DEBUG ? "http://555.666.777.888:6666": "http://www.b.com");

                    return map;
                }

                /**
                 * 传入Gson实例。（可选）
                 * @return
                 */
                @Override
                public Gson getGsonInstance() {
                    return null;
                }

            });
```

**说明：**

1、必须传入：getBaseUrl()中传入默认的baseUrl，当然你可以更灵活地根据debug/release等环境传入不同的地址(需根据业务需求来判断)

2、可选传入：如果存在多个baseUrl,那么请在getBaseUrls()传入一个Map<String,String>。map的key为地址的标识，value为实际地址，key标识将用于区分地址类型。下文会会提到使用方案。当然你可以更灵活地根据debug/release等环境传入不同的地址(需根据业务需求来判断)

**注意：**

因为Retrofit对baseUrl有规定：

1、对于scheme://host[:port]类型的baseUrl可以不以/（斜线）结尾，

2、对于scheme://host[:port]/path类型的baseUrl必须以/（斜线） 结尾。否则Retrofit会抛出异常。

所以：为避免不必要的异常，建议BaseUrl以/（斜线） 结尾。


### 3.网络请求

#### 3.1创建InterfaceApi。
```
public interface JavaTestInterface {

    @Headers({"Domain-Host:" + "url1"})
    @GET("sales/v1/test/")
    Observable<BaseResponse<TestBean>> testRequestGet(
            @Query("app_source") String appSource,
            @Query("isCustomizeRom") boolean isCustomizeRom,
            @Query("versionname") String versionName);

}
```

**说明：**

这里如果想动态切换baseUrl，请在Headers中添加请求头，用于标明baseUrl类型key(header字段为：Domain-Host)，此类型key在初始化getBaseUrls中已经申明为map的key值。

比如这里申明了：@Headers({"Domain-Host:" + "url1"})，那么baseUrl会替换为getBaseUrls()传入的map中，key="url1"所对应的value

**注意：**

对于字段地址（这里的"sales/v1/test"）请务必采用绝对地址（即不以斜杠开头）。否则会影响到框架对于baseUrl的替换。

同时在实际开发中，我们也强烈建议采用绝对地址，避免不必要的路径错误。

#### 3.2执行网络请求。

##### 1、简易请求方式：
```
ApiFactory.getApiService(JavaTestInterface.class)
                .testRequestGet(app_source, isCustomizeRom, versionname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppObserver<TestBean>(dataCallback));
```
简易请求方式采用了默认的配置。需要注意的是，此方法内部创建Retrofit工厂时，传入的gson实例采用了项目在初始化中getGsonInstance()获取到的实例（如果初始化时没有初始化getGsonInstance()，则采用空实例(new Gson()）,Observer需根据业务需求自行创建。

##### 2、扩展请求方式：
```
public static AuthRetrofitFactory myAuthRetrofitFactory
            = new AuthRetrofitFactory(App.getInstance(), new JavaNetRequestConfig());

ApiFactory.getApiService(myAuthRetrofitFactory,TestServiceApi.class)
                .testRequestGet(app_source, isCustomizeRom, versionname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppObserver<TestBean>(dataCallback));
```

此方式通过自定义创建Retrofit工厂（AuthRetrofitFactory）来请求网络，更灵活。再此基础上，可以自定义某个网络请求的NetRequestConfigProvider。此处NetRequestConfigProvider的配置会优先于Application中的配置。
