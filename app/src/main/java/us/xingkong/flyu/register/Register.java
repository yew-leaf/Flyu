package us.xingkong.flyu.register;

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
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.main.MainActivity;

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
    @BindView(R.id.register)
    AppCompatButton register;
    @BindView(R.id.to)
    AppCompatButton to;

    private long exitTime;
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
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.register();
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
        register.setEnabled(enable);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(register, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void toOtherActivity(UserModel user) {
        Intent intent = new Intent(Register.this, MainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Snackbar.make(register, "再按一次退出", Snackbar.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
