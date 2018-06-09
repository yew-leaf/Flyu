package us.xingkong.oktuil.response;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;
import us.xingkong.oktuil.OkUtil;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 23:28
 * @描述:
 * @更新日志:
 */
public abstract class ResultResponse implements ResponseInterface {

    @Override
    public void onSuccess(final Response response) {
        ResponseBody body = response.body();
        String responseBody;

        try {
            responseBody = body.string();
        } catch (IOException e) {
            e.printStackTrace();
            OkUtil.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(), "Fail To Read Response Body");
                }
            });
            return;
        } finally {
            if (body != null) {
                body.close();
            }
        }

        final String finalResponseBody = responseBody;
        OkUtil.mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(response.code(), finalResponseBody);
            }
        });
    }

    public abstract void onSuccess(int statusCode, String result);

    @Override
    public void onProgress(long currentBytes, long targetBytes) {

    }
}
