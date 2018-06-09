package us.xingkong.flyu.home;

import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BaseActivity;

public class HomeActivity extends BaseActivity<HomeContract.Presenter>
        implements HomeContract.View {


    @Override
    protected int bindLayout() {
        return R.layout.activity_home;
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
