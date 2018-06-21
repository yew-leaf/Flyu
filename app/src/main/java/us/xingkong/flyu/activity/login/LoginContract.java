package us.xingkong.flyu.activity.login;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 20:25
 * @描述:
 * @更新日志:
 */
public interface LoginContract {

    interface Presenter extends BasePresenter {
        void login();
    }

    interface View extends BaseView {
        String getUserName();

        String getPassword();

        void setVisibility(int visibility);

        void setEnable(boolean enable);

        void toOtherActivity(UserModel user);

        void showToast(String message);
    }
}
