package us.xingkong.flyu.register;

import android.view.View;

import us.xingkong.flyu.UserModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 21:15
 * @描述:
 * @更新日志:
 */
public class RegisterPresenter implements RegisterContract.Presenter,
        RegisterModel.OnRequestListener {

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
    public void success(UserModel user) {
        mView.setEnable(true);
        mView.setVisibility(View.INVISIBLE);
        mView.showMessage("注册成功");
        mView.toOtherActivity(user);
    }

    @Override
    public void failure(String result) {
        mView.setEnable(true);
        mView.setVisibility(View.INVISIBLE);
        switch (result) {
            case "error:-1":
                mView.showMessage("两次密码不一致");
                break;
            case "error:1":
                mView.showMessage("用户已被注册");
                break;
            case "error:2":
                mView.showMessage("用户名过长");
                break;
            case "error:3":
                mView.showMessage("密码长度不在6-16位");
                break;
            case "error:4":
                mView.showMessage("邮件格式有误");
                break;
            case "error:0":
                mView.showMessage("不能为空");
                break;
        }
    }
}
