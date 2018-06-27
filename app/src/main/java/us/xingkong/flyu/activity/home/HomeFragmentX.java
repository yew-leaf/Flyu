package us.xingkong.flyu.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.activity.detail.DetailActivity;
import us.xingkong.flyu.adapter.DynamicAdapterX;
import us.xingkong.flyu.base.BaseFragment;
import us.xingkong.flyu.model.DownloadModel;
import us.xingkong.flyu.model.EventModel;
import us.xingkong.flyu.util.S;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 22:28
 * @描述:
 * @更新日志:
 */
public class HomeFragmentX extends BaseFragment<HomeContractX.Presenter>
        implements HomeContractX.View {

    @BindView(R.id.recyclerView)
    XRecyclerView recyclerView;

    private String query;

    public static HomeFragmentX newInstance() {
        return new HomeFragmentX();
    }

    public static HomeFragmentX newInstance(String query) {
        HomeFragmentX fragment = new HomeFragmentX();
        Bundle bundle = new Bundle();
        bundle.putString("Query", query);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected HomeContractX.Presenter newPresenter() {
        return new HomePresenterX(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_dynamic_x;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View root) {
        EventBus.getDefault().register(this);

        LinearLayoutManager manager =
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recyclerView.setLimitNumberToCallLoadMore(0);
        //recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //recyclerView.setArrowImageView(R.mipmap.ic_arrow_refresh);

        mPresenter.loadHome();
    }

    @Override
    protected void initData() {

    }

    @Subscribe
    public void getDynamic(EventModel<String> eventModel) {
        if (!eventModel.getPublisher().equals("QueryUser")) {
            return;
        }
        query = eventModel.getSubscriber();
        mPresenter.loadHome();
    }

    @Override
    protected void initListener() {
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.refresh();
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
            }
        });
    }

    @Override
    public String getUsername() {
        return query;
    }

    @Override
    public void setAdapter(DynamicAdapterX adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setEnable(boolean enable) {
        recyclerView.setEnabled(enable);
        recyclerView.setLoadingMoreEnabled(enable);
    }

    @Override
    public void refreshComplete() {
        recyclerView.refreshComplete();
    }

    @Override
    public void loadMoreComplete() {
        recyclerView.loadMoreComplete();
    }

    @Override
    public void toOtherActivity(DownloadModel.Message message) {
        Intent intent = new Intent(mActivity, DetailActivity.class);
        intent.putExtra("Message", message);
        startActivity(intent);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMessage(String message) {
        S.shortSnackbar(mActivity.findViewById(R.id.root), message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (recyclerView != null) {
            recyclerView.destroy();
            recyclerView = null;
        }
    }
}
