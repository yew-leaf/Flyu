package us.xingkong.flyu.login;

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

    interface View extends BaseView<Presenter> {
        String getUserName();

        String getPassword();

        void setVisibility(int visibility);

        void setEnable(boolean enable);

        void showMessage(String message);

        void toOtherActivity(UserModel user);
    }
}
