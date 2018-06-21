package us.xingkong.flyu.activity.welcome;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.activity.container.ContainerActivity;
import us.xingkong.flyu.activity.login.LoginActivity;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.util.S;

public class WelcomeActivity extends BaseActivity<WelcomeContract.Presenter>
        implements WelcomeContract.View {

    @BindView(R.id.welcome)
    AppCompatImageView welcome;

    private final static int GENERAL_REQUEST = 0;
    private List<String> permissionList;
    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private UserModelDao dao;

    @Override
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

    }

    @Override
    public boolean getUserState() {
        return dao.loadAll().isEmpty();
    }

    @Override
    public UserModel getUser() {
        List<UserModel> list = dao.loadAll();
        for (UserModel user : list) {
            if (user.getIsLogged()) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void applyPermissions() {
        permissionList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(WelcomeActivity.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(WelcomeActivity.this, permissions, GENERAL_REQUEST);
        }
    }

    @Override
    public void showMessage(String message) {
        new AlertDialog.Builder(WelcomeActivity.this)
                .setTitle(R.string.first_meeting)
                .setMessage(R.string.about_text)
                .setCancelable(false)
                .setPositiveButton(R.string.i_see, new DialogInterface.OnClickListener() {
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
    public void toOtherActivity(UserModel userModel) {
        Intent intent = new Intent();
        if (userModel == null) {
            intent.setClass(WelcomeActivity.this, LoginActivity.class);
        } else {
            intent.setClass(WelcomeActivity.this, ContainerActivity.class);
            intent.putExtra("Username", userModel.getUsername());
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GENERAL_REQUEST) {
            for (int i : grantResults) {
                boolean reRequest = ActivityCompat
                        .shouldShowRequestPermissionRationale(WelcomeActivity.this, permissions[i]);
                if (reRequest) {
                    S.shortSnackbar(findViewById(R.id.root), getString(R.string.i_will_die_without_permissions));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}
