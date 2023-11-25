package com.hfpq.commonhttpserial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.azhon.appupdate.manager.DownloadManager;
import com.giftedcat.serialportlibrary.SerialPortManager;
import com.lzy.okgo.OkGo;
import com.permissionx.guolindev.PermissionX;
import com.sxjs.common.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}