package us.xingkong.flyu.app;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import us.xingkong.flyu.DaoMaster;
import us.xingkong.flyu.DaoSession;
import us.xingkong.oktuil.OkUtil;


/**
 * @作者: Xuer
 * @创建时间: 2018/5/22 20:35
 * @描述:
 * @更新日志:
 */
public class App extends android.app.Application {

    private static Context appContext;
    private static App mInstance;
    public static List<Activity> activities = new LinkedList<>();
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase database;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private OkUtil mOkUtil;

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Bmob.initialize(this, Constants.APPID);
        appContext = getApplicationContext();
        mInstance = this;

        mHelper = new DaoMaster.DevOpenHelper(this, "Users", null);
        database = mHelper.getReadableDatabase();
        mDaoMaster = new DaoMaster(database);
        mDaoSession = mDaoMaster.newSession();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
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
}
