package us.xingkong.flyu.main;

import android.content.Intent;

import java.util.List;

import us.xingkong.flyu.PhotoBean;
import us.xingkong.flyu.adapter.PhotosAdapter;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/29 20:04
 * @描述:
 * @更新日志:
 */
public interface MainContract {

    interface Presenter extends BasePresenter {
        void display(List<PhotoBean> list);
    }

    interface View extends BaseView<Presenter> {
        void setHeaderView();

        void setFooterView();

        void setAdapter(PhotosAdapter adapter);

        void toOtherActivity(Intent intent);
    }
}
