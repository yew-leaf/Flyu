package us.xingkong.flyu.mine;

import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.adapter.DynamicAdapter;
import us.xingkong.flyu.base.BaseActivity;

public class MineActivity extends BaseActivity<MineContract.Presenter>
        implements MineContract.View {


    @Override
    protected int bindLayout() {
        return R.layout.activity_mine;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void setRefresh(boolean refresh) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void setAdapter(DynamicAdapter adapter) {

    }

    @Override
    public void toOtherActivity(UserModel user) {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
