package us.xingkong.flyu.activity.dynamic;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import us.xingkong.flyu.model.DownloadModel;
import us.xingkong.flyu.model.EventModel;
import us.xingkong.flyu.R;
import us.xingkong.flyu.adapter.DynamicAdapter;
import us.xingkong.flyu.base.BaseFragment;
import us.xingkong.flyu.activity.container.ContainerActivity;
import us.xingkong.flyu.activity.detail.DetailActivity;
import us.xingkong.flyu.util.SnackbarUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 19:11
 * @描述:
 * @更新日志:
 */
public class DynamicFragment extends BaseFragment<DynamicContract.Presenter>
        implements DynamicContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private DynamicContract.Presenter mPresenter;

    public static DynamicFragment newInstance() {
        return new DynamicFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_dynamic;
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

        new DynamicPresenter(this);
        mPresenter = getPresenter();
        mPresenter.load();
    }

    @Override
    protected void initData() {

    }

    @Subscribe
    public void onMainActivityFinished(EventModel<String> eventModel) {
        if (!eventModel.getPublisher().equals("MainActivity")) {
            return;
        }
        mPresenter.load();
    }

    @Override
    protected void initListener() {
        swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    public String getUsername() {
        return ContainerActivity.getUserModel().getUsername();
    }

    @Override
    public void setDynamic(String number) {
        EventBus.getDefault().post(new EventModel<>("DynamicFragment", number));
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
        swipeRefresh.setEnabled(enable);
        recyclerView.setEnabled(enable);
    }

    @Override
    public void showMessage(String message) {
        SnackbarUtil.shortSnackbar(mActivity.findViewById(R.id.root), message).show();
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
    public void onRefresh() {
        mPresenter.load();
    }
}
