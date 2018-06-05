package us.xingkong.oktuil;

import android.os.Handler;
import android.os.Looper;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import us.xingkong.oktuil.builder.DownloadBuilder;
import us.xingkong.oktuil.builder.GetBuilder;
import us.xingkong.oktuil.builder.PostBuilder;
import us.xingkong.oktuil.builder.UploadBuilder;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 20:58
 * @描述: 基本上都是照搬tsy12321大神的https://github.com/tsy12321/MyOkHttp，侵权删
 * @更新日志:
 */
public class OkUtil {
    private static OkHttpClient mOkHttpClient;
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public OkUtil() {
        this(null);
    }

    public OkUtil(OkHttpClient okHttpClient) {
        if (mOkHttpClient == null) {
            synchronized (OkUtil.class) {
                if (mOkHttpClient == null) {
                    if (okHttpClient == null) {
                        mOkHttpClient = new OkHttpClient();
                    } else
                        mOkHttpClient = okHttpClient;
                }
            }
        }
    }

    public GetBuilder get() {
        return new GetBuilder(this);
    }

    public PostBuilder post() {
        return new PostBuilder(this);
    }

    public UploadBuilder upload() {
        return new UploadBuilder(this);
    }

    public DownloadBuilder download() {
        return new DownloadBuilder(this);
    }

    public void cancel(Object tag) {
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }

        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
