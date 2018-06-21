package us.xingkong.flyu.di.module;

import dagger.Module;
import dagger.Provides;
import us.xingkong.flyu.activity.login.LoginModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/19 20:35
 * @描述:
 * @更新日志:
 */
@Module
public class ModelModule {

    @Provides
    public LoginModel provideLoginModel() {
        return new LoginModel();
    }
}
