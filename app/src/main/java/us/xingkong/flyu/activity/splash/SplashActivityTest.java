package us.xingkong.flyu.activity.splash;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.update.BmobUpdateAgent;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.activity.container.ContainerActivity;
import us.xingkong.flyu.activity.login.LoginActivity;
import us.xingkong.flyu.model.BmobUserModel;
import us.xingkong.flyu.util.S;

import static us.xingkong.flyu.app.Constants.GENERAL_REQUEST;

public class SplashActivityTest extends AppCompatActivity
        implements SplashContract.View {

    private List<String> permissionList;
    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private UserModelDao dao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionList = new ArrayList<>();
        BmobUpdateAgent.initAppVersion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyPermissions();
        if (getBmobUser() == null) {
            showMessage("关于这个app");
            return;
        }
        toOtherActivity();
    }

    /*@Override
    protected WelcomeContract.Presenter newPresenter() {
        return new WelcomePresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init() {
        permissionList = new ArrayList<>();
    }

    @Override
    protected void initView() {
        dao = App.getInstance().getDaoSession().getUserModelDao();

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.welcome)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(new RequestOptions().centerCrop())
                .into(welcome);

        mPresenter.loadUser();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }*/

    @Override
    public boolean getUserState() {
        //return dao.loadAll().isEmpty();
        return false;
    }

    @Override
    public UserModel getUser() {
       /* List<UserModel> list = dao.loadAll();
        for (UserModel user : list) {
            if (user.getIsLogged()) {
                return user;
            }
        }*/
        return null;
    }

    @Override
    public BmobUserModel getBmobUser() {
        return BmobUser.getCurrentUser(BmobUserModel.class);
    }

    @Override
    public void applyPermissions() {
        permissionList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(SplashActivityTest.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SplashActivityTest.this, permissions, GENERAL_REQUEST);
        }
    }

    @Override
    public void showMessage(String message) {
        new AlertDialog.Builder(SplashActivityTest.this)
                .setTitle(message)
                .setMessage(R.string.about_text)
                .setCancelable(false)
                .setPositiveButton(R.string.i_see, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(SplashActivityTest.this, LoginActivity.class));
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void toOtherActivity(UserModel userModel) {
        /*Intent intent = new Intent();
        if (userModel == null) {
            intent.setClass(SplashActivityTest.this, LoginActivity.class);
        } else {
            intent.setClass(SplashActivityTest.this, ContainerActivity.class);
            intent.putExtra("Username", userModel.getUsername());
        }
        startActivity(intent);
        finish();*/
    }

    @Override
    public void toOtherActivity() {
        startActivity(new Intent(SplashActivityTest.this, ContainerActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GENERAL_REQUEST) {
            for (int i : grantResults) {
                boolean reRequest = ActivityCompat
                        .shouldShowRequestPermissionRationale(SplashActivityTest.this, permissions[i]);
                if (reRequest) {
                    S.shortSnackbar(findViewById(R.id.root), getString(R.string.i_will_die_without_permissions));
                }
            }
        }
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public void onBackPressed() {

    }
}
