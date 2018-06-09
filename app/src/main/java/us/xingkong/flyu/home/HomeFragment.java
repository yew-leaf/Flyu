package us.xingkong.flyu.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BaseFragment;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 22:28
 * @描述:
 * @更新日志:
 */
public class HomeFragment extends BaseFragment<HomeContract.Presenter>
        implements HomeContract.View {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static HomeFragment newInstance() {
        return new HomeFragment();
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

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void toOtherActivity(UserModel user) {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
