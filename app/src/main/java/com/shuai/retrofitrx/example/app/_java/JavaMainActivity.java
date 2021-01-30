package com.shuai.retrofitrx.example.app._java;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shuai.retrofitrx.example.app.App;
import com.shuai.retrofitrx.example.app.R;
import com.shuai.retrofitrx.example.app.net.AppDataCallback;
import com.shuai.retrofitrx.example.app.net.AppObserver;
import com.shuai.retrofitrx.example.app.bean.TestBean;
import com.shuai.retrofitrx.net.ApiFactory;
import com.shuai.retrofitrx.net.retrofit.AuthRetrofitFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class JavaMainActivity extends AppCompatActivity {

    private Button btn;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn_request_net);
        tv = findViewById(R.id.tv_result);

        btn.setOnClickListener(v -> requestGet(true, new AppDataCallback<TestBean>() {
            @Override
            public void onSuccess(@Nullable TestBean data) {
                if (data != null && !TextUtils.isEmpty(data.getResult())) {
                    tv.setText(data.getResult());
                }
            }

            @Override
            public void onError(int errorCode, @Nullable String errorMsg, @NotNull Throwable throwable) {

            }

            @Override
            public void onFinally() {

            }
        }.setDisposable(null)));

    }

    public void requestGet(boolean useConfig, AppDataCallback<TestBean> dataCallback) {

        AuthRetrofitFactory myAuthRetrofitFactory = new AuthRetrofitFactory(App.getInstance(), new JavaNetRequestConfig());
        if (useConfig) {
            ApiFactory.getApiService(myAuthRetrofitFactory, JavaTestInterface.class)
                    .testRequestGet("salesorder", false, "v2.6.2.1102-debug")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new AppObserver<TestBean>(dataCallback));
        } else {
            ApiFactory.getApiService(JavaTestInterface.class)
                    .testRequestGet("salesorder", false, "v2.6.2.1102-debug")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new AppObserver<TestBean>(dataCallback));
        }


    }
}