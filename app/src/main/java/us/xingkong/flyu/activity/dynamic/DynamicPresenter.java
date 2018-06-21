package us.xingkong.flyu.activity.dynamic;

import android.support.annotation.NonNull;

import com.google.gson.stream.MalformedJsonException;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.adapter.rxjava2.HttpException;
import us.xingkong.flyu.adapter.DynamicAdapter;
import us.xingkong.flyu.base.BasePresenterImpl;
import us.xingkong.flyu.model.DownloadModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/9 1:15
 * @描述:
 * @更新日志:
 */
public class DynamicPresenter extends BasePresenterImpl<DynamicContract.View>
        implements DynamicContract.Presenter {

    private DynamicModel dynamicModel;
    private DynamicAdapter mAdapter;
    private Reference<List<DownloadModel.Message>> mDynamicReference;

    DynamicPresenter(@NonNull DynamicContract.View view) {
        super(view);
        dynamicModel = new DynamicModel();
    }

    @Override
    public void loadDynamic() {
        if (!mView.isActive()) {
            return;
        }
        mView.setEnable(false);
        mView.setRefresh(true);
        dynamicModel.loadDynamic(mView.getUsername()).subscribe(new Observer<DownloadModel>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(DownloadModel downloadModel) {
                mAdapter = setAdapter(downloadModel.getMessage());
                displayDynamic();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof HttpException || e instanceof MalformedJsonException) {
                    mAdapter = new DynamicAdapter(mView.getContext(), new ArrayList<DownloadModel.Message>());
                } else if (e instanceof UnknownHostException) {
                    if (checkReference(mDynamicReference)) {
                        mAdapter = new DynamicAdapter(mView.getContext(), mDynamicReference.get());
                        mAdapter.setName(mView.getUsername());
                    } else {
                        mAdapter = new DynamicAdapter(mView.getContext(), null);
                    }
                    mView.showMessage("网络连接不可用");//snackBar action
                } else if (e instanceof SocketTimeoutException) {
                    mView.showMessage("网络连接超时");
                }
                displayDynamic();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void displayDynamic() {
        mAdapter.notifyDataSetChanged();
        mView.setAdapter(mAdapter);
        mView.setEnable(true);
        mView.setRefresh(false);
        mAdapter.setItemClickListener(new DynamicAdapter.onItemClickListener() {
            @Override
            public void onItemClick(DownloadModel.Message message) {
                mView.toOtherActivity(message);
            }

            @Override
            public void onReloadClick() {
                loadDynamic();
            }
        });

    }

    @Override
    public void subscribe() {
        super.subscribe();
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
        if (checkReference(mDynamicReference)) {
            mDynamicReference.clear();
        }
    }

    private DynamicAdapter setAdapter(List<DownloadModel.Message> list) {
        Collections.reverse(list);
        mDynamicReference = new SoftReference<>(list);
        DynamicAdapter adapter = new DynamicAdapter(mView.getActivity(), list);
        adapter.setName(mView.getUsername());
        mView.setDynamic(String.valueOf(list.size()));
        return adapter;
    }
}
