package com.wenhuaijun.cartoonautosplit.app;

import android.app.Application;

import com.wenhuaijun.cartoonautosplit.utils.JUtils;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class APP extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        JUtils.initialize(this);
        JUtils.setDebug(true,"heheda");
    }
}
