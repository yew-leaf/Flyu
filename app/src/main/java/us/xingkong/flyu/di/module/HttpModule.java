package us.xingkong.flyu.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import us.xingkong.flyu.util.RetrofitUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/19 22:56
 * @描述:
 * @更新日志:
 */
@Module
public class HttpModule {

    @Provides
    @Singleton
    RetrofitUtil provideRetrofiUtil() {
        return RetrofitUtil.getInstance();
    }
}
