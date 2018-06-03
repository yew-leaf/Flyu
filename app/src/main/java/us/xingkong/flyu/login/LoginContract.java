package us.xingkong.flyu.login;

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

        void setCheckbox();
    }

    interface View extends BaseView<Presenter> {

    }
}
