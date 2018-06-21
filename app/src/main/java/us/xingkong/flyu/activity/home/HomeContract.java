package us.xingkong.flyu.activity.home;

import us.xingkong.flyu.adapter.DynamicAdapter;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;
import us.xingkong.flyu.model.DownloadModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 22:25
 * @描述:
 * @更新日志:
 */
public interface HomeContract {

    interface Presenter extends BasePresenter {
        void loadHome();

        void displayHome();
    }

    interface View extends BaseView {
        String getUsername();

        void setRefresh(boolean refresh);

        void setAdapter(DynamicAdapter adapter);

        void setEnable(boolean enable);

        void toOtherActivity(DownloadModel.Message message);

        boolean isActive();
    }
}
