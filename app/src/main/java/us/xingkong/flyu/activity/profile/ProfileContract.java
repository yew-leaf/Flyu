package us.xingkong.flyu.activity.profile;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;
import us.xingkong.flyu.model.BmobUserModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/7 21:36
 * @描述:
 * @更新日志:
 */
public interface ProfileContract {

    interface Presenter extends BasePresenter {
        void updateAvatar(BmobUserModel bmobUserModel);

        void updateSignature(BmobUserModel bmobUserModel, String signature);

        void logout(UserModel userModel);
    }

    interface View extends BaseView {
        void displayProfile(BmobUserModel bmobUserModel);

        void showSignature(String sign);

        void setEnable(boolean enable);

        void toOtherActivity(UserModel userModel);

        void toOtherActivity(BmobUserModel bmobUserModel);

        boolean isActive();

        void showToast(String message);
    }
}
