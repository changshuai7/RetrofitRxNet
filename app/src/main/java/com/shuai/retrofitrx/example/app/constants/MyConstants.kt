package com.shuai.retrofitrx.example.app.constants;

/**
 * 常量配置
 */
public class MyConstants {

    public static class CommonKey {
        public final static String STATUS = "status";
        public final static String ERR_MSG = "err_msg";
        public static final String USER_ID = "user_id";
        public static final String APP_SOURCE = "app_source";
        public static final String NEW_HOST_NAME = "new_host_name";
    }

    public static class ParamsKey {
        public final static String PARAMS_1 = "params_1";
        public final static String PARAMS_2 = "params_2";
    }

    public static class HeaderKey {
        public final static String HEADER_1 = "my-header1";
        public final static String HEADER_2 = "my-header2";
    }


    public static class ServerDomainKey {
        public final static String URL1 = "url1";
        public final static String URL2 = "url2";
    }

    public interface Authority {
        String FILE_AUTHORITY = "com.example.app.fileProvider";
    }

}
