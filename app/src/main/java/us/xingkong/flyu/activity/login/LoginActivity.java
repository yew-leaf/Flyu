package us.xingkong.flyu.activity.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.activity.container.ContainerActivity;
import us.xingkong.flyu.activity.register.RegisterActivity;
import us.xingkong.flyu.util.SnackbarUtil;
import us.xingkong.flyu.util.UiUtil;

public class LoginActivity extends BaseActivity<LoginContract.Presenter>
        implements LoginContract.View, View.OnClickListener {

    @BindView(R.id.username)
    AppCompatEditText username;
    @BindView(R.id.password)
    AppCompatEditText password;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.login)
    AppCompatButton login;
    @BindView(R.id.register)
    AppCompatButton register;

    private final static int GENERAL_REQUEST = 0;
    private List<String> permissionList;
    private long exitTime;
    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    //private LoginContract.Presenter mPresenter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        permissionList = new ArrayList<>();
        new LoginPresenter(this);
        mPresenter = getPresenter();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @TargetApi(23)
    public void applyPermissions() {
        permissionList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LoginActivity.this, permissions, GENERAL_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GENERAL_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                boolean reRequest = ActivityCompat
                        .shouldShowRequestPermissionRationale(LoginActivity.this, permissions[i]);
                if (reRequest) {
                    SnackbarUtil.shortSnackbar(findViewById(R.id.root), "没有权限我会死的......").show();
                }
            }
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
    public void showMessage(String message) {
        SnackbarUtil.shortSnackbar(findViewById(R.id.root), message).show();
    }

    @Override
    public void toOtherActivity(UserModel user) {
        Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
        intent.putExtra("Username", user.getUsername());
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                UiUtil.closeKeyboard(LoginActivity.this);
                mPresenter.login();
                break;
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                SnackbarUtil.shortSnackbar(findViewById(R.id.root), "再按一次退出").show();
                exitTime = System.currentTimeMillis();
            } else {
                App.exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
