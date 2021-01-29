package com.shuai.retrofitrx.utils;


import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;


/**
 * 提取出一些框架中可能用到的工具类
 */
public class Util {

    /**
     * 集合是否为空
     *
     * @param collection
     * @return
     */
    public static boolean CollectionIsEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Map是否为空
     *
     * @param map
     * @return
     */
    public static boolean MapIsEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * 检查URL是否合法
     *
     * @param url
     * @return
     */
    public static boolean checkUrl(String url) {
        if (url == null) {
            Logger.e("URL检查出异常：", "url为空");
            return false;
        }

        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            Logger.e("URL检查出异常：", url + "->为非法URL");
            return false;
        }
        return true;
    }

    /**
     * 去除String类型List中的空
     * @param list
     * @return
     */
    public static List<String> removeStrListEmpty(List<String> list){
        ArrayList<String> result = new ArrayList();
        for (String str :list){
            if (!isStrNullOrEmpty(str)){
                result.add(str);
            }
        }
        return result;
    }

    /**
     * 判断CharSequence是否为空
     *
     * @param string
     * @return
     */
    public static boolean isNullOrEmpty(@Nullable CharSequence string) {
        return string == null || string.length() == 0;
    }

    /**
     * 判断String是否为空（内部trim校验）
     *
     * @param string
     * @return
     */
    public static boolean isStrNullOrEmpty(@Nullable String string) {
        return string == null || string.trim().length() == 0;
    }


}
