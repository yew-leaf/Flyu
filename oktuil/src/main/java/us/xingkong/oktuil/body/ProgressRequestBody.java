package us.xingkong.oktuil.body;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import us.xingkong.oktuil.response.ResponseInterface;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 18:16
 * @描述:
 * @更新日志:
 */
public class ProgressRequestBody extends RequestBody {

    private RequestBody mRequestBody;
    private ResponseInterface mResponseInterface;
    private CountingSink mCountingSink;

    public ProgressRequestBody(RequestBody requestBody, ResponseInterface responseInterface) {
        mRequestBody = requestBody;
        mResponseInterface = responseInterface;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        mCountingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(mCountingSink);
        mRequestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink {

        private long currentBytes = 0;
        private long contentLength = 0L;

        CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            if (contentLength == 0) {
                //获得contentLength的值，后续不再调用
                contentLength = contentLength();
            }
            currentBytes += byteCount;
            mResponseInterface.onProgress(currentBytes, contentLength);
        }
    }
}
