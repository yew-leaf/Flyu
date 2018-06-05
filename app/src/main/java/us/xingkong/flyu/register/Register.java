package us.xingkong.flyu.register;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.main.MainActivity;
import us.xingkong.flyu.util.UiUtil;

public class Register extends BaseActivity<RegisterContract.Presenter>
        implements RegisterContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    @BindView(R.id.login)
    AppCompatButton submit;
    @BindView(R.id.to)
    AppCompatButton to;

    private RegisterContract.Presenter mPresenter;


    @Override
    protected int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        toolbar.setTitle("注册");

        new RegisterPresenter(this);
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
                mPresenter.register();
                UiUtil.closeKeyboard(Register.this);
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
                finish();
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
        submit.setEnabled(enable);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(submit, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void toOtherActivity(UserModel user) {
        Intent intent = new Intent(Register.this, MainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}
