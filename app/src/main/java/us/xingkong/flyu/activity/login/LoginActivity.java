package us.xingkong.flyu.activity.login;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.OnClick;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.activity.container.ContainerActivity;
import us.xingkong.flyu.activity.register.RegisterActivity;
import us.xingkong.flyu.activity.updatepassword.UpdatePasswordActivity;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.util.S;
import us.xingkong.flyu.util.T;
import us.xingkong.flyu.util.UIUtil;

public class LoginActivity extends BaseActivity<LoginContract.Presenter>
        implements LoginContract.View {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.username)
    AppCompatEditText username;
    @BindView(R.id.password)
    AppCompatEditText password;
    @BindView(R.id.updatePassword)
    AppCompatTextView updatePassword;
    @BindView(R.id.login)
    AppCompatButton login;
    @BindView(R.id.register)
    AppCompatButton register;

    private long exitTime;

    @Override
    protected LoginContract.Presenter newPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_login;
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

    @OnClick({R.id.updatePassword, R.id.login, R.id.register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updatePassword:
                startActivity(new Intent(LoginActivity.this, UpdatePasswordActivity.class));
                overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
                break;
            case R.id.login:
                UIUtil.closeKeyboard(LoginActivity.this);
                mPresenter.login();
                break;
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
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
        login.setEnabled(enable);
        register.setEnabled(enable);
    }

    @Override
    public void toOtherActivity(UserModel userModel) {
        /*Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
        intent.putExtra("Username", userModel.getUsername());
        startActivity(intent);
        finish();*/
    }

    @Override
    public void toOtherActivity() {
        startActivity(new Intent(LoginActivity.this, ContainerActivity.class));
        finish();
    }

    @Override
    public void showToast(String message) {
        T.shortToast(LoginActivity.this, message);
    }

    @Override
    public void showMessage(String message) {
        S.shortSnackbar(findViewById(R.id.root), message);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                S.shortSnackbar(findViewById(R.id.root), getString(R.string.double_click_to_exit));
                exitTime = System.currentTimeMillis();
            } else {
                App.exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
