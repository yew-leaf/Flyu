package us.xingkong.flyu.register;

import android.view.View;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.base.OnRequestListener;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 21:15
 * @描述:
 * @更新日志:
 */
public class RegisterPresenter implements RegisterContract.Presenter,
        OnRequestListener<UserModel> {

    private RegisterContract.View mView;
    private RegisterModel model;

    RegisterPresenter(RegisterContract.View view) {
        mView = view;
        mView.setPresenter(this);
        model = new RegisterModel();
        model.setOnRequestListener(this);
    }

    @Override
    public void register() {
        mView.setEnable(false);
        mView.setVisibility(View.VISIBLE);
        model.register(mView.getUserName(), mView.getEmail(), mView.getPassword(), mView.getRepassword());
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void success(UserModel result) {
        mView.setEnable(true);
        mView.setVisibility(View.INVISIBLE);
        mView.showMessage("注册成功");
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
            case Constants.USER_EXISTS:
                mView.showMessage("用户已被注册");
                break;
            case Constants.USERNAME_IS_TOO_LONG:
                mView.showMessage("用户名过长");
                break;
            case Constants.PASSWORD_IS_ILLEAGAL:
                mView.showMessage("密码长度不在6-16位");
                break;
            case Constants.EMAIL_IS_ILLEAGAL:
                mView.showMessage("邮件格式有误");
                break;
            case Constants.PASSWORD_ISNT_THE_SAME:
                mView.showMessage("两次密码不一致");
                break;
            case Constants.USERNAME_OR_PASSWORD_IS_EMPTY:
                mView.showMessage("不能为空");
                break;
        }
    }
}
