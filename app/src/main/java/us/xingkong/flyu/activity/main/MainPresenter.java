package us.xingkong.flyu.activity.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import us.xingkong.flyu.activity.browse.BrowseActivity;
import us.xingkong.flyu.adapter.PhotosAdapter;
import us.xingkong.flyu.base.BasePresenterImpl;
import us.xingkong.flyu.model.EventModel;
import us.xingkong.flyu.model.PhotoModel;
import us.xingkong.flyu.model.UploadModel;
import us.xingkong.flyu.util.L;
import us.xingkong.oktuil.response.GsonResponse;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/29 20:09
 * @描述:
 * @更新日志:
 */
public class MainPresenter extends BasePresenterImpl<MainContract.View>
        implements MainContract.Presenter {

    private MainModel mainModel;
    private PhotosAdapter mAdapter;

    MainPresenter(@NonNull MainContract.View view) {
        super(view);
        mainModel = new MainModel();
    }

    @Override
    public void displayPhotos(final List<PhotoModel> list) {
        if (list.size() >= 3) {
            mView.showMessage("最多支持三张照片哦");
        }

        mAdapter = new PhotosAdapter(mView.getContext(), list);
        mView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new PhotosAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position < list.size()) {
                    PhotoModel photoModel = list.get(position);
                    photoModel.setPosition(position);
                    Intent intent = new Intent(mView.getActivity(), BrowseActivity.class);
                    intent.putExtra("Photos", (Serializable) list);
                    intent.putExtra("PhotoModel", photoModel);
                    mView.toOtherActivity(intent);
                }
            }
        });
    }

    @Override
    public void uploadImageAndText(List<File> files) {
        mView.setEnable(false);
        mView.setVisibility(View.VISIBLE);

        //我太菜了不会用retrofit监听上传进度

        /*mainModel.uploadImageAndText(mView.getUsername(), mView.getContent(), files).subscribe(new Observer<UploadModel>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(UploadModel uploadModel) {
                mView.setEnable(true);
                mView.showMessage("上传成功");
                EventBus.getDefault().post(new EventModel<>("MainActivity", "Refresh"));
                mView.finishActivity();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e("Upload Error", e.getMessage());
                if (e instanceof SocketTimeoutException) {
                    mView.showMessage("上传超时");
                } else {
                    mView.showMessage("上传失败");
                }
                mView.setEnable(true);
            }

            @Override
            public void onComplete() {

            }
        });*/

        mainModel.upload(mView.getUsername(), mView.getContent(), files)
                .tag(mView).enqueue(new GsonResponse<UploadModel>() {
            @Override
            public void onFailure(int statusCode, String errorMsg) {
                L.e("MainPresenter", errorMsg);
                mView.setEnable(true);
                mView.setVisibility(View.INVISIBLE);
                mView.showMessage("上传失败");
            }

            @Override
            public void onProgress(long currentBytes, long targetBytes) {
                super.onProgress(currentBytes, targetBytes);
                int progress = (int) ((float) currentBytes * 100 / (float) targetBytes);
                mView.showProgress(progress);
            }

            @Override
            public void onSuccess(int statusCode, String result, UploadModel response) {
                if (!result.equals("error")) {
                    mView.setEnable(true);
                    mView.showMessage("上传成功");
                    EventBus.getDefault().post(new EventModel<>("MainActivity", "Refresh"));
                    mView.finishActivity();
                } else {
                    L.e("MainPresenter", "Upload Error:" + result);
                }
            }
        });
    }

    @Override
    public void subscribe() {
        super.subscribe();
        mView.setFooterView();
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
    }
}