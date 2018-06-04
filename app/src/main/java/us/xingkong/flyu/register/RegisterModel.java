package us.xingkong.flyu.register;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.util.OkUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 20:53
 * @描述:
 * @更新日志:
 */
public class RegisterModel {

    private OnRequestListener mListener;
    private String mResult;

    public interface OnRequestListener {
        void success(UserModel user);

        void failure(String result);
    }

    public void setOnRequestListener(OnRequestListener listener) {
        mListener = listener;
    }

    public void register(final String username, final String email, final String password, final String repassword) {
        HashMap<String, String> params = new HashMap<>();
        params.put("u", username);
        params.put("e", email);
        params.put("p", password);
        if (!password.equals(repassword)) {
            mResult = "error:5";
            mListener.failure(mResult);
        } else
            OkUtil.get(Constants.REGISTER, params, new OkUtil.DataCallBack() {
                @Override
                public void onSuccess(String result) throws Exception {
                    Log.e("result", result);
                    mResult = result;
                    if (result.equals("succ")) {
                        UserModel user = new UserModel();
                        user.setUsername(username);
                        user.setEmail(email);
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
