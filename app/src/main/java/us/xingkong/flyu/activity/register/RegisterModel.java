package us.xingkong.flyu.activity.register;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.model.BmobUserModel;
import us.xingkong.flyu.rx.RxSchedulers;
import us.xingkong.flyu.util.RetrofitUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 20:53
 * @描述:
 * @更新日志:
 */
public class RegisterModel {

    public Observable<String> register(@NonNull String username, @NonNull String email, @NonNull String password) {
        return RetrofitUtil
                .getInstance()
                .url(Constants.BASE_LOGIN_AND_REGISTER_URL)
                .create()
                .register(username, email, password)
                .compose(RxSchedulers.<String>compose());
    }

    public BmobUserModel bmobRegister(String username, String password, String email) {
        BmobUserModel bmobUserModel = new BmobUserModel();
        bmobUserModel.setUsername(username);
        bmobUserModel.setPassword(password);
        bmobUserModel.setEmail(email);
        bmobUserModel.setAvatar(null);
        bmobUserModel.setSignature("此刻的心情");
        return bmobUserModel;
    }
}
