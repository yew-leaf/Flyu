package us.xingkong.flyu.welcome;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import us.xingkong.flyu.DownloadModel;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.container.ContainerActivity;
import us.xingkong.flyu.login.LoginActivity;

public class WelcomeActivity extends BaseActivity<WelcomeContract.Presenter>
        implements WelcomeContract.View {

    @BindView(R.id.welcome)
    AppCompatImageView welcome;

    private Handler mHandler;
    private WelcomeContract.Presenter mPresenter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        Glide.with(this)
                .load(R.drawable.welcome)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(new RequestOptions().centerCrop())
                .into(welcome);

        new WelcomePresenter(this);
        mPresenter = getPresenter();
    }

    @Override
    protected void initData() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void initListener() {

    }

    @Override
    public UserModel getUser() {
        UserModelDao dao = App.getInstance().getDaoSession().getUserModelDao();
        List<UserModel> list = dao.loadAll();
        for (UserModel user : list) {
            if (user.getIsLogged()) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void toOtherActivity(UserModel userModel, DownloadModel downloadModel) {
        Intent intent;
        if (downloadModel == null) {
            intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        } else {
            intent = new Intent(WelcomeActivity.this, ContainerActivity.class);

        }
        startActivity(intent);
        finish();
        overridePendingTransition(0, R.anim.activity_exit);
    }
}
