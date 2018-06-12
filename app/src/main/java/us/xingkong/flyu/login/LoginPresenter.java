package us.xingkong.flyu.login;

import android.view.View;

import java.util.List;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.base.OnRequestListener;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 23:23
 * @描述:
 * @更新日志:
 */
public class LoginPresenter implements LoginContract.Presenter,
        OnRequestListener<UserModel> {

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
    public void start() {
        UserModelDao dao = App.getInstance().getDaoSession().getUserModelDao();
        List<UserModel> list = dao.loadAll();
        for (UserModel user : list) {
            if (user.getIsLogged()) {
                mView.toOtherActivity(user);
            }
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void success(UserModel result) {
        mView.setEnable(true);
        mView.setVisibility(View.INVISIBLE);
        //mView.showMessage("登录成功");
        mView.toOtherActivity(result);
    }

    @Override
    public void failure(String result) {
        mView.setEnable(true);
        mView.setVisibility(View.INVISIBLE);
        switch (result) {
            case Constants.NETWORK_IS_UNAVAILABLE:
                mView.showMessage("网络连接错误");
                break;
            case Constants.PASSWORD_IS_WRONG:
                mView.showMessage("密码错误");
                break;
            case Constants.USER_DOESNT_EXIST:
                mView.showMessage("该用户未被注册");
                break;
            case Constants.USERNAME_OR_PASSWORD_IS_EMPTY:
                mView.showMessage("用户名或密码为空");
                break;
        }
    }
}
