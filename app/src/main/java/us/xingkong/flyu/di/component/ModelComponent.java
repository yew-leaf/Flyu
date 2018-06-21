package us.xingkong.flyu.di.component;

import dagger.Component;
import us.xingkong.flyu.activity.login.LoginPresenter;
import us.xingkong.flyu.di.module.ModelModule;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/19 20:39
 * @描述:
 * @更新日志:
 */
@Component(modules = {ModelModule.class})
public interface ModelComponent {
    void inject(LoginPresenter loginPresenter);
}
