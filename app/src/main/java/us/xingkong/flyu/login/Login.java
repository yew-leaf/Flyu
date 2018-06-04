package us.xingkong.flyu.login;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.main.MainActivity;
import us.xingkong.flyu.register.Register;
import us.xingkong.flyu.util.UIUtil;

public class Login extends BaseActivity<LoginContract.Presenter>
        implements LoginContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.username)
    AppCompatEditText username;
    @BindView(R.id.password)
    AppCompatEditText password;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.submit)
    AppCompatButton submit;
    @BindView(R.id.register)
    AppCompatButton register;

    private long exitTime;
    private LoginContract.Presenter mPresenter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        toolbar.setTitle("登录");

        new LoginPresenter(this);
        mPresenter = getPresenter();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.login();
                UIUtil.closeKeyboard(Login.this);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public String getUserName() {
        return username.getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }

    @Override
    public void setVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void setEnable(boolean enable) {
        submit.setEnabled(enable);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(submit, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void toOtherActivity(UserModel user) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Snackbar.make(submit, "再按一次退出", Snackbar.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                App.exit();
                //System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
