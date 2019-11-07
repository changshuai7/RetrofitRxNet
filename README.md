# RetrofitRxNet:网络请求库
## 写在前边

本框架集成Retrofit+RxJava整合框架，可以简单便捷地进行网络请求。



## 一、框架结构

略


## 二、集成方式

### 1.引入。

```
dependencies {
        implementation 'com.shuai:retrofit-rx-net:0.0.1'
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

                /**
                 * FileProvider定义
                 */
                @Override
                public String getFileProviderAuthority() {
                    return MyConstants.Authority.FILE_AUTHORITY;
                }
            })
            .requestConfig(new NetRequestConfigProvider() {

                /**
                 * 配置默认Auth：公共请求header。（可选）
                 */
                @Override
                public Map getHeaderMap() {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(MyConstants.HeaderKey.HEADER_1, "header1");
                    map.put(MyConstants.HeaderKey.HEADER_2, "header2");

                    return map;
                }

                /**
                 * 配置默认Auth：公共get/post请求的url拼接参数。（可选）
                 */
                @Override
                public Map getParamsMap() {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(MyConstants.ParamsKey.PARAMS_1, "tom");
                    map.put(MyConstants.ParamsKey.PARAMS_2, "jake");

                    return map;
                }

                 /**
                 * 配置默认Auth：公共post请求的通用参数。（可选）
                 */
                @Override
                public Map getBodyMap() {
                    //code...
                    return null;
                }

                /**
                 * 配置通用Auth默认baseUrl的domain
                 */
                @Override
                public String getBaseUrl() {
                    // 建议baseUrl以斜杠结尾，避免Retrofit报错
                    return BuildConfig.DEBUG ? "http://192.168.11.11:1234/" : "http://www.realese.com";
                }

                /**
                 * 可传入多个BaseUrl，通过制定Header中Domain-Host字段来区分。（可选）
                 * @return
                 */
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
public interface TestServiceApi {

    @Headers({"Domain-Host:" + "url1"})// 加上 Domain-Host header
    @GET(sales/v1/test/)
    Observable<BaseResponse<CheckRomBean>> testRequestGet(@Query("app_source") String app_source, @Query("isCustomizeRom") boolean isCustomizeRom, @Query("versionname") String versionname);

}
```

**说明：**

这里如果想动态切换baseUrl，请在Headers中添加请求头，用于标明baseUrl类型key(header字段为：Domain-Host)，此类型key在初始化getBaseUrls中已经申明为map的key值。

比如这里申明了：@Headers({"Domain-Host:" + "url1"})，那么baseUrl会替换为getBaseUrls()传入的map中，key="url1"所对应的value

**注意：**

对于字段地址（这里的sales/v1/test）请务必采用绝对地址（即不以斜杠开头）。否则会影响到框架对于baseUrl的替换。

同时在实际开发中，我们也强烈建议采用绝对地址，避免不必要的路径错误。

#### 3.2执行网络请求。

##### 1、简易请求方式：
```
ApiFactory.getApiService(TestServiceApi.class)
                .testRequestGet(app_source, isCustomizeRom, versionname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<CheckRomBean>(dataCallback));
```
简易请求方式采用了默认的配置。需要注意的是，此方法内部创建Retrofit工厂时，传入的gson实例采用了项目在初始化中getGsonInstance()获取到的实例（如果初始化时没有初始化getGsonInstance()，则采用空实例(new Gson()）,Observer需根据业务需求自行创建。

##### 2、扩展请求方式：
```
AuthRetrofitFactory factory = new AuthRetrofitFactory(new Gson(),MyApplication.getInstance());
TestServiceApi testServiceApi = factory.create().create(TestServiceApi.class);

testServiceApi
            .testRequestGet(app_source, isCustomizeRom, versionname)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MyObserver<CheckRomBean>(dataCallback));
```

此方式通过自定义创建Retrofit工厂（AuthRetrofitFactory）来请求网络，更灵活（其实ApiFactory内部也是维护了一个Retrofit工厂）。
同时，new Gson()的传入是可选的，如果此处传入，则优先采用此传入的gson实例；如果此处没有传入gson实例，则使用初始化中getGsonInstance()获取到的；如果都没有，则采用空实例(new Gson()）

同时Retrofit工厂也可以传入baseUrl来创建ApiInterface实例(直接覆盖初始化中的getBaseUrl())，如下：：

```
TestServiceApi testServiceApi = factory.create("http://123.445.666.777:8980/").create(TestServiceApi.class);
```

无论上述那种方式请求网络，都可以实现baseUrl的动态替换、公参加入等完整功能。



## 4、开发计划和进度

开发进度：

1、整体框架大致完成（OK）

2、get/post请求开发完成（OK）

未完成：

1、下载上传（ing...）

思考：

1、是否使用公共参数/请求头可通过请求头动态配置

2、新参数无法覆盖默认配置的参数，由于系统原因导致的问题。思考解决方案。

3、网络请求生命周期的控制和收尾

4、外部增加对OkHttpClient的扩展

5、OkHttpClient可以使用一个单例的思考

## 5、写在后面
针对有些业务，网络请求地址可能出现极其繁多复杂，难于管理的情况，baseUrl动态替换的方案可能略显逊色。我们建议你可以通过全路径传入url地址来请求网络。

对于管理全路径的Url，可以参考我的另一个项目：[ServerAddressHelper](https://github.com/changshuai7/ServerAddressHelper)，该项实现了灵活管理全路径服务器的地址，目前已经在若干大型应用中实践，具有良好稳定性。