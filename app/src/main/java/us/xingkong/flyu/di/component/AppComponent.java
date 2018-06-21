package us.xingkong.flyu.di.component;

import javax.inject.Singleton;

import dagger.Component;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.di.module.AppModule;
import us.xingkong.flyu.di.module.HttpModule;
import us.xingkong.flyu.util.RetrofitUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/19 22:46
 * @描述:
 * @更新日志:
 */

@Singleton
@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent {
    App getApplication();

    RetrofitUtil getRetrofitUtil();
}
