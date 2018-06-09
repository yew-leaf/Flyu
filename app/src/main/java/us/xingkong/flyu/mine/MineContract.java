package us.xingkong.flyu.mine;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.adapter.DynamicAdapter;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 19:08
 * @描述:
 * @更新日志:
 */
public interface MineContract {

    interface Presenter extends BasePresenter {
        void load();

        void display();
    }

    interface View extends BaseView<Presenter> {
        String getUsername();

        void setRefresh(boolean refresh);

        void showMessage(String message);

        void setAdapter(DynamicAdapter adapter);

        void toOtherActivity(UserModel user);

        boolean isActive();
    }
}
