package us.xingkong.oktuil.response;

import okhttp3.Response;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 18:18
 * @描述:
 * @更新日志:
 */
public interface ResponseInterface {
    void onSuccess(Response response);

    void onFailure(int statusCode, String errorMsg);

    void onProgress(long currentBytes, long targetBytes);
}
