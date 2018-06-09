package us.xingkong.flyu.home;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 22:25
 * @描述:
 * @更新日志:
 */
public interface HomeContract {

    interface Presenter extends BasePresenter {
        void display();

    }

    interface View extends BaseView<Presenter> {
        void showMessage(String message);

        void toOtherActivity(UserModel user);

        boolean isActive();
    }
}
