package us.xingkong.flyu.activity.splash;

import android.annotation.TargetApi;
import android.support.annotation.Nullable;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;
import us.xingkong.flyu.model.BmobUserModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/5 20:55
 * @描述:
 * @更新日志:
 */
public interface SplashContract {

    interface Presenter extends BasePresenter {
        void loadUser();
    }

    interface View extends BaseView {
        boolean getUserState();

        UserModel getUser();

        BmobUserModel getBmobUser();

        @TargetApi(23)
        void applyPermissions();

        void toOtherActivity(@Nullable UserModel userModel);

        void toOtherActivity();
    }
}
