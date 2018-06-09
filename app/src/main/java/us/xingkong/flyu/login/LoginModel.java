package us.xingkong.flyu.login;

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
 * @创建时间: 2018/6/3 23:19
 * @描述:
 * @更新日志:
 */
public class LoginModel {

    private OnRequestListener<UserModel> mListener;
    private String mResult;

    public void setOnRequestListener(OnRequestListener<UserModel> listener) {
        mListener = listener;
    }

    public void login(final String username, final String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("u", username);
        params.put("p", password);
        OkUtil okUtil = App.getInstance().getOkUtil();
        okUtil.get()
                .url(Constants.LOGIN)
                .params(params)
                .tag(this)
                .enqueue(new ResultResponse() {
                    @Override
                    public void onSuccess(int statusCode, String result) {
                        Log.e("result", result);
                        mResult = result;
                        if (result.equals(Constants.SUCCESS)) {
                            UserModelDao dao = App.getInstance().getDaoSession().getUserModelDao();
                            UserModel user = dao.load(username);
                            user.setIsLogged(true);
                            dao.update(user);
                            mListener.success(user);
                        } else {
                            mListener.failure(mResult);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String errorMsg) {
                        mResult = Constants.NETWORK_IS_UNAVAILABLE;
                        mListener.failure(mResult);
                    }
                });
    }
}
