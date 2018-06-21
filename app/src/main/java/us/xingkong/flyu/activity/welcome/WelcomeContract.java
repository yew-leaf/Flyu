package us.xingkong.flyu.activity.welcome;

import android.annotation.TargetApi;
import android.support.annotation.Nullable;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/5 20:55
 * @描述:
 * @更新日志:
 */
public interface WelcomeContract {

    interface Presenter extends BasePresenter {
        void loadUser();
    }

    interface View extends BaseView {
        boolean getUserState();

        UserModel getUser();

        @TargetApi(23)
        void applyPermissions();

        void toOtherActivity(@Nullable UserModel userModel);
    }
}
