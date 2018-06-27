package us.xingkong.flyu.activity.login;


import android.support.annotation.NonNull;

import io.reactivex.Observable;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.rx.RxSchedulers;
import us.xingkong.flyu.util.RetrofitUtil;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 23:19
 * @描述:
 * @更新日志:
 */
public class LoginModel {

    public Observable<String> login(@NonNull String username, @NonNull String password) {
        return RetrofitUtil
                .getInstance()
                .url(Constants.BASE_LOGIN_AND_REGISTER_URL)
                .create()
                .login(username, password)
                .compose(RxSchedulers.<String>compose());
    }
}
