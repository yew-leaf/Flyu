package us.xingkong.oktuil.response;

import java.io.File;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 20:47
 * @描述:
 * @更新日志:
 */
public abstract class BaseDownloadResponse {

    public void onStart(long targetBytes) {

    }

    public void onCancel() {

    }

    public abstract void onFinish(File file);

    public abstract void onProgress(long currentBytes, long targetBytes);

    public abstract void onFailure(String errorMsg);
}
