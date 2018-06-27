package us.xingkong.flyu.activity.home;

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
import us.xingkong.flyu.activity.dynamic.DynamicModel;
import us.xingkong.flyu.adapter.DynamicAdapterX;
import us.xingkong.flyu.base.BasePresenterImpl;
import us.xingkong.flyu.model.DownloadModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/9 22:55
 * @描述:
 * @更新日志:
 */
public class HomePresenterX extends BasePresenterImpl<HomeContractX.View>
        implements HomeContractX.Presenter {

    private DynamicModel dynamicModel;
    private DynamicAdapterX mAdapter;
    private Reference<List<DownloadModel.Message>> mHomeReference;
    private int current;
    private List<DownloadModel.Message> totalList;
    private List<DownloadModel.Message> currentList;

    HomePresenterX(@NonNull HomeContractX.View view) {
        super(view);
        dynamicModel = new DynamicModel();
        totalList = new ArrayList<>();
        currentList = new ArrayList<>();
    }

    @Override
    public void loadHome() {
        if (!mView.isActive()) {
            return;
        }

        dynamicModel.loadDynamic(mView.getUsername()).subscribe(new Observer<DownloadModel>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(DownloadModel downloadModel) {
                totalList = downloadModel.getMessage();
                currentList.clear();
                mView.setEnable(true);
                mAdapter = setAdapter(totalList);
                displayHome();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.setEnable(false);
                if (e instanceof HttpException || e instanceof MalformedJsonException) {
                    mAdapter = new DynamicAdapterX(mView.getContext(), new ArrayList<DownloadModel.Message>());
                } else if (e instanceof UnknownHostException) {
                    if (checkReference(mHomeReference)) {
                        mAdapter = new DynamicAdapterX(mView.getContext(), mHomeReference.get());
                    } else {
                        mAdapter = new DynamicAdapterX(mView.getContext(), null);
                    }
                    mView.showMessage("网络连接不可用");//snackBar action
                }
                if (e instanceof SocketTimeoutException) {
                    mView.showMessage("网络连接超时");
                }
                displayHome();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void refresh() {
        current = 0;
        loadHome();
    }

    @Override
    public void loadMore() {
        current += 10;
        for (int i = current; i < current + getLoadNumber(); i++) {
            currentList.add(totalList.get(i));
        }
        mAdapter.notifyDataSetChanged();
        mView.loadMoreComplete();
    }

    @Override
    public void displayHome() {
        mAdapter.notifyDataSetChanged();
        mView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new DynamicAdapterX.onItemClickListener() {
            @Override
            public void onItemClick(DownloadModel.Message message) {
                mView.toOtherActivity(message);
            }

            @Override
            public void onReloadClick() {
                loadHome();
            }
        });
        mView.refreshComplete();
    }

    @Override
    public void subscribe() {
        super.subscribe();
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
        if (checkReference(mHomeReference)) {
            mHomeReference.clear();
        }
    }

    private DynamicAdapterX setAdapter(List<DownloadModel.Message> list) {
        Collections.reverse(list);
        for (int i = current; i < current + getLoadNumber(); i++) {
            currentList.add(list.get(i));
        }
        mHomeReference = new SoftReference<>(list);
        DynamicAdapterX adapter = new DynamicAdapterX(mView.getActivity(), currentList);
        adapter.setName(mView.getUsername());
        return adapter;
    }

    private int getLoadNumber() {
        return ((current + 10) <= totalList.size()) ? 10 : totalList.size() - current;
    }
}
