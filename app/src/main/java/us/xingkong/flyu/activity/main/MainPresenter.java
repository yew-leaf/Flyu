package us.xingkong.flyu.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;

import us.xingkong.flyu.activity.browse.BrowseActivity;
import us.xingkong.flyu.adapter.PhotosAdapter;
import us.xingkong.flyu.model.EventModel;
import us.xingkong.flyu.model.PhotoModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/29 20:09
 * @描述:
 * @更新日志:
 */
public class MainPresenter implements MainContract.Presenter,
        MainModel.OnRequestListener {

    private MainContract.View mView;
    private MainModel model;
    private Activity activity;
    private PhotosAdapter mAdapter;
    private NumberFormat format = NumberFormat.getNumberInstance();

    MainPresenter(MainContract.View view) {
        mView = view;
        mView.setPresenter(this);
        model = new MainModel();
        model.setOnRequestListener(this);
        activity = mView.getActivity();
        format.setMaximumFractionDigits(2);
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
                    PhotoModel model = list.get(position);
                    model.setPosition(position);
                    Intent intent = new Intent(activity, BrowseActivity.class);
                    intent.putExtra("Photos", (Serializable) list);
                    intent.putExtra("PhotoModel", model);
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
        EventBus.getDefault().post(new EventModel<>("MainActivity", "Refresh"));
        mView.finishActivity();
    }

    @Override
    public void progress(long currentBytes, long targetBytes) {
        String progress = format.format((float) (currentBytes / targetBytes) * 100);
        Log.i("Progress", progress);
        mView.showProgress(progress);
    }

    @Override
    public void failure(String result) {
        mView.setEnable(true);
        mView.showMessage("上传失败");
    }
}