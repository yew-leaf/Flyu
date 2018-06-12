package us.xingkong.flyu.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import us.xingkong.flyu.DownloadModel;
import us.xingkong.flyu.EventModel;
import us.xingkong.flyu.R;
import us.xingkong.flyu.adapter.DynamicAdapter;
import us.xingkong.flyu.base.BaseFragment;
import us.xingkong.flyu.detail.DetailActivity;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 22:28
 * @描述:
 * @更新日志:
 */
public class HomeFragment extends BaseFragment<HomeContract.Presenter>
        implements HomeContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String query;
    private HomeContract.Presenter mPresenter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public static HomeFragment newInstance(String query) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_home;
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

        new HomePresenter(this);
        mPresenter = getPresenter();
    }

    @Override
    protected void initData() {

    }

    @Subscribe
    public void getDynamic(EventModel<String> model) {
        if (!model.getPublisher().equals("Home")) {
            return;
        }
        query = model.getSubscriber();
        mPresenter.load();
    }

    @Override
    protected void initListener() {
        swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    public String getUsername() {
        return query;
    }

    @Override
    public void setRefresh(boolean refresh) {
        swipeRefresh.setRefreshing(refresh);
    }

    @Override
    public void setAdapter(DynamicAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setEnable(boolean enable) {
        recyclerView.setEnabled(enable);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void toOtherActivity(DownloadModel.Message message) {
        Intent intent = new Intent(mActivity, DetailActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onRefresh() {
        mPresenter.load();
    }
}
