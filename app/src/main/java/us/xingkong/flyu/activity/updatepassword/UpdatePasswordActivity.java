package us.xingkong.flyu.activity.updatepassword;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.OnClick;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.util.S;
import us.xingkong.flyu.util.T;
import us.xingkong.flyu.util.UIUtil;

public class UpdatePasswordActivity extends BaseActivity<UpdatePasswordContract.Presenter>
        implements UpdatePasswordContract.View {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.username)
    AppCompatEditText username;
    @BindView(R.id.password)
    AppCompatEditText password;
    @BindView(R.id.repassword)
    AppCompatEditText repassword;
    @BindView(R.id.updatePassword)
    AppCompatButton updatePassword;

    @Override
    protected UpdatePasswordPresenter newPresenter() {
        return new UpdatePasswordPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_update_password;
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

    @OnClick(R.id.updatePassword)
    public void onUpdatePasswordClicked() {
        UIUtil.closeKeyboard(UpdatePasswordActivity.this);
        mPresenter.updatePassword();
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
    public String getRepassword() {
        return repassword.getText().toString();
    }

    @Override
    public void setVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void setEnable(boolean enable) {
        updatePassword.setEnabled(enable);
    }

    @Override
    public void toOtherActivity(UserModel userModel) {
        finishActivity();
    }

    @Override
    public void toOtherActivity() {

    }

    @Override
    public void showToast(String message) {
        T.shortToast(UpdatePasswordActivity.this, message);
    }

    @Override
    public void showMessage(String message) {
        S.shortSnackbar(findViewById(R.id.root), message);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
    }
}
