package us.xingkong.flyu.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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

    private static App mInstance;
    private static Context appContext;
    public static List<Activity> activities;

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
        super.onCreate();
        //Bmob.initialize(this, Constants.APPID);

        mInstance = this;
        appContext = getApplicationContext();

        activities = new LinkedList<>();

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
                ).build();
        mOkUtil = new OkUtil(okHttpClient);
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

    public static synchronized AppComponent getAppComponent() {
        if (mInstance == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(mInstance))
                    .httpModule(new HttpModule()).build();
        }
        return mAppComponent;
    }
}
