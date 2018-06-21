package us.xingkong.flyu.activity.updatepassword;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.rx.RxSchedulers;
import us.xingkong.flyu.util.RetrofitUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/20 19:12
 * @描述:
 * @更新日志:
 */
public class UpdatePasswordModel {

    public Observable<String> updatePassword(@NonNull String username, @NonNull String password, @NonNull String repassword) {
        return RetrofitUtil
                .getInstance()
                .url(Constants.BASE_LOGIN_AND_REGISTER_URL)
                .create()
                .updatePassword(username, password, repassword)
                .compose(RxSchedulers.<String>compose());
    }
}
