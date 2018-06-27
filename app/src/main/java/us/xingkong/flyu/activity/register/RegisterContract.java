package us.xingkong.flyu.activity.register;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;
import us.xingkong.flyu.model.BmobUserModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 20:46
 * @描述:
 * @更新日志:
 */
public interface RegisterContract {

    interface Presenter extends BasePresenter {
        void register();
    }

    interface View extends BaseView {
        String getUserName();

        String getEmail();

        String getPassword();

        String getRepassword();

        void setVisibility(int visibility);

        void setEnable(boolean enable);

        void showMessage(String message);

        void toOtherActivity(UserModel userModel);

        void toOtherActivity(BmobUserModel bmobUserModel);

        void showToast(String message);
    }
}
