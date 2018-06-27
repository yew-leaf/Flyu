package us.xingkong.flyu.activity.login;


import android.support.annotation.NonNull;
import android.view.View;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.base.BasePresenterImpl;
import us.xingkong.flyu.model.BmobUserModel;
import us.xingkong.flyu.util.L;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 23:23
 * @描述:
 * @更新日志:
 */
public class LoginPresenter extends BasePresenterImpl<LoginContract.View>
        implements LoginContract.Presenter {

    private LoginModel loginModel;
    private UserModelDao dao;

    LoginPresenter(@NonNull LoginContract.View view) {
        super(view);
        //loginModel = new LoginModel();
        //dao = App.getInstance().getDaoSession().getUserModelDao();
    }

    @Override
    public void login() {
        mView.setEnable(false);
        mView.setVisibility(View.VISIBLE);

        BmobUser.loginByAccount(mView.getUserName(), mView.getPassword(), new LogInListener<BmobUserModel>() {
            @Override
            public void done(BmobUserModel bmobUserModel, BmobException e) {
                if (e == null) {
                    L.i("LoginPresenter", mView.getUserName() + "login successfully");
                    mView.showToast("登录成功");
                    mView.toOtherActivity();
                } else {
                    L.e("LoginPresenter", e.getMessage());
                    L.e("ErrorCode", e.getErrorCode() + "");
                    showBmobMessage(e.getErrorCode());
                }
                mView.setEnable(true);
                mView.setVisibility(View.INVISIBLE);
            }
        });

        /*loginModel.login(mView.getUserName(), mView.getPassword()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(String s) {
                if (s.equals(Constants.SUCCESS)) {
                    UserModel userModel = dao.load(mView.getUserName());
                    userModel.setIsLogged(true);
                    dao.update(userModel);
                    mView.showToast("登录成功");
                    mView.toOtherActivity(userModel);
                } else {
                    showMessage(s);
                }
                mView.setEnable(true);
                mView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
                    mView.showMessage("网络连接错误");
                } else if (e instanceof SocketTimeoutException) {
                    mView.showMessage("网络连接超时");
                }
                mView.setEnable(true);
                mView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {

            }
        });*/
    }

    @Override
    public void subscribe() {
        super.subscribe();
        /*List<UserModel> list = dao.loadAll();
        for (UserModel user : list) {
            if (user.getIsLogged()) {
                mView.toOtherActivity(user);
            }
        }*/
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
    }

    private void showMessage(String message) {
        switch (message) {
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

    private void showBmobMessage(int errorCode) {
        switch (errorCode) {
            case 101:
                mView.showMessage("用户名或密码不正确");
                break;
            case 9018:
                mView.showMessage("用户名或密码为空");
                break;
            case 9016:
                mView.showMessage("网络不可用，检查下网络吧");
                break;
        }
    }
}
