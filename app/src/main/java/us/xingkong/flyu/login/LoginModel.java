package us.xingkong.flyu.login;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.util.OkUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 23:19
 * @描述:
 * @更新日志:
 */
public class LoginModel {

    private OnRequestListener mListener;
    private String mResult;

    public interface OnRequestListener {
        void success(UserModel user);

        void failure(String result);
    }

    public void setOnRequestListener(OnRequestListener listener) {
        mListener = listener;
    }

    public void login(final String username, final String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("u", username);
        params.put("p", password);
        OkUtil.get(Constants.LOGIN, params, new OkUtil.DataCallBack() {
            @Override
            public void onSuccess(String result) throws Exception {
                Log.e("result", result);
                mResult = result;
                if (result.equals("succ")) {
                    UserModel user = new UserModel();
                    user.setUsername(username);
                    user.setPassword(password);
                    mListener.success(user);
                } else
                    mListener.failure(mResult);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                Log.e("RegisterModel", "注册失败");
                mResult = "error:-1";
                mListener.failure(mResult);
            }
        });
    }
}
