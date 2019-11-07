package com.shuai.retrofitrx.example.app.provider.common.model;


import com.shuai.retrofitrx.example.app.provider.common.ICommonProvider;

/**
 * 公共信息的ICommonProvider实现类
 *
 * @author changshuai
 */

public class CommonProvider implements ICommonProvider {

    /**
     * 测试数据
     *
     * @return
     */
    @Override
    public String providerTest() {
        return "HELLO WORLD!";
    }
}
