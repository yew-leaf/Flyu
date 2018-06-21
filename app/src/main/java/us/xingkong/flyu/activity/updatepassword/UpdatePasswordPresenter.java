package us.xingkong.flyu.activity.updatepassword;

import android.support.annotation.NonNull;
import android.view.View;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.base.BasePresenterImpl;
import us.xingkong.flyu.util.L;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/20 19:11
 * @描述:
 * @更新日志:
 */
public class UpdatePasswordPresenter extends BasePresenterImpl<UpdatePasswordContract.View>
        implements UpdatePasswordContract.Presenter {

    private UpdatePasswordModel updatePasswordModel;
    private UserModelDao dao;

    UpdatePasswordPresenter(@NonNull UpdatePasswordContract.View view) {
        super(view);
        updatePasswordModel = new UpdatePasswordModel();
        dao = App.getInstance().getDaoSession().getUserModelDao();
    }

    @Override
    public void updatePassword() {
        mView.setEnable(false);
        mView.setVisibility(View.VISIBLE);
        updatePasswordModel.updatePassword(mView.getUserName(), mView.getPassword()
                , mView.getRepassword()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(String s) {
                if (s.equals(Constants.SUCCESS)
                        || s.equals(Constants.UPDATE_PASSWORD_SUCCESSFULLY)) {
                    UserModel user = dao.load(mView.getUserName());
                    if (user != null) {
                        user.setPassword(mView.getRepassword());
                        user.setIsLogged(false);
                        dao.update(user);
                    } else {
                        //卸载过的可以在这里重新保存入数据库
                        user = new UserModel();
                        user.setUsername(mView.getUserName());
                        user.setPassword(mView.getRepassword());
                        user.setIsLogged(false);
                        dao.insert(user);
                    }
                    mView.showToast("修改密码成功");
                    mView.toOtherActivity(user);
                } else {
                    showMessage(s);
                }
                mView.setEnable(true);
                mView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                L.e("UpdatePasswordPresenterModel", e.getMessage());
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
        });
    }

    @Override
    public void subscribe() {
        super.subscribe();
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
    }

    private void showMessage(String message) {
        switch (message) {
            case Constants.PASSWORD_IS_WRONG:
                mView.showMessage("密码错误");
                break;
            case Constants.USERNAME_OR_PASSWORD_IS_EMPTY:
                mView.showMessage("用户名或密码为空");
                break;
            case Constants.UPDATE_PASSWORD_UNSUCCESSFULLY:
                mView.showMessage("修改密码失败");
                break;
        }
    }
}
