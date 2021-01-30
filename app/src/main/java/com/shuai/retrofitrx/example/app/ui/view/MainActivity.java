package com.shuai.retrofitrx.example.app.ui.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shuai.csnet.example.app.R;
import com.shuai.retrofitrx.example.app.net.core.AppDataCallback;
import com.shuai.retrofitrx.example.app.net.NetProvider;
import com.shuai.retrofitrx.example.app.ui.bean.CheckRomBean;


public class MainActivity extends AppCompatActivity {

    private Button btn;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn_request_net);
        tv = findViewById(R.id.tv_result);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGet();
            }
        });

    }

    public void requestGet() {
        NetProvider.getInstance().requestCheckRom("salesorder", false, "v2.6.2.1102-debug",
                new AppDataCallback<CheckRomBean>() {

                    @Override
                    public void onSuccess(final CheckRomBean data) {
                        tv.setText("请求结果：" + data.toString());
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg, Throwable throwable) {
                        tv.setText("请求错误：" + "code = " + errorCode + ",errorMsg = " + errorMsg);

                    }

                    @Override
                    public void onFinally() {

                    }
                }.setDisposable(null));
    }


}
