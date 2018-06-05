package us.xingkong.oktuil.body;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import us.xingkong.oktuil.response.BaseDownloadResponse;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 20:39
 * @描述:
 * @更新日志:
 */
public class ProgressResponseBody extends ResponseBody {

    private ResponseBody mResponseBody;
    private BaseDownloadResponse mDownloadResponse;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, BaseDownloadResponse downloadResponse) {
        mResponseBody = responseBody;
        mDownloadResponse = downloadResponse;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {

            long targetBytes;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long currentBytes = super.read(sink, byteCount);
                targetBytes += ((currentBytes != -1) ? currentBytes : 0);
                return currentBytes;
            }
        };
    }
}
