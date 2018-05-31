package us.xingkong.flyu.app;

import android.app.Activity;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * @作者: Xuer
 * @包名: us.xingkong.flyu.app
 * @类名: application
 * @创建时间: 2018/5/22 20:35
 * @最后修改于:
 * @版本: 1.0
 * @描述:
 * @更新日志:
 */
public class App extends android.app.Application {

    private static Context appContext;
    public static List<Activity> activities = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, Constants.appID);
        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void exit() {
        for (Activity activity : activities) {
            if (!activity.isFinishing())
                activity.finish();
        }
        activities.clear();
    }
}
