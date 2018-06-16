package us.xingkong.flyu.activity.register;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.activity.login.LoginActivity;
import us.xingkong.flyu.util.SnackbarUtil;
import us.xingkong.flyu.util.UiUtil;

public class RegisterActivity extends BaseActivity<RegisterContract.Presenter>
        implements RegisterContract.View {

    @BindView(R.id.username)
    AppCompatEditText username;
    @BindView(R.id.email)
    AppCompatEditText email;
    @BindView(R.id.password)
    AppCompatEditText password;
    @BindView(R.id.repassword)
    AppCompatEditText repassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.register)
    AppCompatButton register;

    //private RegisterContract.Presenter mPresenter;


    @Override
    protected int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        new RegisterPresenter(this);
        mPresenter = getPresenter();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.register();
                UiUtil.closeKeyboard(RegisterActivity.this);
            }
        });
    }

    @Override
    public String getUserName() {
        return username.getText().toString();
    }

    @Override
    public String getEmail() {
        return email.getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }

    @Override
    public String getRepassword() {
        return repassword.getText().toString();
    }

    @Override
    public void setVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void setEnable(boolean enable) {
        register.setEnabled(enable);
    }

    @Override
    public void showMessage(String message) {
        SnackbarUtil.shortSnackbar(findViewById(R.id.root), message).show();
    }

    @Override
    public void toOtherActivity(UserModel user) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
