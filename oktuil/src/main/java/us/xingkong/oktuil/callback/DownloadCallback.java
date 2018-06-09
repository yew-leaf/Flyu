package us.xingkong.oktuil.callback;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import us.xingkong.oktuil.response.BaseDownloadResponse;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 22:13
 * @描述:
 * @更新日志:
 */
public class DownloadCallback implements Callback {

    private String mFilePath;
    private Long mCompleteBytes;
    private BaseDownloadResponse mDownloadResponse;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public DownloadCallback(String filePath, Long completeBytes, BaseDownloadResponse downloadResponse) {
        mFilePath = filePath;
        mCompleteBytes = completeBytes;
        mDownloadResponse = downloadResponse;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mDownloadResponse != null) {
                    mDownloadResponse.onFailure(e.toString());
                }
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        ResponseBody body = response.body();

        try {
            if (response.isSuccessful()) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDownloadResponse != null) {
                            mDownloadResponse.onStart(response.body().contentLength());
                        }
                    }
                });

                try {
                    if (response.header("Content-Range") == null || response.header("Content-Range").length() == 0) {
                        //返回的没有Content-Range 不支持断点下载 需要重新下载
                        mCompleteBytes = 0L;
                    }

                    saveFile(response, mFilePath, mCompleteBytes);

                    final File file = new File(mFilePath);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mDownloadResponse != null) {
                                mDownloadResponse.onFinish(file);
                            }
                        }
                    });
                } catch (final Exception e) {
                    if (call.isCanceled()) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mDownloadResponse != null) {
                                    mDownloadResponse.onCancel();
                                }
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mDownloadResponse != null) {
                                    mDownloadResponse.onFailure("errorMsg:" + e.toString());
                                }
                            }
                        });
                    }
                }
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDownloadResponse != null) {
                            mDownloadResponse.onFailure("errorCode:" + response.code());
                        }
                    }
                });
            }
        }finally {
            if(body != null) {
                body.close();
            }
        }
    }

    private void saveFile(Response response, String filePath, Long completeBytes) throws Exception {
        InputStream ips = null;
        byte[] buffer = new byte[4 * 1024];
        int length;
        RandomAccessFile file = null;

        try {
            ips = response.body().byteStream();

            file = new RandomAccessFile(filePath, "rwd");
            if (completeBytes > 0L) {
                file.seek(completeBytes);
            }

            long currentLength = 0;
            final long targetLength = response.body().contentLength();
            while ((length = ips.read(buffer)) != -1) {
                file.write(buffer, 0, length);
                currentLength += length;

                final long finalCurrentLength = currentLength;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDownloadResponse != null) {
                            mDownloadResponse.onProgress(finalCurrentLength, targetLength);
                        }
                    }
                });
            }
        } finally {
            try {
                if (ips != null) ips.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (file != null) file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
