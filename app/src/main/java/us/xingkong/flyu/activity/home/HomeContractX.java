package us.xingkong.flyu.activity.home;

import us.xingkong.flyu.adapter.DynamicAdapterX;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;
import us.xingkong.flyu.model.DownloadModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 22:25
 * @描述:
 * @更新日志:
 */
public interface HomeContractX {

    interface Presenter extends BasePresenter {
        void loadHome();

        void refresh();

        void loadMore();

        void displayHome();
    }

    interface View extends BaseView {
        String getUsername();

        void setAdapter(DynamicAdapterX adapter);

        void setEnable(boolean enable);

        void refreshComplete();

        void loadMoreComplete();

        void toOtherActivity(DownloadModel.Message message);

        boolean isActive();
    }
}
