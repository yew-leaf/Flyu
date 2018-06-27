package us.xingkong.flyu.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.bmob.v3.Bmob;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import us.xingkong.flyu.BuildConfig;
import us.xingkong.flyu.DaoMaster;
import us.xingkong.flyu.DaoSession;
import us.xingkong.flyu.di.component.AppComponent;
import us.xingkong.flyu.di.component.DaggerAppComponent;
import us.xingkong.flyu.di.module.AppModule;
import us.xingkong.flyu.di.module.HttpModule;
import us.xingkong.flyu.util.L;
import us.xingkong.oktuil.OkUtil;


/**
 * @作者: Xuer
 * @创建时间: 2018/5/22 20:35
 * @描述:
 * @更新日志:
 */
public class App extends Application {

    List<Class<? extends View>> problemViewClassList;
    private static App mInstance;
    private static Context appContext;
    public static List<Activity> activityList;

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase database;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private OkUtil mOkUtil;

    private static AppComponent mAppComponent;

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        //严苛模式
        if (BuildConfig.isDebug) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }

        super.onCreate();

        Bmob.initialize(this, Constants.ApplicationID);

        //初始化滑动返回
        problemViewClassList = new ArrayList<>();
        BGASwipeBackHelper.init(this, problemViewClassList);

        mInstance = this;
        appContext = getApplicationContext();

        activityList = new LinkedList<>();

        mHelper = new DaoMaster.DevOpenHelper(this, "Users", null);
        database = mHelper.getReadableDatabase();
        mDaoMaster = new DaoMaster(database);
        mDaoSession = mDaoMaster.newSession();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HttpLoggingInterceptor(
                        new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                L.i("OkUtil", message);
                            }
                        })
                        .setLevel(HttpLoggingInterceptor.Level.BASIC)
                )
                .build();
        mOkUtil = new OkUtil(okHttpClient);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activityList.add(activity);
                L.i("onActivityCreated", "add");
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activityList.remove(activity);
                L.i("onActivityDestroyed", "remove");
            }
        });
    }

    public static Context getAppContext() {
        return appContext;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public OkUtil getOkUtil() {
        return mOkUtil;
    }

    public static void exit() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing())
                activity.finish();
        }
        activityList.clear();
    }

    public static synchronized AppComponent getAppComponent() {
        if (mInstance == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(mInstance))
                    .httpModule(new HttpModule()).build();
        }
        return mAppComponent;
    }
}
