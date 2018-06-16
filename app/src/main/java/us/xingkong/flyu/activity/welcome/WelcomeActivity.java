package us.xingkong.flyu.activity.welcome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import us.xingkong.flyu.model.DownloadModel;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.activity.container.ContainerActivity;
import us.xingkong.flyu.activity.login.LoginActivity;

public class WelcomeActivity extends BaseActivity<WelcomeContract.Presenter>
        implements WelcomeContract.View {

    @BindView(R.id.welcome)
    AppCompatImageView welcome;

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
                .asDrawable()
                .load(R.drawable.welcome)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(new RequestOptions().centerCrop())
                .into(welcome);

        new WelcomePresenter(this);
        mPresenter = getPresenter();
    }

    @Override
    protected void initData() {

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
        new AlertDialog.Builder(WelcomeActivity.this)
                .setTitle("初次见面！")
                .setMessage(R.string.about_text)
                .setCancelable(false)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void toOtherActivity(UserModel userModel, DownloadModel downloadModel) {
        final Intent intent = new Intent(WelcomeActivity.this, ContainerActivity.class);
        intent.putExtra("username",userModel.getUsername());
        //intent.putExtra("downloadModel",downloadModel);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
                overridePendingTransition(0, R.anim.activity_exit);
            }
        },2000);

    }
}
