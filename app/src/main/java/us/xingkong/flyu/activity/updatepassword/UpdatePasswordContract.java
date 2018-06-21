package us.xingkong.flyu.activity.updatepassword;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/20 19:09
 * @描述:
 * @更新日志:
 */
public interface UpdatePasswordContract {

    interface Presenter extends BasePresenter {
        void updatePassword();
    }

    interface View extends BaseView {
        String getUserName();

        String getPassword();

        String getRepassword();

        void setVisibility(int visibility);

        void setEnable(boolean enable);

        void toOtherActivity(UserModel user);

        void showToast(String message);
    }
}
