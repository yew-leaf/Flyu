package us.xingkong.flyu.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import us.xingkong.flyu.app.App;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/19 22:47
 * @描述:
 * @更新日志:
 */
@Module
public class AppModule {
    private final App mApplication;

    public AppModule(App application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    App provideApplication() {
        return mApplication;
    }
    
}
