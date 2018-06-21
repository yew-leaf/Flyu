package us.xingkong.flyu.activity.dynamic;

import us.xingkong.flyu.adapter.DynamicAdapter;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;
import us.xingkong.flyu.model.DownloadModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 19:08
 * @描述:
 * @更新日志:
 */
public interface DynamicContract {

    interface Presenter extends BasePresenter {
        void loadDynamic();

        void displayDynamic();
    }

    interface View extends BaseView {
        String getUsername();

        void setDynamic(String size);

        void setRefresh(boolean refresh);

        void setAdapter(DynamicAdapter adapter);

        void setEnable(boolean enable);

        void toOtherActivity(DownloadModel.Message message);

        boolean isActive();
    }
}
