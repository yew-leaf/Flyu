package us.xingkong.flyu.main;

import android.app.Activity;
import android.content.Intent;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import us.xingkong.flyu.PhotoModel;
import us.xingkong.flyu.adapter.PhotosAdapter;
import us.xingkong.flyu.base.OnRequestListener;
import us.xingkong.flyu.browse.BrowseActivity;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/29 20:09
 * @描述:
 * @更新日志:
 */
public class MainPresenter implements MainContract.Presenter,
        OnRequestListener<String> {

    private MainContract.View mView;
    private MainModel model;
    private Activity activity;
    private PhotosAdapter mAdapter;

    MainPresenter(MainContract.View view) {
        mView = view;
        mView.setPresenter(this);
        model = new MainModel();
        model.setOnRequestListener(this);
        activity = mView.getActivity();
    }

    @Override
    public void display(final List<PhotoModel> list) {
        if (list.size() >= 3) {
            mView.showMessage("最多支持三张照片哦");
        }
        mAdapter = new PhotosAdapter(mView.getContext(), list);
        mView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new PhotosAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position < list.size()) {
                    PhotoModel bean = list.get(position);
                    bean.setPosition(position);
                    Intent intent = new Intent(activity, BrowseActivity.class);
                    intent.putExtra("photo", (Serializable) list);
                    intent.putExtra("bean", bean);
                    mView.toOtherActivity(intent);
                }
            }
        });
    }

    @Override
    public void upload(List<File> files) {
        mView.setEnable(false);
        model.uploadImageAndText(mView.getUsername(), mView.getContent(), files);
    }

    @Override
    public void start() {
        mView.setFooterView();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void success(String result) {
        mView.setEnable(true);
        mView.showMessage("上传成功");
        mView.finishActivity();
    }

    @Override
    public void failure(String result) {
        mView.setEnable(true);
        mView.showMessage("上传失败");
    }
}
