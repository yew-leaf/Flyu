package us.xingkong.flyu.activity.register;

import io.reactivex.Observable;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.rx.RxSchedulers;
import us.xingkong.flyu.util.RetrofitUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 20:53
 * @描述:
 * @更新日志:
 */
public class RegisterModel {

    public Observable<String> register(String username, String email, String password) {
        return RetrofitUtil
                .getInstance()
                .url(Constants.BASE_LOGIN_AND_REGISTER_URL)
                .create()
                .register(username, email, password)
                .compose(RxSchedulers.<String>compose());
    }
}
