package us.xingkong.oktuil.response;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;
import us.xingkong.oktuil.OkUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/5 13:52
 * @描述:
 * @更新日志:
 */
public abstract class GsonResponse<T> implements ResponseInterface {

    private Type mType;

    public GsonResponse() {
        Type clazz = getClass().getGenericSuperclass();
        if (clazz instanceof Class) {
            throw new RuntimeException("Miss Type Parameter");
        }
        ParameterizedType parameter = (ParameterizedType) clazz;
        mType = $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }

    private Type getType() {
        return mType;
    }

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

        try {
            final Gson gson = new Gson();
            final T gsonResponse = (T) gson.fromJson(finalResponseBody, getType());
            OkUtil.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onSuccess(response.code(),finalResponseBody, gsonResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            OkUtil.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(), "Fail To Parse Gson:" + finalResponseBody);
                }
            });
        }
    }

    public abstract void onSuccess(int statusCode,String result,T response);

    @Override
    public void onProgress(long currentBytes, long targetBytes) {

    }
}
