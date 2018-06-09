package us.xingkong.oktuil.callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import us.xingkong.oktuil.OkUtil;
import us.xingkong.oktuil.response.ResponseInterface;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 21:26
 * @描述:
 * @更新日志:
 */
public class Callback implements okhttp3.Callback {

    private ResponseInterface mResponseInterface;

    public Callback(ResponseInterface responseInterface) {
        mResponseInterface = responseInterface;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        OkUtil.mHandler.post(new Runnable() {
            @Override
            public void run() {
                mResponseInterface.onFailure(0, e.toString());
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        if (response.isSuccessful()) {
            mResponseInterface.onSuccess(response);
        } else {
            OkUtil.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseInterface.onFailure(response.code(), "errorCode:" + response.code());
                }
            });
        }
    }
}
