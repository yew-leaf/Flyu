package us.xingkong.flyu.activity.main;

import android.content.Intent;

import java.io.File;
import java.util.List;

import us.xingkong.flyu.adapter.PhotosAdapter;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;
import us.xingkong.flyu.model.PhotoModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/29 20:04
 * @描述:
 * @更新日志:
 */
public interface MainContract {

    interface Presenter extends BasePresenter {
        void displayPhotos(List<PhotoModel> list);

        void uploadImageAndText(List<File> files);
    }

    interface View extends BaseView {
        String getUsername();

        String getContent();

        void setHeaderView();

        void setFooterView();

        void setAdapter(PhotosAdapter adapter);

        void setVisibility(int visibility);

        void setEnable(boolean enable);

        void showProgress(int progress);

        void toOtherActivity(Intent intent);

        void finishActivity();
    }
}
