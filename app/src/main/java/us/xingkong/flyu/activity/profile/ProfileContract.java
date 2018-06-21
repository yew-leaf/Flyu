package us.xingkong.flyu.activity.profile;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/7 21:36
 * @描述:
 * @更新日志:
 */
public interface ProfileContract {

    interface Presenter extends BasePresenter {


    }

    interface View extends BaseView {
        void displayProfile();

        void toOtherActivity(UserModel user);

        boolean isActive();
    }
}
