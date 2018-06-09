package us.xingkong.flyu.mine;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.adapter.DynamicAdapter;
import us.xingkong.flyu.base.BaseFragment;
import us.xingkong.flyu.container.ContainerActivity;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 19:11
 * @描述:
 * @更新日志:
 */
public class MineFragment extends BaseFragment<MineContract.Presenter>
        implements MineContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private UserModel userModel;
    private MineContract.Presenter mPresenter;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_mine;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View root) {
        LinearLayoutManager manager =
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        new MinePresenter(this);
        mPresenter = getPresenter();
    }

    @Override
    protected void initData() {
        userModel = ContainerActivity.getUserModel();
        mPresenter.load();
        mPresenter.display();
    }

    @Override
    protected void initListener() {
        swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    public String getUsername() {
        return userModel.getUsername();
    }

    @Override
    public void setRefresh(boolean refresh) {
        swipeRefresh.setRefreshing(refresh);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void setAdapter(DynamicAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void toOtherActivity(UserModel user) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onRefresh() {
        mPresenter.load();
        mPresenter.display();
    }
}
