package us.xingkong.flyu.activity.welcome;

import us.xingkong.flyu.model.DownloadModel;
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
        void load();
    }

    interface View extends BaseView<Presenter> {
        UserModel getUser();

        void showMessage(String message);

        void toOtherActivity(UserModel userModel, DownloadModel downloadModel);
    }
}
