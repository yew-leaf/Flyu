package us.xingkong.flyu.activity.register;

import android.util.Log;

import java.util.HashMap;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.base.OnRequestListener;
import us.xingkong.oktuil.OkUtil;
import us.xingkong.oktuil.response.ResultResponse;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 20:53
 * @描述:
 * @更新日志:
 */
public class RegisterModel {

    private OnRequestListener<UserModel> mListener;
    private String mResult;

    public void setOnRequestListener(OnRequestListener<UserModel> listener) {
        mListener = listener;
    }

    public void register(final String username, final String email, final String password, final String repassword) {
        HashMap<String, String> params = new HashMap<>();
        params.put("u", username);
        params.put("e", email);
        params.put("p", password);
        OkUtil okUtil = App.getInstance().getOkUtil();
        if (!password.equals(repassword)) {
            mListener.failure(Constants.PASSWORD_ISNT_THE_SAME);
        } else {
            okUtil.get()
                    .url(Constants.REGISTER)
                    .params(params)
                    .tag(this)
                    .enqueue(new ResultResponse() {
                        @Override
                        public void onSuccess(int statusCode, String result) {
                            Log.i("RegisterModel", result);
                            if (result.equals(Constants.SUCCESS)) {
                                UserModel user = new UserModel();
                                user.setUsername(username);
                                user.setEmail(email);
                                user.setPassword(password);
                                user.setIsLogged(false);
                                UserModelDao dao = App.getInstance().getDaoSession().getUserModelDao();
                                dao.insert(user);
                                mListener.success(user);
                            } else {
                                mListener.failure(result);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, String errorMsg) {
                            Log.i("RegisterModel", "注册失败");
                            mListener.failure(Constants.NETWORK_IS_UNAVAILABLE);
                        }
                    });
        }
    }
}
