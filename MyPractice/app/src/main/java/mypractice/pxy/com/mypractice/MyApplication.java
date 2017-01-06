package mypractice.pxy.com.mypractice;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/1/6.
 */

public class MyApplication  extends Application {

    public static Context app;

    @Override
    public void onCreate() {
        super.onCreate();
        app=getApplicationContext();
    }
}

