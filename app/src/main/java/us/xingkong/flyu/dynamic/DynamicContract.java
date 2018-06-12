package us.xingkong.flyu.dynamic;

import us.xingkong.flyu.DownloadModel;
import us.xingkong.flyu.adapter.DynamicAdapter;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 19:08
 * @描述:
 * @更新日志:
 */
public interface DynamicContract {

    interface Presenter extends BasePresenter {
        void load();

        void display();
    }

    interface View extends BaseView<Presenter> {
        String getUsername();

        void setDynamic(String number);

        void setRefresh(boolean refresh);

        void setAdapter(DynamicAdapter adapter);

        void setEnable(boolean enable);

        void showMessage(String message);

        void toOtherActivity(DownloadModel.Message message);

        boolean isActive();
    }
}
