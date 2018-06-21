package us.xingkong.flyu.di.component;

import android.app.Activity;

import dagger.Component;
import us.xingkong.flyu.di.module.ActivityModule;
import us.xingkong.flyu.di.scope.ActivityScope;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/19 21:11
 * @描述:
 * @更新日志:
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    //void inject(LoginActivity loginActivity);
    Activity getActivity();
}
