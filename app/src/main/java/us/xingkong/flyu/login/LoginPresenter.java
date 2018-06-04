package us.xingkong.flyu.login;

import android.view.View;

import us.xingkong.flyu.UserModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 23:23
 * @描述:
 * @更新日志:
 */
public class LoginPresenter implements LoginContract.Presenter,
        LoginModel.OnRequestListener {

    private LoginContract.View mView;
    private LoginModel model;

    LoginPresenter(LoginContract.View view) {
        mView = view;
        mView.setPresenter(this);
        model = new LoginModel();
        model.setOnRequestListener(this);
    }

    @Override
    public void login() {
        mView.setEnable(false);
        mView.setVisibility(View.VISIBLE);
        model.login(mView.getUserName(), mView.getPassword());
    }

    @Override
    public void setCheckbox() {

    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void success(UserModel user) {
        mView.setEnable(true);
        mView.setVisibility(View.INVISIBLE);
        mView.showMessage("登录成功");
        mView.toOtherActivity(user);
    }

    @Override
    public void failure(String result) {
        mView.setEnable(true);
        mView.setVisibility(View.INVISIBLE);
        switch (result) {
            case "error:-1":
                mView.showMessage("网络连接错误");
                break;
            case "2":
                mView.showMessage("密码错误");
                break;
            case "3":
                mView.showMessage("该用户未被注册");
                break;
            case "4":
                mView.showMessage("用户名或密码为空");
                break;
        }
    }
}
