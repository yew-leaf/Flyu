package us.xingkong.flyu.di.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import us.xingkong.flyu.di.scope.ActivityScope;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/19 20:25
 * @描述:
 * @更新日志:
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }
}
